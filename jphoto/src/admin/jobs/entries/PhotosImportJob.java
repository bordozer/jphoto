package admin.jobs.entries;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.FileSystemImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.PhotosightImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.filesystem.FilesystemImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightRemoteContentHelper;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryEntry;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import core.enums.SavedJobParameterKey;
import core.enums.UserGender;
import core.enums.YesNo;
import core.general.base.CommonProperty;
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
	private List<PhotosightCategory> photosightCategories;

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

				final PhotosightImportParameters photosightParameters = ( PhotosightImportParameters ) importParameters;

				parametersMap.put( SavedJobParameterKey.PARAM_USER_ID, new CommonProperty( SavedJobParameterKey.PARAM_USER_ID.getId(), photosightParameters.getPhotosightUserIds() ) );
				parametersMap.put( SavedJobParameterKey.USER_NAME, new CommonProperty( SavedJobParameterKey.USER_NAME.getId(), photosightParameters.getUserName() ) );
				parametersMap.put( SavedJobParameterKey.USER_GENDER_ID, new CommonProperty( SavedJobParameterKey.USER_GENDER_ID.getId(), photosightParameters.getUserGender().getId() ) );
				parametersMap.put( SavedJobParameterKey.USER_MEMBERSHIP_ID, new CommonProperty( SavedJobParameterKey.USER_MEMBERSHIP_ID.getId(), photosightParameters.getMembershipType().getId() ) );
				parametersMap.put( SavedJobParameterKey.IMPORT_PHOTOSIGHT_COMMENTS, new CommonProperty( SavedJobParameterKey.IMPORT_PHOTOSIGHT_COMMENTS.getId(), photosightParameters.isImportComments() ) );
				parametersMap.put( SavedJobParameterKey.DELAY_BETWEEN_REQUESTS, new CommonProperty( SavedJobParameterKey.DELAY_BETWEEN_REQUESTS.getId(), photosightParameters.getDelayBetweenRequest() ) );
				final int pageQty = photosightParameters.getPageQty();
				parametersMap.put( SavedJobParameterKey.IMPORT_PAGE_QTY, new CommonProperty( SavedJobParameterKey.IMPORT_PAGE_QTY.getId(), pageQty ) );


				final List<String> photosightCategoryIds = Lists.transform( photosightCategories, new Function<PhotosightCategory, String>() {
					@Override
					public String apply( final PhotosightCategory photosightCategory ) {
						return String.valueOf( photosightCategory.getId() );
					}
				} );
				parametersMap.put( SavedJobParameterKey.PHOTOSIGHT_CATEGORIES, new CommonProperty( SavedJobParameterKey.PHOTOSIGHT_CATEGORIES.getId(), photosightCategoryIds ) );

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
				final List<String> photosightUserId = jobParameters.get( SavedJobParameterKey.PARAM_USER_ID ).getValueListString();
				final String userName = jobParameters.get( SavedJobParameterKey.USER_NAME ).getValue();
				final UserGender userGender = UserGender.getById( jobParameters.get( SavedJobParameterKey.USER_GENDER_ID ).getValueInt() );
				final UserMembershipType membershipType = UserMembershipType.getById( jobParameters.get( SavedJobParameterKey.USER_MEMBERSHIP_ID ).getValueInt() );
				final boolean importComments = jobParameters.get( SavedJobParameterKey.IMPORT_PHOTOSIGHT_COMMENTS ).getValueBoolean();
				final boolean breakImportIfAlreadyImportedPhotoFound = jobParameters.get( SavedJobParameterKey.BREAK_IMPORT_IF_ALREADY_IMPORTED_PHOTO_FOUND ).getValueBoolean();
				final int delayBetweenRequest = jobParameters.get( SavedJobParameterKey.DELAY_BETWEEN_REQUESTS ).getValueInt();
				final int pageQty = jobParameters.get( SavedJobParameterKey.IMPORT_PAGE_QTY ).getValueInt();

				final List<PhotosightCategory> photosightCategories = Lists.transform( jobParameters.get( SavedJobParameterKey.PHOTOSIGHT_CATEGORIES ).getValueListInt(), new Function<Integer, PhotosightCategory>() {
					@Override
					public PhotosightCategory apply( final Integer id ) {
						return PhotosightCategory.getById( id );
					}
				} );

				importParameters = new PhotosightImportParameters( photosightUserId, userName, userGender, membershipType, importComments, delayBetweenRequest, pageQty
					, getLanguage(), breakImportIfAlreadyImportedPhotoFound, photosightCategories );

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
				final PhotosightImportParameters photosightParameters = ( PhotosightImportParameters ) importParameters;

				final List<String> photosightUserIds = Lists.transform( photosightParameters.getPhotosightUserIds(), new Function<String, String>() {
					@Override
					public String apply( final String photosightUserId ) {
						return String.format( "<a href='%s' title='%s'>%s</a>", PhotosightRemoteContentHelper.getUserCardUrl( photosightUserId ), translatorService.translate( "Photo import job parameter: Photosight user ID", getLanguage() ), photosightUserId );
					}
				} );
				final String photosightUserLinks = StringUtils.join( photosightUserIds, ", " );
				builder.append( translatorService.translate( "Photo import job parameter: Photosight user ids", getLanguage() ) )
					   .append( ": " )
					   .append( photosightUserLinks ).append( "<br />" );

				final List<PhotosightCategory> photosightCategories = photosightParameters.getPhotosightCategories();
				builder.append( translatorService.translate( "Photo import job parameter: Import photos from categories", getLanguage() ) ).append( ": " );
				final String catText;
				if ( photosightCategories.size() == PhotosightCategory.values().length ) {
					catText = translatorService.translate( "Photo import job parameter: All categories", getLanguage() );
				} else {
					if ( photosightCategories.size() < PhotosightCategory.values().length / 2 ) {
						final List<String> categories = Lists.transform( photosightCategories, new Function<PhotosightCategory, String>() {
							@Override
							public String apply( final PhotosightCategory photosightCategory ) {
								return photosightCategory.getName();
							}
						} );
						catText = StringUtils.join( categories, ", " );
					} else {
						final List<String> excludedCategories = newArrayList();
						for ( final PhotosightCategory photosightCategory : PhotosightCategory.values() ) {
							if ( ! photosightCategories.contains( photosightCategory ) ) {
								excludedCategories.add( String.format( "<span style='text-decoration: line-through;'>%s</span>", photosightCategory.getName() ) );
							}
						}
						catText = StringUtils.join( excludedCategories, ", " );
					}
				}
				builder.append( catText ).append( "<br />" );

				final int pageQty = photosightParameters.getPageQty();
				builder.append( translatorService.translate( "Photo import job parameter: Pages to process", getLanguage() ) ).append( ": " ).append( pageQty > 0 ? pageQty : translatorService.translate( "Photo import job parameter: Process all pages", getLanguage() ) ).append( "<br />" );

				builder.append( translatorService.translate( "Photo import job parameter: Import comments", getLanguage() ) ).append( ": " ).append( translatorService.translate( photosightParameters.isImportComments() ? YesNo.YES.getName() : YesNo.NO.getName(), getLanguage() ) ).append( "<br />" );

				builder.append( translatorService.translate( "Photo import job parameter: Being imported user parameters section", getLanguage() ) ).append( ": " ).append( "<br />" );
				builder.append( "&nbsp;" ).append( translatorService.translate( "Photo import job parameter: User name", getLanguage() ) ).append( ": " ).append( photosightParameters.getUserName() ).append( "<br />" );
				builder.append( "&nbsp;" ).append( translatorService.translate( "Photo import job parameter: Gender", getLanguage() ) ).append( ": " ).append( translatorService.translate( photosightParameters.getUserGender().getName(), getLanguage() ) ).append( "<br />" );
				builder.append( "&nbsp;" ).append( translatorService.translate( "Photo import job parameter: Membership", getLanguage() ) ).append( ": " ).append( translatorService.translate( photosightParameters.getMembershipType().getName(), getLanguage() ) ).append( "<br />" );

				builder.append( translatorService.translate( "Photo import job parameter: Delay between requests", getLanguage() ) ).append( ": " ).append( photosightParameters.getDelayBetweenRequest() ).append( "<br />" );

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
		totalJopOperations = importStrategy.getTotalOperations( totalJopOperations );

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
				importStrategy = new PhotosightImportStrategy( this, importParameters, services );
				break;
			default:
				throw new IllegalArgumentException( String.format( "Unsupported import source: %s", importSource ) );
		}
		return importStrategy;
	}

	public void setPhotosightCategories( final List<PhotosightCategory> photosightCategories ) {
		this.photosightCategories = photosightCategories;
	}
}
