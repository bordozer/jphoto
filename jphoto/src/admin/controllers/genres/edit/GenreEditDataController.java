package admin.controllers.genres.edit;

import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.services.entry.GenreService;
import core.services.entry.VotingCategoryService;
import core.services.translator.TranslatorService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsAdminPhotoCategoriesService;
import ui.services.validation.DataRequirementService;

import javax.validation.Valid;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes( { "genreEditDataModel" } )
@Controller
@RequestMapping( UrlUtilsServiceImpl.GENRES_URL )
public class GenreEditDataController {

	public static final String VIEW = "admin/genres/edit/GenreEditData";

	public static final String MODEL_NAME = "genreEditDataModel";

	@Autowired
	private GenreService genreService;

	@Autowired
	private GenreEditDataValidator genreEditDataValidator;

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private BreadcrumbsAdminPhotoCategoriesService breadcrumbsAdminPhotoCategoriesService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private DataRequirementService dataRequirementService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( genreEditDataValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public GenreEditDataModel prepareModel() {
		final GenreEditDataModel model = new GenreEditDataModel();

		model.setDataRequirementService( dataRequirementService );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newGenre( final @ModelAttribute( MODEL_NAME ) GenreEditDataModel model ) {
		model.clear();

		model.setNew( true );
		model.setPageTitleData( breadcrumbsAdminPhotoCategoriesService.getGenreNewBreadcrumbs() );
		model.setPhotoVotingCategories( votingCategoryService.loadAll() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{genreId}/edit/" )
	public String editGenre( final @PathVariable( "genreId" ) int genreId, final @ModelAttribute( MODEL_NAME ) GenreEditDataModel model ) {
		model.clear();

		final Genre genre = new Genre( genreService.load( genreId ) );
		model.setGenreId( genre.getId() );
		model.setGenreName( genre.getName() );
		model.setDescription( genre.getDescription() );
		model.setCanContainNudeContent( genre.isCanContainNudeContent() );
		model.setContainsNudeContent( genre.isContainsNudeContent() );
		model.setMinMarksForBest( genre.getMinMarksForBest() );
		model.setPhotoVotingCategories( votingCategoryService.loadAll() );

		final List<String> allowedVotingCategoryIDs = newArrayList();
		for ( PhotoVotingCategory photoVotingCategory : genre.getPhotoVotingCategories() ) {
			allowedVotingCategoryIDs.add( String.valueOf( photoVotingCategory.getId() ) );
		}
		model.setAllowedVotingCategoryIDs( allowedVotingCategoryIDs );

		model.setPageTitleData( breadcrumbsAdminPhotoCategoriesService.getGenreDataEditBreadcrumbs( genre ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String saveGenre( @Valid final @ModelAttribute( MODEL_NAME ) GenreEditDataModel model, final BindingResult result ) {
		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final Genre genre = new Genre();
		genre.setId( model.getGenreId() );
		genre.setName( model.getGenreName() );
		genre.setDescription( model.getDescription() );
		genre.setCanContainNudeContent( model.isCanContainNudeContent() );
		genre.setContainsNudeContent( model.isContainsNudeContent() );
		genre.setMinMarksForBest( model.getMinMarksForBest() );

		genre.setPhotoVotingCategories( model.getAllowedVotingCategories() );

		if ( ! genreService.save( genre ) ) {
			result.reject( translatorService.translate( "Saving data error", EnvironmentContext.getLanguage() ), translatorService.translate( "Error saving data to DB", EnvironmentContext.getLanguage() ) );
			return VIEW;
		}

		return String.format( "redirect:/%s/%s/", systemVarsService.getAdminPrefix(), UrlUtilsServiceImpl.GENRES_URL );
	}

}
