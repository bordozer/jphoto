package core.services.pageTitle;

import core.general.genre.Genre;
import elements.PageTitleData;

public interface PageTitleGenreUtilsService {

	PageTitleData getGenreListData();

	PageTitleData getGenreNewData();

	PageTitleData getGenreEditData( Genre genre );

	String getGenreRootTranslated();
}
