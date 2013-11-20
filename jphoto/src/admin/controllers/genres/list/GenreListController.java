package admin.controllers.genres.list;

import core.general.genre.Genre;
import core.general.configuration.ConfigurationKey;
import core.services.system.ConfigurationService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import utils.TranslatorUtils;
import core.services.utils.UrlUtilsServiceImpl;
import core.services.pageTitle.PageTitleGenreUtilsService;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@SessionAttributes( { "genreListModel" } )
@Controller
@RequestMapping( UrlUtilsServiceImpl.GENRES_URL )
public class GenreListController {

	public static final String VIEW = "/admin/genres/list/GenreList";

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private PageTitleGenreUtilsService pageTitleGenreUtilsService;

	@ModelAttribute( "genreListModel" )
	public GenreListModel prepareModel() {
		return new GenreListModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showGenreList( final @ModelAttribute( "genreListModel" ) GenreListModel model ) {
		model.clear();

		model.setSystemMinMarksToBeInTheBestPhotoOfGenre( configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO ) );

		final List<Genre> genres = genreService.loadAll();
		model.setGenreList( genres );

		final Map<Integer, Integer> photosByGenreMap = newHashMap();
		for ( Genre genre : genres ) {
			photosByGenreMap.put( genre.getId(), photoService.getPhotoQtyByGenre( genre.getId() ) );
		}
		model.setPhotosByGenreMap( photosByGenreMap );

		/*final Map<Integer, List<PhotoVotingCategory>> genrePhotoVotingCategoriesMap = newHashMap();
		for ( final Genre genre : genres ) {
			final List<PhotoVotingCategory> list = newArrayList();

			genrePhotoVotingCategoriesMap.put( genre, list );
		}
		model.setGenrePhotoVotingCategoriesMap( genrePhotoVotingCategoriesMap );*/

		model.setPageTitleData( pageTitleGenreUtilsService.getGenreListData() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{genreId}/delete/" )
	public String deleteGenre( final @PathVariable( "genreId" ) int genreId, final @ModelAttribute( "genreListModel" ) GenreListModel model ) {
		final boolean result = genreService.delete( genreId );

		if ( ! result ) {
			BindingResult bindingResult = new BeanPropertyBindingResult( model, "genreListModel" );
			bindingResult.reject( TranslatorUtils.translate( "Registration error" ), TranslatorUtils.translate( "Deletion error." ) );
			model.setBindingResult( bindingResult );
		}

		return String.format( "redirect:/%s/%s/", systemVarsService.getAdminPrefix(), UrlUtilsServiceImpl.GENRES_URL );
	}
}
