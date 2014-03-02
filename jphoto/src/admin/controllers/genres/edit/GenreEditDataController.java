package admin.controllers.genres.edit;

import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.services.entry.GenreService;
import core.services.entry.VotingCategoryService;
import core.services.pageTitle.PageTitleGenreUtilsService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import utils.TranslatorUtils;

import javax.validation.Valid;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes( { "genreEditDataModel" } )
@Controller
@RequestMapping( UrlUtilsServiceImpl.GENRES_URL )
public class GenreEditDataController {

	public static final String VIEW = "admin/genres/edit/GenreEditData";

	@Autowired
	private GenreService genreService;

	@Autowired
	private GenreEditDataValidator genreEditDataValidator;

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private PageTitleGenreUtilsService pageTitleGenreUtilsService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( genreEditDataValidator );
	}

	@ModelAttribute( "genreEditDataModel" )
	public GenreEditDataModel prepareModel() {
		return new GenreEditDataModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newGenre( @ModelAttribute( "genreEditDataModel" ) GenreEditDataModel model ) {
		model.clear();

		model.setGenre( new Genre() );
		model.setNew( true );
		model.setPageTitleData( pageTitleGenreUtilsService.getGenreNewData() );
		model.setPhotoVotingCategories( votingCategoryService.loadAll() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{genreId}/edit/" )
	public String editGenre( @PathVariable( "genreId" ) int genreId, @ModelAttribute( "genreEditDataModel" ) GenreEditDataModel model ) {
		model.clear();

		final Genre genre = new Genre( genreService.load( genreId ) );
		model.setGenre( genre );
		model.setPhotoVotingCategories( votingCategoryService.loadAll() );

		final List<String> allowedVotingCategoryIDs = newArrayList();
		for ( PhotoVotingCategory photoVotingCategory : genre.getPhotoVotingCategories() ) {
			allowedVotingCategoryIDs.add( String.valueOf( photoVotingCategory.getId() ) );
		}
		model.setAllowedVotingCategoryIDs( allowedVotingCategoryIDs );

		model.setPageTitleData( pageTitleGenreUtilsService.getGenreEditData( genre ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String saveGenre( @Valid @ModelAttribute( "genreEditDataModel" ) GenreEditDataModel model, final BindingResult result ) {
		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final Genre genre = model.getGenre();
		genre.setPhotoVotingCategories( model.getAllowedVotingCategories() );

		if ( ! genreService.save( genre ) ) {
			result.reject( TranslatorUtils.translate( "Saving data error" ), TranslatorUtils.translate( "Error saving data to DB" ) );
			return VIEW;
		}

		return String.format( "redirect:/%s/%s/", systemVarsService.getAdminPrefix(), UrlUtilsServiceImpl.GENRES_URL );
	}

}
