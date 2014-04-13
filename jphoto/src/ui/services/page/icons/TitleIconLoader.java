package ui.services.page.icons;

import core.services.system.Services;
import org.quartz.SchedulerException;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TitleIconLoader {

	public static List<AbstractTitleIcon> getTitleIcons( final Services services ) {
		final List<AbstractTitleIcon> result = newArrayList();

		addScheduledIcon( result, services );

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
}
