package ui.services.breadcrumbs;

import core.services.system.Services;
import org.springframework.beans.factory.annotation.Autowired;
import ui.activity.ActivityType;
import ui.elements.PageTitleData;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;

import static ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

public class CommonBreadcrumbsServiceImpl implements CommonBreadcrumbsService {

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
