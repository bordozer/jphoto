package controllers.users.messages.list;

import core.context.EnvironmentContext;
import core.enums.PrivateMessageType;
import core.general.base.PagingModel;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.services.dao.PrivateMessageDaoImpl;
import core.services.entry.PrivateMessageService;
import core.services.pageTitle.PageTitleUserUtilsService;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.sql.BaseSqlUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import utils.ListUtils;
import utils.NumberUtils;
import utils.PagingUtils;
import utils.UserUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newHashSet;

@Controller
@RequestMapping( "members/{userId}/messages/" )
public class PrivateMessageListController {

	private static final String MODEL_NAME = "privateMessageListModel";
	private static final String VIEW = "users/messages/list/PrivateMessages";

	private static final int ITEMS_ON_PAGE = 25;

	@Autowired
	private UserService userService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PageTitleUserUtilsService pageTitleUserUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@ModelAttribute( MODEL_NAME )
	public PrivateMessageListModel prepareModel( final @PathVariable( "userId" ) String _userId ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );

		final User forUser = userService.load( userId );

		securityService.assertUserEqualsToCurrentUser( forUser );

		final PrivateMessageListModel model = new PrivateMessageListModel();
		model.setForUser( forUser );

		return model;
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel();
		PagingUtils.initPagingModel( pagingModel, request );

