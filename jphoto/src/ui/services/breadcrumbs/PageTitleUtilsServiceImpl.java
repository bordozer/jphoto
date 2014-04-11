package ui.services.breadcrumbs;

import ui.context.EnvironmentContext;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PageTitleUtilsServiceImpl implements PageTitleUtilsService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Override
	public String getDataString( String root, String... strings ) {

		final StringBuilder builder = new StringBuilder( root ).append( " / " );

		int i = 0;

		for ( String string : strings ) {
			builder.append( string );
			if ( i++ < strings.length - 1 ) {
				builder.append( " / " );
			}
		}

		return builder.toString();
	}

	@Override
	public String getTitleDataString( final String... strings ) {
		return getDataString( systemVarsService.getProjectName(), strings );
	}

	@Override
	public String getBreadcrumbsDataString( final String... strings ) {
		return getDataString( entityLinkUtilsService.getPortalPageLink( EnvironmentContext.getLanguage() ), strings );
	}

	@Override
	public String getBreadcrumbsDataString( final List<String> list ) {
		final String[] elements = new String[ list.size() ];

		int i = 0;
		for ( final String element : list ) {
			elements[ i++ ] = element;
		}

		return getDataString( entityLinkUtilsService.getPortalPageLink( EnvironmentContext.getLanguage() ), elements );
	}
}
