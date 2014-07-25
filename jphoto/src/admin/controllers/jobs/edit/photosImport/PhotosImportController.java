package admin.controllers.jobs.edit.photosImport;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.controllers.jobs.edit.DateRangableController;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.FileSystemImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.RemotePhotoSiteCategoryToGenreMapping;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightRemoteContentHelper;
import admin.jobs.entries.AbstractJob;
import admin.jobs.entries.PhotosImportJob;
import admin.jobs.enums.DateRangeType;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.JobDateRange;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import core.enums.SavedJobParameterKey;
import core.enums.UserGender;
import core.exceptions.BaseRuntimeException;
import core.general.base.CommonProperty;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.user.UserMembershipType;
import core.services.entry.GenreService;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ui.context.EnvironmentContext;
import ui.translatable.GenericTranslatableList;
import utils.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes( {PhotosImportController.JOB_MODEL_NAME} )
@Controller
@RequestMapping( "jobs/data/photosImport" )
public class PhotosImportController extends DateRangableController {

	public static final String JOB_MODEL_NAME = "photosImportModel";

	private static final String START_VIEW = "admin/jobs/edit/photosImport/Start";

	@Autowired
	private PhotosImportValidator photosImportValidator;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private ConfigurationService configurationService;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( photosImportValidator );
	}

	@ModelAttribute( JOB_MODEL_NAME )
	public PhotosImportModel initModel() {
		final PhotosImportModel model = new PhotosImportModel();

		final PhotosImportSource importSource = PhotosImportSource.PHOTOSIGHT;
		model.setImportSource( importSource );
		model.setImportSourceId( importSource.getId() );

		prepareDateRange( model );

		model.setUserMembershipTypeTranslatableList( GenericTranslatableList.userMembershipTypeTranslatableList( EnvironmentContext.getLanguage(), translatorService ) );
		model.setUserGenderTranslatableList( GenericTranslatableList.userGenderTranslatableList( EnvironmentContext.getLanguage(), translatorService ) );
		model.setPhotosImportSourceTranslatableList( GenericTranslatableList.photosImportSourceTranslatableList( EnvironmentContext.getLanguage(), translatorService ) );

		return model;
	}
	
	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( final @ModelAttribute( JOB_MODEL_NAME ) PhotosImportModel model ) {
		return doShowForm( model, SavedJobType.PHOTOS_IMPORT );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public ModelAndView postForm( final @Valid @ModelAttribute( JOB_MODEL_NAME ) PhotosImportModel model, final BindingResult result ) {
		return doPostForm( model, result );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public ModelAndView saveJob( final @Valid @ModelAttribute( JOB_MODEL_NAME ) PhotosImportModel model, final BindingResult result ) {
		return doSaveJob( model, result );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/edit/" )
	public ModelAndView editEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) PhotosImportModel model, final HttpServletRequest request ) {
		return processEditing( savedJobId, model, request );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/delete/" )
	public ModelAndView deleteEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) PhotosImportModel model ) {
		return deleteAndReturnView( savedJobId, model );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/progress/{jobId}/" )
	public ModelAndView showProgress( final @PathVariable( "jobId" ) int jobId, @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/stop/{jobId}/" )
	public ModelAndView stopJob( final @PathVariable( "jobId" ) int jobId, final @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		stopJobWithChildByUserDemand( jobId );
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/finish/" )
	public ModelAndView finishJob( @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getFinishView( model );
	}

	@Override
	protected void showFormCustomAction( final AbstractAdminJobModel model ) {

		final boolean nudeContentByDefault = configurationService.getBoolean( ConfigurationKey.ADMIN_PHOTOSIGHT_IMPORT_JOB_IMPORT_NUDE_CONTENT );

		final PhotosImportModel aModel = ( PhotosImportModel ) model;

		aModel.setImportComments( true );
		aModel.setBreakImportIfAlreadyImportedPhotoFound( true );

		final List<RemotePhotoSiteCategory> categoryList = newArrayList( Arrays.asList( RemotePhotoSiteCategory.values() ) );

		CollectionUtils.filter( categoryList, new Predicate<RemotePhotoSiteCategory>() {
			@Override
			public boolean evaluate( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
				return nudeContentByDefault || ! getGenreByPhotosightCategory( remotePhotoSiteCategory ).isCanContainNudeContent();
			}
		} );

		aModel.setPhotosightCategories( Lists.transform( categoryList, new Function<RemotePhotoSiteCategory, String>() {
			@Override
			public String apply( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
				return String.valueOf( remotePhotoSiteCategory.getId() );
			}
		} ) );

		aModel.setPhotosightCategoryWrappers( getPhotosightCategoriesCheckboxes() );
		aModel.setPhotosightImport_importNudeContentByDefault( nudeContentByDefault );
	}

	private Genre getGenreByPhotosightCategory( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		final List<RemotePhotoSiteCategoryToGenreMapping> genreMapping = RemotePhotoSiteCategoryToGenreMapping.getRemotePhotoSiteCategoryToGenreMapping();

		for ( final RemotePhotoSiteCategoryToGenreMapping entry : genreMapping ) {
			if ( entry.getRemotePhotoSiteCategory() == remotePhotoSiteCategory ) {
				final GenreDiscEntry genreDiscEntry = entry.getGenreDiscEntry();
				return genreService.loadIdByName( genreDiscEntry.getName() );
			}
		}

		return null;
	}

	private List<PhotosightCategoryWrapper> getPhotosightCategoriesCheckboxes() {
		final List<RemotePhotoSiteCategory> photosightCategoriesSorted = Arrays.asList( RemotePhotoSiteCategory.values() );
		Collections.sort( photosightCategoriesSorted, new Comparator<RemotePhotoSiteCategory>() {
			@Override
			public int compare( final RemotePhotoSiteCategory category1, final RemotePhotoSiteCategory category2 ) {
				return category1.name().compareTo( category2.name() );
			}
		} );

		final List<PhotosightCategoryWrapper> photosightCategoryWrappers = newArrayList();
		for ( final RemotePhotoSiteCategory remotePhotoSiteCategory : photosightCategoriesSorted ) {

			final PhotosightCategoryWrapper categoryWrapper = new PhotosightCategoryWrapper( remotePhotoSiteCategory );

			final Genre genre = getGenreByPhotosightCategory( remotePhotoSiteCategory );
			if ( genre.isCanContainNudeContent() ) {
				categoryWrapper.addCssClass( "photosight-category-nude" );
			} else {
				categoryWrapper.addCssClass( "photosight-category-no-nude" );
			}

			photosightCategoryWrappers.add( categoryWrapper );
		}

		return photosightCategoryWrappers;
	}

	@Override
	protected void initJobFromModel( final AbstractAdminJobModel model ) {

		final PhotosImportJob job = ( PhotosImportJob ) model.getJob();
		final PhotosImportModel aModel = ( PhotosImportModel ) model;

		final PhotosImportSource importSource = aModel.getImportSource();
		job.setImportSource( importSource );

		final AbstractImportParameters importParameters;

		switch ( importSource ) {
			case FILE_SYSTEM:
				initDateRangeJob( aModel, job );

				final int photoQtyLimit = NumberUtils.convertToInt( aModel.getPhotoQtyLimit() );
				final String pictureDir = aModel.getPictureDir();
				final boolean deletePictureAfterImport = aModel.isDeletePictureFromDiskAfterImport();
				final int assignAllGeneratedPhotosToUserId = aModel.getAssignAllGeneratedPhotosToUserId();

				final Date dateFrom = dateUtilsService.parseDate( aModel.getDateFrom() );
				final Date dateTo = dateUtilsService.parseDate( aModel.getDateTo() );
				final DateRangeType dateRangeType = DateRangeType.getById( NumberUtils.convertToInt( aModel.getDateRangeTypeId() ) );
				final int timePeriod = NumberUtils.convertToInt( aModel.getTimePeriod() );
				final JobDateRange jobDateRange = new JobDateRange( dateRangeType, dateFrom, dateTo, timePeriod, dateUtilsService );

				importParameters = new FileSystemImportParameters( pictureDir, photoQtyLimit, deletePictureAfterImport, assignAllGeneratedPhotosToUserId, jobDateRange, EnvironmentContext.getLanguage() );

				job.setTotalJopOperations( photoQtyLimit > 0 ? photoQtyLimit : AbstractJob.OPERATION_COUNT_UNKNOWN );
				break;
			case PHOTOSIGHT:
				final List<String> photosightUserIds = newArrayList();
				final String photosightUserIdsText = aModel.getPhotosightUserId();
				final String[] ids = photosightUserIdsText.split( "," );
				for ( final String idTxt : ids ) {
					photosightUserIds.add( idTxt.trim() );
				}

				final String userName = aModel.getUserName();
				final UserGender userGender = UserGender.getById( NumberUtils.convertToInt( aModel.getUserGenderId() ) );
				final UserMembershipType membershipType = UserMembershipType.getById( NumberUtils.convertToInt( aModel.getUserMembershipId() ) );
				final boolean importComments = aModel.isImportComments();
				final int delayBetweenRequests = NumberUtils.convertToInt( aModel.getDelayBetweenRequest() );
				final int pageQty = NumberUtils.convertToInt( aModel.getPageQty() );

				final List<String> photosightCategories = aModel.getPhotosightCategories();
				final List<RemotePhotoSiteCategory> categoryList = Lists.transform( photosightCategories, new Function<String, RemotePhotoSiteCategory>() {
					@Override
					public RemotePhotoSiteCategory apply( final String id ) {
						return RemotePhotoSiteCategory.getById( NumberUtils.convertToInt( id ) );
					}
				} );
				job.setPhotosightCategories( categoryList );

				importParameters = new RemoteSitePhotosImportParameters( photosightUserIds, userGender, membershipType, importComments, delayBetweenRequests
					, pageQty, EnvironmentContext.getCurrentUser().getLanguage(), aModel.isBreakImportIfAlreadyImportedPhotoFound(), categoryList, new PhotosightRemoteContentHelper() );

				job.setTotalJopOperations( pageQty > 0 ? pageQty : AbstractJob.OPERATION_COUNT_UNKNOWN );
				break;
			default:
				throw new BaseRuntimeException( String.format( "Unsupported PhotoImportSource: %s", importSource ) );
		}

		job.setImportParameters( importParameters );
	}

	@Override
	protected void initModelFromSavedJob( final AbstractAdminJobModel model, final int savedJobId ) {
		final PhotosImportModel aModel = ( PhotosImportModel ) model;

		final Map<SavedJobParameterKey,CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap( savedJobId );

		final PhotosImportSource importSource = PhotosImportSource.getById( savedJobParametersMap.get( SavedJobParameterKey.PHOTOS_IMPORT_SOURCE ).getValueInt() );

		aModel.setImportSource( importSource );
		aModel.setImportSourceId( importSource.getId() );

		switch ( importSource ) {
			case FILE_SYSTEM:
				final String pictureDir = savedJobParametersMap.get( SavedJobParameterKey.PARAM_PICTURES_DIR ).getValue();
				final int photos = savedJobParametersMap.get( SavedJobParameterKey.PARAM_PHOTOS_QTY ).getValueInt();
				final boolean deletePictureAfterImport = savedJobParametersMap.get( SavedJobParameterKey.DELETE_PICTURE_AFTER_IMPORT ).getValueBoolean();
				final int userId = savedJobParametersMap.get( SavedJobParameterKey.PARAM_USER_ID ).getValueInt();

				aModel.setPictureDir( pictureDir );
				aModel.setPhotoQtyLimit( photos > 0 ? String.valueOf( photos ) : StringUtils.EMPTY );
				aModel.setDeletePictureFromDiskAfterImport( deletePictureAfterImport );
				aModel.setAssignAllGeneratedPhotosToUserId( userId );
				aModel.setAssignAllGeneratedPhotosToUser( userService.load( userId ) );

				initModelDateRange( aModel, savedJobParametersMap );
				break;
			case PHOTOSIGHT:
				final String photosightUserId = savedJobParametersMap.get( SavedJobParameterKey.PARAM_USER_ID ).getValue();
				final String userName = savedJobParametersMap.get( SavedJobParameterKey.USER_NAME ).getValue();
				final String genderId = savedJobParametersMap.get( SavedJobParameterKey.USER_GENDER_ID ).getValue();
				final String membershipId = savedJobParametersMap.get( SavedJobParameterKey.USER_MEMBERSHIP_ID ).getValue();
				final boolean importComments = savedJobParametersMap.get( SavedJobParameterKey.IMPORT_PHOTOSIGHT_COMMENTS ).getValueBoolean();
				final String pageQty = savedJobParametersMap.get( SavedJobParameterKey.IMPORT_PAGE_QTY ).getValue();
				final String delayBetweenRequests = savedJobParametersMap.get( SavedJobParameterKey.DELAY_BETWEEN_REQUESTS ).getValue();

				aModel.setPhotosightUserId( photosightUserId );
				aModel.setUserName( userName );
				aModel.setUserGenderId( genderId );
				aModel.setUserMembershipId( membershipId );
				aModel.setImportComments( importComments );
				aModel.setPageQty( pageQty );
				aModel.setDelayBetweenRequest( delayBetweenRequests );

				final List<String> categories = savedJobParametersMap.get( SavedJobParameterKey.PHOTOSIGHT_CATEGORIES ).getValueListString();
				/*Collections.sort( categories, new Comparator<String>() {
					@Override
					public int compare( final String id1, final String id2 ) {
						return PhotosightCategory.getById( id1 ).name().compareTo( PhotosightCategory.getById( id2 ).name() );
					}
				} );*/

				aModel.setPhotosightCategories( categories );
				break;
			default:
				throw new BaseRuntimeException( String.format( "Unsupported PhotoImportSource: %s", importSource ) );
		}
	}

	@Override
	protected String getStartViewName() {
		return START_VIEW;
	}
}
