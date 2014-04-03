package ui.services.breadcrumbs;

import core.general.activity.ActivityType;
import core.services.system.Services;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;

import static ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

public class PageTitleServiceImpl implements PageTitleService {

	@Autowired
	private Services services;

	@Override
	public PageTitleData getActivityStreamBreadcrumbs( final ActivityType activityType ) {

		final TranslatableStringBreadcrumb activityStreamText = new TranslatableStringBreadcrumb( "Breadcrumbs: Activity stream", services );

		final String title = BreadcrumbsBuilder.pageTitle( activityStreamText, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( activityStreamText, services ).build();

		final BreadcrumbsBuilder breadcrumbs = portalPage( services );

		if ( activityType == null ) {
			breadcrumbs.add( activityStreamText ).build();
		} else {
			breadcrumbs.activityStream().translatableString( activityType.getName() );
		}

		return new PageTitleData( title, header, breadcrumbs.build() );
	}
}