		pagingModel.setItemsOnPage( ITEMS_ON_PAGE );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showAllList( final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return getMessageView( PrivateMessageType.USER_PRIVATE_MESSAGE_IN, model, pagingModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{messageTypeId}/" )
	public String showList( final @PathVariable( "messageTypeId" ) int messageTypeId, final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		final PrivateMessageType messageType = PrivateMessageType.getById( messageTypeId );

		return getMessageView( messageType, model, pagingModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/with/{withUserId}/" )
	public String showMessagingWithUser( final @PathVariable( "withUserId" ) String _withUserId, final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model ) {

		securityService.assertUserExists( _withUserId );

		final int withUserId = NumberUtils.convertToInt( _withUserId );
		final User forUser = model.getForUser();

		final List<PrivateMessage> receivedMessages = privateMessageService.loadReceivedPrivateMessages( forUser.getId(), PrivateMessageType.USER_PRIVATE_MESSAGE_IN );
		final List<PrivateMessage> sentMessages = privateMessageService.loadSentPrivateMessages( forUser.getId() );

		model.setUsersWhoCommunicatedWithUser( getUsersWhoCommunicatedWithUser( forUser ) );
		model.setMessagingWithUserId( withUserId );

		CollectionUtils.filter( receivedMessages, new Predicate<PrivateMessage>() {
			@Override
			public boolean evaluate( final PrivateMessage privateMessage ) {
				return privateMessage.getFromUser().getId() == withUserId;
			}
		} );

		CollectionUtils.filter( sentMessages, new Predicate<PrivateMessage>() {
			@Override
			public boolean evaluate( final PrivateMessage privateMessage ) {
				return privateMessage.getToUser().getId() == withUserId;
			}
		} );

		final List<PrivateMessage> messagesToShow = newArrayList();
		messagesToShow.addAll( receivedMessages );
		messagesToShow.addAll( sentMessages );

		Collections.sort( messagesToShow, new Comparator<PrivateMessage>() {
			@Override
			public int compare( final PrivateMessage o1, final PrivateMessage o2 ) {
				return o2.getCreationTime().compareTo( o1.getCreationTime() );
			}
		} );

		model.setPrivateMessages( messagesToShow );

		model.setMessagesByType( getMessagesByType( model.getForUser() ) );

		markMessagesAsReadIfNecessary( receivedMessages, forUser );

		model.setPageTitleData( pageTitleUserUtilsService.getUserPrivateMessagesListData( forUser ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/delete/selected/" )
	public String deleteSelectedMessages( final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model, final HttpServletRequest request ) {

		final List<String> _selectedMessagesIds = model.getSelectedMessagesIds();

		if ( _selectedMessagesIds != null ) {
			privateMessageService.delete( ListUtils.convertStringListToInteger( _selectedMessagesIds ) );
		}

		return String.format( "redirect:%s", request.getHeader( "Referer" ) );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/type/{messageTypeId}/delete/all/" )
	public String deleteAllMessages( final @PathVariable( "messageTypeId" ) String _messageTypeId, final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model, final HttpServletRequest request ) {

		final PrivateMessageType messageType = getMessageTypeId( _messageTypeId );
		privateMessageService.deleteAll( model.getForUser().getId(), messageType );

		return String.format( "redirect:%s", request.getHeader( "Referer" ) );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/type/{messageTypeId}/markAllAsRead/" )
	public String markAllMessagesAsRead( final @PathVariable( "messageTypeId" ) String _messageTypeId, final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model ) {

		final User forUser = model.getForUser();

		final PrivateMessageType messageType = getMessageTypeId( _messageTypeId );

		final List<PrivateMessage> receivedMessages = privateMessageService.loadReceivedPrivateMessages( forUser.getId(), messageType );

		markMessagesAsReadIfNecessary( receivedMessages, forUser );

		return String.format( "redirect:%s/members/%s/messages/%s/", urlUtilsService.getBaseURLWithPrefix(), model.getForUser().getId(), messageType.getId() );
	}

	private PrivateMessageType getMessageTypeId( final String _messageTypeId ) {
		return PrivateMessageType.getById( NumberUtils.convertToInt( _messageTypeId ) ); // TODO: exception if _messageTypeId is rubbish
	}

	private Map<PrivateMessageType, MessageTypeData> getMessagesByType( final User forUser ) {
		final Map<PrivateMessageType, MessageTypeData> messagesByType = newLinkedHashMap();

		for ( final PrivateMessageType messageType : PrivateMessageType.values() ) {

			if ( messageType == PrivateMessageType.ADMIN_NOTIFICATIONS && !securityService.isSuperAdminUser( EnvironmentContext.getCurrentUser().getId() ) ) {
				continue;
			}

			final MessageTypeData data = new MessageTypeData();
			final int messagesCount;
			if ( messageType != PrivateMessageType.USER_PRIVATE_MESSAGE_OUT ) {
				messagesCount = privateMessageService.getReceivedPrivateMessagesCount( forUser.getId(), messageType );
			} else {
				messagesCount = privateMessageService.getSentPrivateMessagesCount( forUser.getId() );
			}

			final int newMessagesCount = messageType != PrivateMessageType.USER_PRIVATE_MESSAGE_OUT ? privateMessageService.getNewReceivedPrivateMessagesCount( forUser.getId(), messageType ) : 0;

			data.setMessages( messagesCount );
			data.setNewMessages( newMessagesCount );

			messagesByType.put( messageType, data );
		}

		return messagesByType;
	}

	private String getMessageView( final PrivateMessageType messageType, final PrivateMessageListModel model, final PagingModel pagingModel ) {
		final User forUser = model.getForUser();

		model.setPrivateMessageType( messageType );

		final List<PrivateMessage> messagesToShow;
		if ( messageType != PrivateMessageType.USER_PRIVATE_MESSAGE_OUT ) {
			messagesToShow = getReceivedMessages( forUser, pagingModel, messageType );
		} else {
			messagesToShow = getSentMessages( forUser, pagingModel );
		}

		markMessagesAsReadIfNecessary( messagesToShow, forUser );

		model.setPrivateMessages( messagesToShow );

		model.setUsersWhoCommunicatedWithUser( getUsersWhoCommunicatedWithUser( forUser ) );

		model.setMessagesByType( getMessagesByType( model.getForUser() ) );

		model.setShowPaging( true );

		model.setPageTitleData( pageTitleUserUtilsService.getUserPrivateMessagesListData( forUser ) );

		return VIEW;
	}

	private List<PrivateMessage> getReceivedMessages( final User forUser, final PagingModel pagingModel, final PrivateMessageType messageType ) {
		return getMessagesByQuery( getReceivedMessagesQuery( forUser, messageType ), pagingModel );
	}

	private List<PrivateMessage> getSentMessages( final User forUser, final PagingModel pagingModel ) {
		return getMessagesByQuery( getSentMessagesQuery( forUser ), pagingModel );
	}

	private SqlIdsSelectQuery getReceivedMessagesQuery( final User toUser, final PrivateMessageType messageType ) {
		final SqlTable tPrivateMessage = new SqlTable( PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE );

		final SqlIdsSelectQuery selectIdsQuery = new SqlIdsSelectQuery( tPrivateMessage );

		final SqlColumnSelect tPrivateMessageColToUser = new SqlColumnSelect( tPrivateMessage, PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID );

		final SqlLogicallyJoinable and = new SqlCondition( tPrivateMessageColToUser, SqlCriteriaOperator.EQUALS, toUser.getId(), dateUtilsService );
		selectIdsQuery.addWhereAnd( and );

		final SqlColumnSelect tPrivateMessageColMessageTypeId = new SqlColumnSelect( tPrivateMessage, PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID );
		final SqlLogicallyJoinable and2 = new SqlCondition( tPrivateMessageColMessageTypeId, SqlCriteriaOperator.EQUALS, messageType.getId(), dateUtilsService );
		selectIdsQuery.addWhereAnd( and2 );

		final SqlColumnSelect tPrivateMessageColCreateTime = new SqlColumnSelect( tPrivateMessage, PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE_COL_CREATE_TIME );
		selectIdsQuery.addSortingDesc( tPrivateMessageColCreateTime );

		return selectIdsQuery;
	}

	private SqlIdsSelectQuery getSentMessagesQuery( final User fromUser ) {
		final SqlTable tPrivateMessage = new SqlTable( PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE );

		final SqlIdsSelectQuery selectIdsQuery = new SqlIdsSelectQuery( tPrivateMessage );

		final SqlColumnSelect tPrivateMessageColToUser = new SqlColumnSelect( tPrivateMessage, PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID );

		final SqlLogicallyJoinable and = new SqlCondition( tPrivateMessageColToUser, SqlCriteriaOperator.EQUALS, fromUser.getId(), dateUtilsService );
		selectIdsQuery.addWhereAnd( and );

		final SqlColumnSelect tPrivateMessageColMessageTypeId = new SqlColumnSelect( tPrivateMessage, PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID );
		final SqlLogicallyJoinable and2 = new SqlCondition( tPrivateMessageColMessageTypeId, SqlCriteriaOperator.EQUALS, PrivateMessageType.USER_PRIVATE_MESSAGE_OUT.getId(), dateUtilsService );
		selectIdsQuery.addWhereAnd( and2 );

		final SqlColumnSelect tPrivateMessageColCreateTime = new SqlColumnSelect( tPrivateMessage, PrivateMessageDaoImpl.TABLE_PRIVATE_MESSAGE_COL_CREATE_TIME );
		selectIdsQuery.addSortingDesc( tPrivateMessageColCreateTime );

		return selectIdsQuery;
	}

	private List<PrivateMessage> getMessagesByQuery( final SqlIdsSelectQuery selectIdsQuery, final PagingModel pagingModel ) {

		baseSqlUtilsService.initLimitAndOffset( selectIdsQuery, pagingModel );

		final List<PrivateMessage> receivedMessages = newArrayList();

		final SqlSelectIdsResult idsResult = privateMessageService.load( selectIdsQuery );
		final List<Integer> ids = idsResult.getIds();
		for ( final int id : ids ) {
			receivedMessages.add( privateMessageService.load( id ) );
		}

		pagingModel.setTotalItems( idsResult.getRecordQty() );

		return receivedMessages;
	}

	private void markMessagesAsReadIfNecessary( final List<PrivateMessage> messagesToShow, final User messagesAddressat ) {
		if ( UserUtils.isUsersEqual( messagesAddressat, EnvironmentContext.getCurrentUser() ) ) {
			privateMessageService.markPrivateMessagesAsRead( messagesToShow );
		}
	}

	private List<UsersWhoCommunicatedWithUser> getUsersWhoCommunicatedWithUser( final User user ) {

		final List<PrivateMessage> receivedMessages = privateMessageService.loadReceivedPrivateMessages( user.getId(), PrivateMessageType.USER_PRIVATE_MESSAGE_IN );
		final List<PrivateMessage> sentMessages = privateMessageService.loadSentPrivateMessages( user.getId() );

		final List<PrivateMessage> messages = newArrayList( receivedMessages );
		final Set<UsersWhoCommunicatedWithUser> whoGotMessagesUserSet = newHashSet();

		for ( final PrivateMessage message : sentMessages ) {
			final User toUser = message.getToUser();
			UsersWhoCommunicatedWithUser communicator = getCommunicatorFromSet( whoGotMessagesUserSet, toUser );

			if ( communicator == null ) {
				communicator = new UsersWhoCommunicatedWithUser( toUser );
				communicator.setSentMessagesCount( 1 );
			} else {
				communicator.setSentMessagesCount( communicator.getSentMessagesCount() + 1 );
			}

			whoGotMessagesUserSet.add( communicator );
		}

		CollectionUtils.filter( messages, new Predicate<PrivateMessage>() {
			@Override
			public boolean evaluate( final PrivateMessage privateMessage ) {
				return privateMessage.getPrivateMessageType() == PrivateMessageType.USER_PRIVATE_MESSAGE_IN;
			}
		} );

		for ( final PrivateMessage message : messages ) {
			final User fromUser = message.getFromUser();
			UsersWhoCommunicatedWithUser communicator = getCommunicatorFromSet( whoGotMessagesUserSet, fromUser );

			if ( communicator == null ) {
				communicator = new UsersWhoCommunicatedWithUser( fromUser );
				communicator.setReceivedMessagesCount( 1 );
			} else {
				communicator.setReceivedMessagesCount( communicator.getReceivedMessagesCount() + 1 );
			}

			whoGotMessagesUserSet.add( communicator );
		}

		CollectionUtils.filter( whoGotMessagesUserSet, new Predicate<UsersWhoCommunicatedWithUser>() {
			@Override
			public boolean evaluate( final UsersWhoCommunicatedWithUser communicator ) {
				return !UserUtils.isUsersEqual( communicator.getWithUser(), user );
			}
		} );

		final List<UsersWhoCommunicatedWithUser> whoGotMessagesUsers = newArrayList( whoGotMessagesUserSet );
		Collections.sort( whoGotMessagesUsers, new Comparator<UsersWhoCommunicatedWithUser>() {
			@Override
			public int compare( final UsersWhoCommunicatedWithUser o1, final UsersWhoCommunicatedWithUser o2 ) {
				return o1.getWithUser().getNameEscaped().compareTo( o2.getWithUser().getNameEscaped() );
			}
		} );

		return whoGotMessagesUsers;
	}

	private UsersWhoCommunicatedWithUser getCommunicatorFromSet( final Set<UsersWhoCommunicatedWithUser> usersWhoCommunicatedWithUser, final User withUser ) {
		for ( final UsersWhoCommunicatedWithUser communicator : usersWhoCommunicatedWithUser ) {
			if ( communicator.getWithUser().equals( withUser ) ) {
				return communicator;
			}
		}

		return null;
	}
}
