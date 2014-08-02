package ui.controllers.genres;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoPreviewWrapper;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.PhotoUIService;
import ui.services.breadcrumbs.CommonBreadcrumbsService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( value = "genres" )
public class GenreListController {

	private static final String MODEL_NAME = "genreListModel";
	private static final String VIEW = "genres/GenreList";

	@Autowired
	private GenreService genreService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoUIService photoUIService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private CommonBreadcrumbsService commonBreadcrumbsService;

	@ModelAttribute( MODEL_NAME )
	public GenreListModel prepareModel() {

		final List<GenreListEntry> genreListEntries = newArrayList();

		final List<Genre> genres = genreService.loadAllSortedByNameForLanguage( getLanguage() );
		for ( final Genre genre : genres ) {

			final GenreListEntry entry = new GenreListEntry( genre );
			entry.setGenreNameTranslated( translatorService.translateGenre( genre, getLanguage() ) );

			final int lastGenrePhotoId = photoService.getLastGenrePhotoId( genre.getId() );
			if ( lastGenrePhotoId == 0 ) {
				continue;
			}

			final Photo photo = photoService.load( lastGenrePhotoId );
			final PhotoPreviewWrapper previewWrapper = photoUIService.getPhotoPreviewWrapper( photo, EnvironmentContext.getCurrentUser() );
			entry.setPhotoPreviewWrapper( previewWrapper );
			entry.setPhotosCount( photoService.getPhotoQtyByGenre( genre.getId() ) );

			final User photoAuthor = userService.load( photo.getUserId() );
			entry.setGenreIconTitle( translatorService.translate( "Genre list: LAst uploaded photo. Author $1", getLanguage(), photoAuthor.getNameEscaped() ) );

			entry.setPhotosByGenreURL( urlUtilsService.getPhotosByGenreLink( genre.getId() ) );

			genreListEntries.add( entry );
		}

		return new GenreListModel( genreListEntries );
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showGenreList( final @ModelAttribute( MODEL_NAME ) GenreListModel model ) {

		model.setPageTitleData( commonBreadcrumbsService.genGenreListBreadcrumbs() );

		return VIEW;
	}
}
