package ui.services.page.icons;

import admin.services.scheduler.ScheduledTasksExecutionService;
import core.enums.PrivateMessageType;
import core.general.user.User;
import core.services.entry.PrivateMessageService;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import org.quartz.SchedulerException;
import ui.context.EnvironmentContext;
import utils.UserUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TitleIconLoader {

	public static List<AbstractTitleIcon> getTitleIcons( final Services services, final ScheduledTasksExecutionService scheduledTasksExecutionService ) {
		final List<AbstractTitleIcon> result = newArrayList();

		addScheduledIcon( result, services, scheduledTasksExecutionService );

		addUnreadCommentsCountIcon( result, services );

		addUnreadPrivateMessagesIcon( result, services );

		addUntranslatedMessagesIcon( result, services );

		return result;
	}

	private static void addScheduledIcon( final List<AbstractTitleIcon> result, final Services services, final ScheduledTasksExecutionService scheduledTasksExecutionService ) {

		if ( ! services.getSecurityService().isSuperAdminUser( getCurrentUser() ) ) {
			return;
		}

		boolean running;
		try {
			running = scheduledTasksExecutionService.isRunning();
		} catch ( final SchedulerException e ) {
			running = false;
		}

		if ( !running ) {
			result.add( new SchedulerTitleIcon( services ) );
		}
	}

	private static void addUnreadCommentsCountIcon( final List<AbstractTitleIcon> result, final Services services ) {
		final int unreadCommentsCount = services.getPhotoCommentService().getUnreadCommentsQty( getCurrentUser().getId() );
		if ( unreadCommentsCount > 0 ) {
			result.add( new UnreadCommentsCountTitleIcon( unreadCommentsCount, services ) );
		}
	}

	private static void addUnreadPrivateMessagesIcon( final List<AbstractTitleIcon> result, final Services services ) {

		final PrivateMessageService privateMessageService = services.getPrivateMessageService();
		final int userId = getCurrentUser().getId();

		if ( UserUtils.isCurrentUserLoggedUser() ) {
			for ( final PrivateMessageType messageType : PrivateMessageType.values() ) {

				if ( messageType == PrivateMessageType.USER_PRIVATE_MESSAGE_OUT ) {
					continue;
				}

				final int messagesCount = privateMessageService.getNewReceivedPrivateMessagesCount( userId, messageType );

				if ( messagesCount > 0 ) {
					result.add( new UnreadPrivateMessagesTitleIcon( messageType, messagesCount, services ) );
				}
			}
		}
	}

	private static void addUntranslatedMessagesIcon( final List<AbstractTitleIcon> result, final Services services ) {
		if ( services.getSecurityService().isSuperAdminUser( getCurrentUser() ) ) {
			final int untranslatedMessagesCount = getTranslatorService( services ).getUntranslatedMap().size();

			if ( untranslatedMessagesCount == 0 ) {
				return;
			}

			result.add( new UntranslatedMessagesTitleIcon( untranslatedMessagesCount, services ) );
		}
	}

	private static TranslatorService getTranslatorService( final Services services ) {
		return services.getTranslatorService();
	}

	private static User getCurrentUser() {
		return EnvironmentContext.getCurrentUser();
	}
}
