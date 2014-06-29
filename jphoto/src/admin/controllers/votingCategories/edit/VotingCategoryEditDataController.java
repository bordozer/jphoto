package admin.controllers.votingCategories.edit;

import core.general.photo.PhotoVotingCategory;
import core.services.entry.VotingCategoryService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsAdminService;
import ui.services.validation.DataRequirementService;

import javax.validation.Valid;

@SessionAttributes( {"votingCategoryEditDataModel"} )
@Controller
@RequestMapping( UrlUtilsServiceImpl.VOTING_CATEGORIES_URL )
public class VotingCategoryEditDataController {

	public static final String VIEW = "admin/votingCategories/edit/VotingCategoryEditData";

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Autowired
	private VotingCategoryEditDataValidator votingCategoryEditDataValidator;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private DataRequirementService dataRequirementService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( votingCategoryEditDataValidator );
	}

	@ModelAttribute( "votingCategoryEditDataModel" )
	public VotingCategoryEditDataModel prepareModel() {
		final VotingCategoryEditDataModel model = new VotingCategoryEditDataModel();

		model.setDataRequirementService( dataRequirementService );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newVotingCategory( @ModelAttribute( "votingCategoryEditDataModel" ) VotingCategoryEditDataModel model ) {
		model.clear();

		model.setNew( true );
		model.setPageTitleData( breadcrumbsAdminService.getVotingCategoryNewBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{categoryId}/edit/" )
	public String editVotingCategory( final @PathVariable( "categoryId" ) int categoryId, final @ModelAttribute( "votingCategoryEditDataModel" ) VotingCategoryEditDataModel model ) {
		model.clear();

		final PhotoVotingCategory photoVotingCategory = votingCategoryService.load( categoryId );

		initModelFromObject( model, photoVotingCategory );

		model.setPageTitleData( breadcrumbsAdminService.getVotingCategoryEditDataBreadcrumbs( photoVotingCategory ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String saveVotingCategory( @Valid final @ModelAttribute( "votingCategoryEditDataModel" ) VotingCategoryEditDataModel model, final BindingResult result ) {
		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final PhotoVotingCategory photoVotingCategory = getObjectFromModel( model );

		if ( !votingCategoryService.save( photoVotingCategory ) ) {
			final Language language = EnvironmentContext.getLanguage();
			result.reject( translatorService.translate( "Saving data error", language ), translatorService.translate( "Error saving data to DB", language ) );
			return VIEW;
		}

		return String.format( "redirect:%s/%s/", urlUtilsService.getBaseAdminURL(), UrlUtilsServiceImpl.VOTING_CATEGORIES_URL );
	}

	private void initModelFromObject( final VotingCategoryEditDataModel model, final PhotoVotingCategory photoVotingCategory ) {
		model.setVotingCategoryId( photoVotingCategory.getId() );
		model.setVotingCategoryName( photoVotingCategory.getName() );
		model.setVotingCategoryDescription( photoVotingCategory.getDescription() );
	}

	private PhotoVotingCategory getObjectFromModel( final VotingCategoryEditDataModel model ) {
		final PhotoVotingCategory photoVotingCategory = new PhotoVotingCategory();
		photoVotingCategory.setId( model.getVotingCategoryId() );
		photoVotingCategory.setName( model.getVotingCategoryName() );
		photoVotingCategory.setDescription( model.getVotingCategoryDescription() );
		return photoVotingCategory;
	}

}
