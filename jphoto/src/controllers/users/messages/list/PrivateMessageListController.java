package controllers.users.messages.list;

import core.enums.PrivateMessageType;
import core.context.EnvironmentContext;
import core.general.user.User;
import core.general.message.PrivateMessage;
import core.services.entry.PrivateMessageService;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.utils.UrlUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utils.ListUtils;
import utils.NumberUtils;
import utils.UserUtils;
import core.services.pageTitle.PageTitleUserUtilsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

@Controller
@RequestMapping( "members/{userId}/messages/" )
public class PrivateMessageListController {

	private static final String MODEL_NAME = "privateMessageListModel";
	private static final String VIEW = "users/messages/list/PrivateMessages";

	@Autowired
	private UserService userService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PageTitleUserUtilsService pageTitleUserUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@ModelAttribute( MODEL_NAME )
	public PrivateMessageListModel prepareModel( final @PathVariable( "userId" ) String _userId ) {

		securityService.assertUserExists( _userId );

		final int userId = NumberUtils.convertToInt( _userId );
		final PrivateMessageListModel model = new PrivateMessageListModel();

		model.setForUser( userService.load( userId ) );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showAllList( final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model ) {
		return getMessageView( PrivateMessageType.USER_PRIVATE_MESSAGE_IN, model );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{messageTypeId}/" )
	public String showList( final @PathVariable( "messageTypeId" ) int messageTypeId, final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model ) {
		final PrivateMessageType messageType = PrivateMessageType.getById( messageTypeId );

		return getMessageView( messageType, model );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/with/{withUserId}/" )
	public String showMessagingWithUser( final @PathVariable( "withUserId" ) String _withUserId, final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model ) {

		securityService.assertUserExists( _withUserId );

		final int withUserId = NumberUtils.convertToInt( _withUserId );
		final User forUser = model.getForUser();

		final List<PrivateMessage> receivedMessages = privateMessageService.loadMessagesToUser( forUser.getId(), PrivateMessageType.USER_PRIVATE_MESSAGE_IN );
		final List<PrivateMessage> sentMessages = privateMessageService.loadMessagesFromUser( forUser.getId() );

		model.setUsersWhoCommunicatedWithUser( getUsersWhoCommunicatedWithUser( receivedMessages, sentMessages, forUser ) );
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

		model.setPageTitleData( pageTitleUserUtilsService.getUserPrivateMessagesListData( forUser ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/**" )
	public String deleteMessages( final @ModelAttribute( MODEL_NAME ) PrivateMessageListModel model, final HttpServletRequest request ) {

		final List<String> _selectedMessagesIds = model.getSelectedMessagesIds();

		if ( _selectedMessagesIds != null ) {
			final List<Integer> selectedMessagesIds = ListUtils.convertStringListToInteger( _selectedMessagesIds );
			for ( final int messagesId : selectedMessagesIds ) {
				privateMessageService.delete( messagesId );
			}
		}

		return String.format( "redirect:%s", request.getHeader( "Referer" ) );
	}

	private String getMessageView( final PrivateMessageType messageType, final PrivateMessageListModel model ) {
		final User forUser = model.getForUser();

		securityService.assertUserEqualsToCurrentUser( forUser );

		model.setPrivateMessageType( messageType );

		final List<PrivateMessage> messagesToShow;

		final List<PrivateMessage> receivedMessages = privateMessageService.loadMessagesToUser( forUser.getId(), messageType );
		final List<PrivateMessage> sentMessages = privateMessageService.loadMessagesFromUser( forUser.getId() );

		if ( messageType != PrivateMessageType.USER_PRIVATE_MESSAGE_OUT ) {
			messagesToShow = receivedMessages;
		} else {
			messagesToShow = sentMessages;
		}

		markMessagesAsReadIfNecessary( messagesToShow );

		model.setPrivateMessages( messagesToShow );

		model.setUsersWhoCommunicatedWithUser( getUsersWhoCommunicatedWithUser( receivedMessages, sentMessages, forUser ) );

		model.setPageTitleData( pageTitleUserUtilsService.getUserPrivateMessagesListData( forUser ) );

		return VIEW;
	}

	private void markMessagesAsReadIfNecessary( final List<PrivateMessage> messagesToShow ) {
		for ( final PrivateMessage message : messagesToShow ) {
			if ( ! message.isRead() && UserUtils.isUsersEqual( message.getToUser(), EnvironmentContext.getCurrentUser() ) ) {
				privateMessageService.markPrivateMessageAsRead( message.getId() );
				privateMessageService.markPrivateMessageAsRead( message.getOutPrivateMessageId() ); // Mark outgoing message as read by addressat
			}
		}
	}

	private List<UsersWhoCommunicatedWithUser> getUsersWhoCommunicatedWithUser( final List<PrivateMessage> receivedMessages, final List<PrivateMessage> sentMessages, final User withUser ) {

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
				return ! UserUtils.isUsersEqual( communicator.getWithUser(), withUser );
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
