package core.services.entry;

import core.dtos.AjaxResultDTO;
import core.dtos.PrivateMessageSendingDTO;
import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.log.LogHelper;
import core.services.dao.PrivateMessageDao;
import core.services.notification.NotificationService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import utils.TranslatorUtils;
import utils.UserUtils;

import java.util.List;

public class PrivateMessageServiceImpl implements PrivateMessageService {

	@Autowired
	private UserService userService;

	@Autowired
	private PrivateMessageDao privateMessageDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private NotificationService notificationService;

	private final LogHelper log = new LogHelper( PrivateMessageServiceImpl.class );

	@Override
	public boolean save( final PrivateMessage entry ) {
		return privateMessageDao.saveToDB( entry );
	}

	@Override
	public PrivateMessage load( final int messageId ) {
		return privateMessageDao.load( messageId );
	}

	@Override
	public boolean delete( final int entryId ) {
		return privateMessageDao.delete( entryId );
	}

	@Override
	public List<PrivateMessage> loadReceivedPrivateMessages( final int toUserId, final PrivateMessageType privateMessageType ) {
		return privateMessageDao.loadReceivedPrivateMessages( toUserId, privateMessageType );
	}

	@Override
	public List<PrivateMessage> loadSentPrivateMessages( final int fromUserId ) {
		return privateMessageDao.loadSentPrivateMessages( fromUserId );
	}

