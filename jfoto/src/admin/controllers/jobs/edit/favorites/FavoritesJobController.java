package admin.controllers.jobs.edit.favorites;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.controllers.jobs.edit.AbstractJobController;
import admin.jobs.entries.FavoritesJob;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.enums.FavoriteEntryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import utils.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes( FavoritesJobController.JOB_MODEL_NAME )
@Controller
@RequestMapping( "jobs/data/favorites" )
public class FavoritesJobController extends AbstractJobController {

	public static final String JOB_MODEL_NAME = "favoritesJobModel";
	private static final String START_VIEW = "admin/jobs/edit/favorites/Start";

	@Autowired
	private FavoritesJobValidator favoritesJobValidator;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( favoritesJobValidator );
	}

	@ModelAttribute( JOB_MODEL_NAME )
	public FavoritesJobModel prepareModel() {
		final FavoritesJobModel model = new FavoritesJobModel();

		model.setActionsQty( "10" );
		model.setPhotoQty( "100" );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( final @ModelAttribute( JOB_MODEL_NAME ) FavoritesJobModel model ) {
		return doShowForm( model, SavedJobType.FAVORITES_GENERATION );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public ModelAndView postForm( final @Valid @ModelAttribute( JOB_MODEL_NAME ) FavoritesJobModel model, final BindingResult result ) {
		return doPostForm( model, result );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public ModelAndView saveJob( final @Valid @ModelAttribute( JOB_MODEL_NAME ) FavoritesJobModel model, final BindingResult result ) {
		return doSaveJob( model, result );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/edit/" )
	public ModelAndView editEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) FavoritesJobModel model, final HttpServletRequest request ) {
		return processEditing( savedJobId, model, request );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/delete/" )
	public ModelAndView deleteEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) FavoritesJobModel model ) {
		return deleteAndReturnView( savedJobId, model );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/progress/{jobId}/" )
	public ModelAndView jobProgress( final @PathVariable( "jobId" ) int jobId, @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/stop/{jobId}/" )
	public ModelAndView stopJob( final @PathVariable( "jobId" ) int jobId, final @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		stopJobWithChildByUserDemand( jobId );

		return getProgressOrFinishView( model, jobId );
	}

	@Override
	protected void showFormCustomAction( final AbstractAdminJobModel model ) {
//		final FavoritesJobModel aModel = ( FavoritesJobModel ) model;
	}

	@Override
	protected void initJobFromModel( final AbstractAdminJobModel model ) {
		final FavoritesJob job = ( FavoritesJob ) model.getJob();
		final FavoritesJobModel aModel = ( FavoritesJobModel ) model;

		final int actionsQty = NumberUtils.convertToInt( aModel.getActionsQty() );
		job.setPhotoQtyLimit( NumberUtils.convertToInt( aModel.getPhotoQty() ) );

		final List<FavoriteEntryType> favoriteEntries = newArrayList();
		for ( final String favoriteEntryId : aModel.getFavoriteEntriesIds() ) {
			favoriteEntries.add( FavoriteEntryType.getById( NumberUtils.convertToInt( favoriteEntryId ) ) );
		}
		job.setFavoriteEntries( favoriteEntries );

		job.setTotalJopOperations( actionsQty );
	}

	@Override
	protected void initModelFromSavedJob( final AbstractAdminJobModel model, final int savedJobId ) {
		final FavoritesJobModel aModel = ( FavoritesJobModel ) model;

		final Map<SavedJobParameterKey,CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap( savedJobId );

		aModel.setActionsQty( savedJobParametersMap.get( SavedJobParameterKey.PARAM_ACTIONS_QTY ).getValue() );
		aModel.setPhotoQty( savedJobParametersMap.get( SavedJobParameterKey.PARAM_PHOTOS_QTY ).getValue() );
		aModel.setFavoriteEntriesIds( savedJobParametersMap.get( SavedJobParameterKey.PARAM_FAVORITE_ENTRIES ).getValueListString() );
	}

	@Override
	protected String getStartViewName() {
		return START_VIEW;
	}
}
