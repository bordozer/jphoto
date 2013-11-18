package core.services.pageTitle;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.user.UserService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import utils.TranslatorUtils;

import java.util.List;

public class PageTitleServiceImpl implements PageTitleService {

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PageTitlePhotoUtilsService pageTitlePhotoUtilsService;

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;

	@Override
	public PageTitleData photoCardTitle( final Photo photo, final User accessor ) {
		final User photoAuthor = userService.load( photo.getUserId() );
		final Genre genre = genreService.load( photo.getGenreId() );

		return photoService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ? pageTitlePhotoUtilsService.getPhotoCardForHiddenAuthor( photo, genre ) : pageTitlePhotoUtilsService.getPhotoCardData( photo, photoAuthor, genre );
	}

	@Override
	public PageTitleData userPhotoVotingData( final Photo photo, final User accessor ) {
		final User photoAuthor = userService.load( photo.getUserId() );
		final Genre genre = genreService.load( photo.getGenreId() );

		return photoService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ? pageTitlePhotoUtilsService.getPhotoTitleForHiddenAuthor( photo, genre, "Votes" ) : pageTitlePhotoUtilsService.getUserPhotoVotingData( photoAuthor, photo, genre );
	}

	@Override
	public PageTitleData userPhotoPreviewsData( final Photo photo, final User accessor ) {
		final User photoAuthor = userService.load( photo.getUserId() );
		final Genre genre = genreService.load( photo.getGenreId() );

		return photoService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ? pageTitlePhotoUtilsService.getPhotoTitleForHiddenAuthor( photo, genre, "Previews" ) : pageTitlePhotoUtilsService.getUserPhotoPreviewsData( photoAuthor, photo, genre );
	}

	@Override
	public PageTitleData getActivityStreamData() {
		final String rootTranslated = TranslatorUtils.translate( "Activity Stream" );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( rootTranslated );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}
}
