package admin.controllers.votingCategories.list;

import core.context.EnvironmentContext;
import core.general.photo.PhotoVotingCategory;
import core.services.entry.VotingCategoryService;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.translator.TranslatorService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SessionAttributes( { "votingCategoryListModel" } )
@Controller
@RequestMapping( UrlUtilsServiceImpl.VOTING_CATEGORIES_URL )
public class VotingCategoryListController {

	public static final String VIEW = "/admin/votingCategories/list/VotingCategoryList";

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@ModelAttribute( "votingCategoryListModel" )
	public VotingCategoryListModel prepareModel() {
		return new VotingCategoryListModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showVotingCategoryList( final @ModelAttribute( "votingCategoryListModel" ) VotingCategoryListModel model ) {
		model.clear();

		final List<PhotoVotingCategory> list = votingCategoryService.loadAll();
		model.setPhotoVotingCategories( list );
		model.setPageTitleData( pageTitleAdminUtilsService.getVotingCategoryList() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{votingCategoryId}/delete/" )
	public String deleteVotingCategory( @PathVariable( "votingCategoryId" ) int votingCategoryId, @ModelAttribute( "votingCategoryListModel" ) VotingCategoryListModel model ) {
		final boolean result = votingCategoryService.delete( votingCategoryId );

		if ( ! result ) {
			BindingResult bindingResult = new BeanPropertyBindingResult( model, "votingCategoryListModel" );
			bindingResult.reject( translatorService.translate( "Registration error", EnvironmentContext.getLanguage() ), translatorService.translate( "Deletion error.", EnvironmentContext.getLanguage() ) );
			model.setBindingResult( bindingResult );
		}

		return String.format( "redirect:/%s/%s/", systemVarsService.getAdminPrefix(), UrlUtilsServiceImpl.VOTING_CATEGORIES_URL );
	}
}