	@Override
	public int getNewReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType ) {
		return privateMessageDao.getNewReceivedPrivateMessagesCount( userId, privateMessageType );
	}

	@Override
	public int getReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType ) {
		return privateMessageDao.getReceivedPrivateMessagesCount( userId, privateMessageType );
	}

	@Override
	public int getSentPrivateMessagesCount( final int userId ) {
		return privateMessageDao.getSentPrivateMessagesCount( userId );
	}

	@Override
	public void markPrivateMessageAsRead( final PrivateMessage privateMessage ) {
		privateMessageDao.markPrivateMessageAsRead( privateMessage.getId() );
		privateMessageDao.markPrivateMessageAsRead( privateMessage.getOutPrivateMessageId() );
	}

	@Override
	public void markPrivateMessagesAsRead( final List<PrivateMessage> messages ) {
		privateMessageDao.markPrivateMessagesAsRead( messages );
	}

	@Override
	public AjaxResultDTO sendPrivateMessageAjax( final PrivateMessageSendingDTO messageDTO ) {

		if ( !UserUtils.isCurrentUserLoggedUser() ) {
			return AjaxResultDTO.failResult( TranslatorUtils.translate( "You are not logged in" ) );
		}

		final int fromUserId = messageDTO.getFromUserId();
		final User fromUser = userService.load( fromUserId );
		if ( fromUser == null ) {
			return AjaxResultDTO.failResult( TranslatorUtils.translate( "Member FROM not found" ) );
		}

		if ( !UserUtils.isUserEqualsToCurrentUser( fromUser ) ) {
			return AjaxResultDTO.failResult( TranslatorUtils.translate( "Attempt to send the message from another account. It seems you have changed your account after loading of this page, haven't you?" ) );
		}

		final int toUserId = messageDTO.getToUserId();
		final User toUser = userService.load( toUserId );
		if ( toUser == null ) {
			return AjaxResultDTO.failResult( TranslatorUtils.translate( "Member you are trying to send message not found" ) );
		}

		if ( UserUtils.isUsersEqual( fromUser, toUser ) ) {
			return AjaxResultDTO.failResult( TranslatorUtils.translate( "You can not send message to yourself" ) );
		}

		final String privateMessageText = messageDTO.getPrivateMessageText();
		if ( StringUtils.isEmpty( privateMessageText ) ) {
			return AjaxResultDTO.failResult( TranslatorUtils.translate( "Message text should not be empty" ) );
		}

		final PrivateMessage privateMessageOut = new PrivateMessage();
		privateMessageOut.setFromUser( fromUser );
		privateMessageOut.setToUser( toUser );
		privateMessageOut.setMessageText( privateMessageText );
		privateMessageOut.setPrivateMessageType( PrivateMessageType.USER_PRIVATE_MESSAGE_OUT );
		privateMessageOut.setCreationTime( dateUtilsService.getCurrentTime() );

		final boolean isSuccessfulOut = privateMessageDao.saveToDB( privateMessageOut );

		final AjaxResultDTO resultDTO = new AjaxResultDTO();
		resultDTO.setSuccessful( isSuccessfulOut );

		if ( !isSuccessfulOut ) {
			resultDTO.setMessage( TranslatorUtils.translate( "Error saving OUT message to DB" ) );

			return resultDTO;
		}

		final PrivateMessage privateMessageIn = new PrivateMessage( privateMessageOut );
		privateMessageIn.setPrivateMessageType( PrivateMessageType.USER_PRIVATE_MESSAGE_IN );
		privateMessageIn.setOutPrivateMessageId( privateMessageOut.getId() );

		final boolean isSuccessfulIn = privateMessageDao.saveToDB( privateMessageIn );

		resultDTO.setSuccessful( isSuccessfulIn );

		if ( !isSuccessfulIn ) {
			resultDTO.setMessage( TranslatorUtils.translate( "Error saving IN message to DB" ) );
			privateMessageDao.delete( privateMessageOut.getId() );
		}

		if ( resultDTO.isSuccessful() ) {
			new Thread( new Runnable() {
				@Override
				public void run() {
					notificationService.newPrivateMessage( privateMessageOut );
				}
			} ).start();
		}

		return resultDTO;
	}

	@Override
	public boolean sendPrivateMessage( final User fromUser, final User toUser, final PrivateMessageType messageType, final String privateMessageText ) {

		final PrivateMessage privateMessageOut = new PrivateMessage();
		privateMessageOut.setFromUser( fromUser );
		privateMessageOut.setToUser( toUser );
		privateMessageOut.setMessageText( privateMessageText );
		privateMessageOut.setPrivateMessageType( messageType );
		privateMessageOut.setCreationTime( dateUtilsService.getCurrentTime() );

		final boolean isSuccessfulOut = privateMessageDao.saveToDB( privateMessageOut );

		/*boolean isSuccessfulIn = true;
		if ( fromUser != null ) {
			final PrivateMessage privateMessageIn = new PrivateMessage( privateMessageOut );
			privateMessageIn.setPrivateMessageType( PrivateMessageType.USER_PRIVATE_MESSAGE_IN );
			privateMessageIn.setOutPrivateMessageId( privateMessageOut.getId() );

			isSuccessfulIn = privateMessageDao.saveToDB( privateMessageIn );
		}*/

		log.debug( String.format( "%s has sent to %s the text '%s'", fromUser, toUser, privateMessageText ) );

		return isSuccessfulOut;
	}

	@Override
	public boolean sendSystemNotificationMessage( final User toUser, final String privateMessageText ) {
		return sendPrivateMessage( null, toUser, PrivateMessageType.SYSTEM_NOTIFICATIONS, privateMessageText );
	}

	@Override
	public boolean sendActivityNotificationMessage( final User toUser, final String privateMessageText ) {
		return sendPrivateMessage( null, toUser, PrivateMessageType.ACTIVITY_NOTIFICATIONS, privateMessageText );
	}

	@Override
	public boolean sendAdminNotificationMessage( final User toUser, final String privateMessageText ) {
		return sendPrivateMessage( null, toUser, PrivateMessageType.ADMIN_NOTIFICATIONS, privateMessageText );
	}

	@Override
	public void delete( final List<Integer> ids ) {
		privateMessageDao.delete( ids );
	}

	@Override
	public void deleteAll( final int userId, final PrivateMessageType messageType ) {
		privateMessageDao.deleteAll( userId, messageType );
	}

	@Override
	public boolean exists( final int entryId ) {
		return privateMessageDao.exists( entryId );
	}

	@Override
	public boolean exists( final PrivateMessage entry ) {
		return privateMessageDao.exists( entry );
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return privateMessageDao.load( selectIdsQuery );
	}
}
