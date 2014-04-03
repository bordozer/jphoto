package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.general.activity.ActivityType;
import core.services.translator.TranslatorService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;

public class PageTitleServiceImpl implements PageTitleService {

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public PageTitleData getActivityStreamData( final ActivityType activityType ) {
		final String rootTranslated = translatorService.translate( "Activity stream", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated );

		final String breadcrumbs;
		if ( activityType != null ) {
			breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getActivityStreamRootLink( EnvironmentContext.getLanguage() ), activityType.getNameTranslated() );
		} else {
			breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( rootTranslated );
		}

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}
}
