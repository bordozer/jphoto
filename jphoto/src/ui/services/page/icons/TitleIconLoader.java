package ui.services.page.icons;

import core.general.user.User;
import core.services.photo.PhotoCommentService;
import core.services.system.Services;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import ui.context.EnvironmentContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TitleIconLoader {

	public static List<AbstractTitleIcon> getTitleIcons( final Services services ) {
		final List<AbstractTitleIcon> result = newArrayList();

		addScheduledIcon( result, services );

		addUnreadCommentsCountIcon( result, services );

		return result;
	}

	private static void addScheduledIcon( final List<AbstractTitleIcon> result, final Services services ) {
		boolean running;
		try {
			running = services.getScheduledTasksExecutionService().isRunning();
		} catch ( final SchedulerException e ) {
			running = false;
		}

		if ( ! running ) {
			result.add( new SchedulerTitleIcon( services ) );
		}
	}

	private static void addUnreadCommentsCountIcon( final List<AbstractTitleIcon> result, final Services services ) {

		final PhotoCommentService photoCommentService = services.getPhotoCommentService();

		final int unreadCommentsCount = photoCommentService.getUnreadCommentsQty( getCurrentUser().getId() );
		if ( unreadCommentsCount > 0 ) {
			result.add( new UnreadCommentsCountTitleIcon( unreadCommentsCount, services ) );
			/*final String unreadCommentsText = String.format( "<a href='%1$s' title=\"%2$s\"><img src=\"%3$s/icons16/newComments16.png\"> +%4$s</a>"
				, urlUtilsService.getReceivedUnreadComments( currentUser.getId() )
				, translatorService.translate( "You have $1 new comment(s)", language, String.valueOf( unreadCommentsCount ) )
				, urlUtilsService.getSiteImagesPath()
				, unreadCommentsCount
			);*/
		}
	}

	private static User getCurrentUser() {
		return EnvironmentContext.getCurrentUser();
	}
}
