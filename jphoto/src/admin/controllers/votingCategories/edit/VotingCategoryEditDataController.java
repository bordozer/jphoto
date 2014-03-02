package admin.controllers.votingCategories.edit;

import core.general.photo.PhotoVotingCategory;
import core.services.entry.VotingCategoryService;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import utils.TranslatorUtils;

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
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( votingCategoryEditDataValidator );
	}

	@ModelAttribute( "votingCategoryEditDataModel" )
	public VotingCategoryEditDataModel prepareModel() {
		return new VotingCategoryEditDataModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newVotingCategory( @ModelAttribute( "votingCategoryEditDataModel" ) VotingCategoryEditDataModel model ) {
		model.clear();

		model.setPhotoVotingCategory( new PhotoVotingCategory() );
		model.setNew( true );
		model.setPageTitleData( pageTitleAdminUtilsService.getVotingCategoryNewData() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{categoryId}/edit/" )
	public String editVotingCategory( final @PathVariable( "categoryId" ) int categoryId, final @ModelAttribute( "votingCategoryEditDataModel" ) VotingCategoryEditDataModel model ) {
		model.clear();

		final PhotoVotingCategory photoVotingCategory = votingCategoryService.load( categoryId );
		model.setPhotoVotingCategory( photoVotingCategory );
		model.setPageTitleData( pageTitleAdminUtilsService.getVotingCategoryEditData( photoVotingCategory ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String saveVotingCategory( @Valid final @ModelAttribute( "votingCategoryEditDataModel" ) VotingCategoryEditDataModel model, final BindingResult result ) {
		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		if ( !votingCategoryService.save( model.getPhotoVotingCategory() ) ) {
			result.reject( TranslatorUtils.translate( "Saving data error" ), TranslatorUtils.translate( "Error saving data to DB" ) );
			return VIEW;
		}

		return String.format( "redirect:/%s/%s/", systemVarsService.getAdminPrefix(), UrlUtilsServiceImpl.VOTING_CATEGORIES_URL );
	}

}
