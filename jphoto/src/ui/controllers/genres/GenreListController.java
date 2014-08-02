package ui.controllers.genres;

import core.general.genre.Genre;
import core.services.entry.GenreService;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
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
	private CommonBreadcrumbsService commonBreadcrumbsService;

	@ModelAttribute( MODEL_NAME )
	public GenreListModel prepareModel() {

		final List<GenreListEntry> genreListEntries = newArrayList();

		final List<Genre> genres = genreService.loadAllSortedByNameForLanguage( EnvironmentContext.getLanguage() );
		for ( final Genre genre : genres ) {

			final GenreListEntry entry = new GenreListEntry( genre );
			entry.setGenreNameTranslated( translatorService.translateGenre( genre, EnvironmentContext.getLanguage() ) );

			genreListEntries.add( entry );
		}

		return new GenreListModel( genreListEntries );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showGenreList( final @ModelAttribute( MODEL_NAME ) GenreListModel model ) {

		model.setPageTitleData( commonBreadcrumbsService.genGenreListBreadcrumbs() );

		return VIEW;
	}

}
