package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.general.activity.ActivityType;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;

public class PageTitleServiceImpl implements PageTitleService {

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private BreadcrumbsPhotoGalleryService breadcrumbsPhotoGalleryService;

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public PageTitleData photoCardTitle( final Photo photo, final User accessor, final String title ) {
		final User photoAuthor = userService.load( photo.getUserId() );
		final Genre genre = genreService.load( photo.getGenreId() );

		return securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ? breadcrumbsPhotoGalleryService.getPhotoCardForHiddenAuthor( photo, genre, title ) : breadcrumbsPhotoGalleryService.getPhotoCardData( photo, photoAuthor, genre, title );
	}

	@Override
	public PageTitleData userPhotoVotingData( final Photo photo, final User accessor ) {
		final User photoAuthor = userService.load( photo.getUserId() );
		final Genre genre = genreService.load( photo.getGenreId() );

		return securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ? breadcrumbsPhotoGalleryService.getPhotoTitleForHiddenAuthor( photo, genre, "Votes" ) : breadcrumbsPhotoGalleryService.getUserPhotoVotingData( photoAuthor, photo, genre );
	}

	@Override
	public PageTitleData userPhotoPreviewsData( final Photo photo, final User accessor ) {
		final User photoAuthor = userService.load( photo.getUserId() );
		final Genre genre = genreService.load( photo.getGenreId() );

		return securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ? breadcrumbsPhotoGalleryService.getPhotoTitleForHiddenAuthor( photo, genre, "Previews" ) : breadcrumbsPhotoGalleryService.getUserPhotoPreviewsData( photoAuthor, photo, genre );
	}

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
