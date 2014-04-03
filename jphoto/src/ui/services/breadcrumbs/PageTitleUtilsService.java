package ui.services.breadcrumbs;

import java.util.List;

public interface PageTitleUtilsService {

	String getDataString( String root, String... strings );

	String getTitleDataString( String... strings );

	String getBreadcrumbsDataString( String... strings );

	String getBreadcrumbsDataString( List<String> list );
}
