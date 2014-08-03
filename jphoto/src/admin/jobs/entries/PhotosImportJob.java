package admin.jobs.entries;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.FileSystemImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.filesystem.FilesystemImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSiteUrlHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImportStrategy;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryEntry;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import core.enums.SavedJobParameterKey;
import core.enums.UserGender;
import core.enums.YesNo;
import core.general.base.CommonProperty;
import core.general.photo.PhotoImageImportStrategyType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.log.LogHelper;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class PhotosImportJob extends AbstractDateRangeableJob {

	private PhotosImportSource importSource;
	private AbstractImportParameters importParameters;
	private List<RemotePhotoSiteCategory> remotePhotoSiteCategories;

	public PhotosImportJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( PhotosImportJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {

		final AbstractPhotoImportStrategy importStrategy = getImportStrategy( importSource );

		updateTotalOperations( importStrategy );

		importStrategy.doImport();
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.PHOTOS_IMPORT_SOURCE, new CommonProperty( SavedJobParameterKey.PHOTOS_IMPORT_SOURCE.getId(), getImportSource().getId() ) );

		switch ( importSource ) {
			case FILE_SYSTEM:
				final FileSystemImportParameters fsParameters = ( FileSystemImportParameters ) importParameters;

				parametersMap.put( SavedJobParameterKey.PARAM_PICTURES_DIR, new CommonProperty( SavedJobParameterKey.PARAM_PICTURES_DIR.getId(), fsParameters.getPictureDir() ) );
				parametersMap.put( SavedJobParameterKey.PARAM_USER_ID, new CommonProperty( SavedJobParameterKey.PARAM_USER_ID.getId(), fsParameters.getAssignAllGeneratedPhotosToUserId() ) );
				parametersMap.put( SavedJobParameterKey.DELETE_PICTURE_AFTER_IMPORT, new CommonProperty( SavedJobParameterKey.DELETE_PICTURE_AFTER_IMPORT.getId(), fsParameters.isDeletePictureAfterImport() ) );
				final int qtyLimit = fsParameters.getPhotoQtyLimit();
				parametersMap.put( SavedJobParameterKey.PARAM_PHOTOS_QTY, new CommonProperty( SavedJobParameterKey.PARAM_PHOTOS_QTY.getId(), qtyLimit ) );

				getDateRangeParametersMap( parametersMap );

				totalJopOperations = qtyLimit;
				break;
			case PHOTOSIGHT:
			case PHOTO35:
			case NATURELIGHT:

				final RemoteSitePhotosImportParameters remoteSitePhotosImportParameters = ( RemoteSitePhotosImportParameters ) importParameters;

				parametersMap.put( SavedJobParameterKey.PARAM_USER_ID, new CommonProperty( SavedJobParameterKey.PARAM_USER_ID.getId(), remoteSitePhotosImportParameters.getRemoteUserIds() ) );
				parametersMap.put( SavedJobParameterKey.USER_GENDER_ID, new CommonProperty( SavedJobParameterKey.USER_GENDER_ID.getId(), remoteSitePhotosImportParameters.getUserGender().getId() ) );
				parametersMap.put( SavedJobParameterKey.USER_MEMBERSHIP_ID, new CommonProperty( SavedJobParameterKey.USER_MEMBERSHIP_ID.getId(), remoteSitePhotosImportParameters.getMembershipType().getId() ) );
				parametersMap.put( SavedJobParameterKey.IMPORT_REMOTE_PHOTO_SITE_COMMENTS, new CommonProperty( SavedJobParameterKey.IMPORT_REMOTE_PHOTO_SITE_COMMENTS.getId(), remoteSitePhotosImportParameters.isImportComments() ) );
				parametersMap.put( SavedJobParameterKey.DELAY_BETWEEN_REQUESTS, new CommonProperty( SavedJobParameterKey.DELAY_BETWEEN_REQUESTS.getId(), remoteSitePhotosImportParameters.getDelayBetweenRequest() ) );
				final int pageQty = remoteSitePhotosImportParameters.getPageQty();
				parametersMap.put( SavedJobParameterKey.IMPORT_PAGE_QTY, new CommonProperty( SavedJobParameterKey.IMPORT_PAGE_QTY.getId(), pageQty ) );
				parametersMap.put( SavedJobParameterKey.PHOTO_IMAGE_IMPORT_STRATEGY, new CommonProperty( SavedJobParameterKey.PHOTO_IMAGE_IMPORT_STRATEGY.getId(), remoteSitePhotosImportParameters.getPhotoImageImportStrategyType().getId() ) );

				final List<String> remotePhotoSiteCategoryIds = Lists.transform( remotePhotoSiteCategories, new Function<RemotePhotoSiteCategory, String>() {
					@Override
					public String apply( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
						return String.valueOf( remotePhotoSiteCategory.getId() );
					}
				} );
				parametersMap.put( SavedJobParameterKey.REMOTE_PHOTO_SITE_CATEGORIES, new CommonProperty( SavedJobParameterKey.REMOTE_PHOTO_SITE_CATEGORIES.getId(), remotePhotoSiteCategoryIds ) );

				totalJopOperations = pageQty;
				break;
			default:
				throw new IllegalArgumentException( String.format( "Unsupported import source: %s", importSource ) );
		}

		return parametersMap;
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {

		importSource = PhotosImportSource.getById( jobParameters.get( SavedJobParameterKey.PHOTOS_IMPORT_SOURCE ).getValueInt() );

		switch ( importSource ) {
			case FILE_SYSTEM:
				final String pictureDir = jobParameters.get( SavedJobParameterKey.PARAM_PICTURES_DIR ).getValue();
				final int assignAllGeneratedPhotosToUserId = jobParameters.get( SavedJobParameterKey.PARAM_USER_ID ).getValueInt();
				final boolean deletePictureAfterImport = jobParameters.get( SavedJobParameterKey.DELETE_PICTURE_AFTER_IMPORT ).getValueBoolean();
				final int photoQtyLimit = jobParameters.get( SavedJobParameterKey.PARAM_PHOTOS_QTY ).getValueInt();

				setDateRangeParameters( jobParameters );

				importParameters = new FileSystemImportParameters( pictureDir, photoQtyLimit, deletePictureAfterImport, assignAllGeneratedPhotosToUserId, jobDateRange, getLanguage() );

				break;
			case PHOTOSIGHT:
			case PHOTO35:
			case NATURELIGHT:
				final List<String> remotePhotoSiteUserIds = jobParameters.get( SavedJobParameterKey.PARAM_USER_ID ).getValueListString();
				final UserGender userGender = UserGender.getById( jobParameters.get( SavedJobParameterKey.USER_GENDER_ID ).getValueInt() );
				final UserMembershipType membershipType = UserMembershipType.getById( jobParameters.get( SavedJobParameterKey.USER_MEMBERSHIP_ID ).getValueInt() );
				final boolean importComments = jobParameters.get( SavedJobParameterKey.IMPORT_REMOTE_PHOTO_SITE_COMMENTS ).getValueBoolean();
				final boolean breakImportIfAlreadyImportedPhotoFound = jobParameters.get( SavedJobParameterKey.BREAK_IMPORT_IF_ALREADY_IMPORTED_PHOTO_FOUND ).getValueBoolean();
				final int delayBetweenRequest = jobParameters.get( SavedJobParameterKey.DELAY_BETWEEN_REQUESTS ).getValueInt();
				final int pageQty = jobParameters.get( SavedJobParameterKey.IMPORT_PAGE_QTY ).getValueInt();
				final PhotoImageImportStrategyType photoImageImportStrategyType = PhotoImageImportStrategyType.getById( jobParameters.get( SavedJobParameterKey.PHOTO_IMAGE_IMPORT_STRATEGY ).getValueInt() );

				final List<RemotePhotoSiteCategory> remotePhotoSiteCategories = Lists.transform( jobParameters.get( SavedJobParameterKey.REMOTE_PHOTO_SITE_CATEGORIES ).getValueListInt(), new Function<Integer, RemotePhotoSiteCategory>() {
					@Override
					public RemotePhotoSiteCategory apply( final Integer id ) {
						return RemotePhotoSiteCategory.getById( importSource, id );
					}
				} );

				importParameters = new RemoteSitePhotosImportParameters( importSource, remotePhotoSiteUserIds, userGender, membershipType, importComments, delayBetweenRequest, pageQty
					, getLanguage(), breakImportIfAlreadyImportedPhotoFound, remotePhotoSiteCategories, photoImageImportStrategyType );

				break;
			default:
				throw new IllegalArgumentException( String.format( "Unsupported PhotoImportSource: %s", importSource ) );
		}
	}

	@Override
	public String getJobParametersDescription() {
		final TranslatorService translatorService = services.getTranslatorService();

		final StringBuilder builder = new StringBuilder();

		switch ( importSource ) {
			case FILE_SYSTEM:
				final FileSystemImportParameters fsParameters = ( FileSystemImportParameters ) importParameters;

				builder.append( translatorService.translate( "Photo import job parameter: Dir", getLanguage() ) ).append( ": " ).append( fsParameters.getPictureDir() ).append( "<br />" );
				builder.append( translatorService.translate( "Photo import job parameter: Generate preview", getLanguage() ) ).append( ": " ).append( translatorService.translate( fsParameters.isDeletePictureAfterImport() ? YesNo.YES.getName() : YesNo.NO.getName(), getLanguage() ) ).append( "<br />" );
				final int userId = fsParameters.getAssignAllGeneratedPhotosToUserId();
				if ( userId > 0 ) {
					final User user = services.getUserService().load( userId );
					builder.append( translatorService.translate( "Photo import job parameter: User Id", getLanguage() ) ).append( ": " ).append( services.getEntityLinkUtilsService().getUserCardLink( user, getLanguage() ) ).append( "<br />" );
				}

				addDateRangeParameters( builder );

				builder.append( translatorService.translate( "Photo import job parameter: photos to process", getLanguage() ) ).append( ": " ).append( fsParameters.getPhotoQtyLimit() );

				break;
			case PHOTOSIGHT:
			case PHOTO35:
			case NATURELIGHT:
				final RemoteSitePhotosImportParameters remoteSitePhotosImportParameters = ( RemoteSitePhotosImportParameters ) importParameters;

				builder.append( translatorService.translate( "Photo import job parameter: remote site", getLanguage() ) ).append( ": " ).append( translatorService.translate( importSource.getName(), getLanguage() ) ).append( "<br />" );

				final AbstractRemotePhotoSiteUrlHelper remoteContentHelper = AbstractRemotePhotoSiteUrlHelper.getInstance( importSource );

				final List<String> remotePhotoSiteUserIds = Lists.transform( remoteSitePhotosImportParameters.getRemoteUserIds(), new Function<String, String>() {
					@Override
					public String apply( final String remotePhotoSiteUserId ) {
						return String.format( "<a href='%s' title='%s'>%s</a>", remoteContentHelper.getUserCardUrl( remotePhotoSiteUserId ), translatorService.translate( "Photo import job parameter: Remote photo site users IDs", getLanguage() ), remotePhotoSiteUserId );
					}
				} );
				final String remoteSiteUserLinks = StringUtils.join( remotePhotoSiteUserIds, ", " );
				builder.append( translatorService.translate( "Photo import job parameter: Remote photo site users IDs", getLanguage() ) )
					   .append( ": " )
					   .append( remoteSiteUserLinks ).append( "<br />" );

				final List<RemotePhotoSiteCategory> remotePhotoSiteCategories = remoteSitePhotosImportParameters.getRemotePhotoSiteCategories();
				builder.append( translatorService.translate( "Photo import job parameter: Import photos from categories", getLanguage() ) ).append( ": " );

				final RemotePhotoSiteCategory[] remoteCategories = RemotePhotoSiteCategory.getRemotePhotoSiteCategories( importSource );

				final String catText;

				if ( remotePhotoSiteCategories.size() == remoteCategories.length ) {
					catText = translatorService.translate( "Photo import job parameter: All categories", getLanguage() );
				} else {
					if ( remotePhotoSiteCategories.size() < remoteCategories.length / 2 ) {
						final List<String> categories = Lists.transform( remotePhotoSiteCategories, new Function<RemotePhotoSiteCategory, String>() {
							@Override
							public String apply( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
								return remotePhotoSiteCategory.getName();
							}
						} );
						catText = StringUtils.join( categories, ", " );
					} else {
						final List<String> excludedCategories = newArrayList();
						for ( final RemotePhotoSiteCategory remotePhotoSiteCategory : remoteCategories ) {
							if ( ! remotePhotoSiteCategories.contains( remotePhotoSiteCategory ) ) {
								excludedCategories.add( String.format( "<span style='text-decoration: line-through;'>%s</span>", remotePhotoSiteCategory.getName() ) );
							}
						}
						catText = StringUtils.join( excludedCategories, ", " );
					}
				}
				builder.append( catText ).append( "<br />" );

				final int pageQty = remoteSitePhotosImportParameters.getPageQty();
				builder.append( translatorService.translate( "Photo import job parameter: Pages to process", getLanguage() ) ).append( ": " ).append( pageQty > 0 ? pageQty : translatorService.translate( "Photo import job parameter: Process all pages", getLanguage() ) ).append( "<br />" );

				builder.append( translatorService.translate( "Photo import job parameter: Import comments", getLanguage() ) ).append( ": " ).append( translatorService.translate( remoteSitePhotosImportParameters.isImportComments() ? YesNo.YES.getName() : YesNo.NO.getName(), getLanguage() ) ).append( "<br />" );

				builder.append( translatorService.translate( "Photo import job parameter: Being imported user parameters section", getLanguage() ) ).append( ": " ).append( "<br />" );
				builder.append( "&nbsp;" ).append( translatorService.translate( "Photo import job parameter: Gender", getLanguage() ) ).append( ": " ).append( translatorService.translate( remoteSitePhotosImportParameters.getUserGender().getName(), getLanguage() ) ).append( "<br />" );
				builder.append( "&nbsp;" ).append( translatorService.translate( "Photo import job parameter: Membership", getLanguage() ) ).append( ": " ).append( translatorService.translate( remoteSitePhotosImportParameters.getMembershipType().getName(), getLanguage() ) ).append( "<br />" );

				builder.append( translatorService.translate( "Photo import job parameter: Delay between requests", getLanguage() ) ).append( ": " ).append( remoteSitePhotosImportParameters.getDelayBetweenRequest() ).append( "<br />" );

				builder.append( translatorService.translate( "Photo import job parameter: Image import strategy", getLanguage() ) ).append( ": " )
					   .append( translatorService.translate( remoteSitePhotosImportParameters.getPhotoImageImportStrategyType().getDescription(), getLanguage() ) ).append( "<br />" );

				break;
			default:
				throw new IllegalArgumentException( String.format( "Unsupported import source: %s", importSource ) );
		}

		return builder.toString();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.PHOTOS_IMPORT;
	}

	public PhotosImportSource getImportSource() {
		return importSource;
	}

	public void setImportSource( final PhotosImportSource importSource ) {
		this.importSource = importSource;
	}

	public AbstractImportParameters getImportParameters() {
		return importParameters;
	}

	public void setImportParameters( final AbstractImportParameters importParameters ) {
		this.importParameters = importParameters;
	}

	// TODO: the same is in PhotoStorageSynchronizationJob
	private void updateTotalOperations( final AbstractPhotoImportStrategy importStrategy ) throws IOException {

		final boolean operationCountIsDefinedByUser = totalJopOperations > 0 && totalJopOperations != AbstractJob.OPERATION_COUNT_UNKNOWN;
		if ( !operationCountIsDefinedByUser ) {
			totalJopOperations = importStrategy.calculateTotalPagesToProcess();
		}

		generationMonitor.setTotal( totalJopOperations ); // TODO: do I need this?

		final JobExecutionHistoryEntry historyEntry = services.getJobExecutionHistoryService().load( jobId );
		services.getJobExecutionHistoryService().updateTotalJobSteps( historyEntry.getId(), totalJopOperations );

		log.debug( String.format( "Update operation count: %d", totalJopOperations ) );
	}

	private AbstractPhotoImportStrategy getImportStrategy( final PhotosImportSource importSource ) {
		final AbstractPhotoImportStrategy importStrategy;
		switch ( importSource ) {
			case FILE_SYSTEM:
				importStrategy = new FilesystemImportStrategy( this, importParameters, services );
				break;
			case PHOTOSIGHT:
			case PHOTO35:
			case NATURELIGHT:
				importStrategy = new RemotePhotoSiteImportStrategy( this, importParameters, services );
				break;
			default:
				throw new IllegalArgumentException( String.format( "Unsupported import source: %s", importSource ) );
		}
		return importStrategy;
	}

	public void setRemotePhotoSiteCategories( final List<RemotePhotoSiteCategory> remotePhotoSiteCategories ) {
		this.remotePhotoSiteCategories = remotePhotoSiteCategories;
	}
}
