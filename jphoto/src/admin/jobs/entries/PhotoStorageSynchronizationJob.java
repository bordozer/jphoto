package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCacheXmlUtils;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryEntry;
import core.enums.SavedJobParameterKey;
import core.enums.UserGender;
import core.general.base.CommonProperty;
import core.general.photo.PhotoImageLocationType;
import core.general.user.UserMembershipType;
import core.log.LogHelper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoStorageSynchronizationJob extends NoParametersAbstractJob {

	public PhotoStorageSynchronizationJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( PhotoStorageSynchronizationJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {
		final List<String> usersIds = getUsersIds();

		final List<RemotePhotoSiteCategory> photosightCategories = Arrays.asList( PhotosightCategory.values() );

		final AbstractImportParameters importParameters = new RemoteSitePhotosImportParameters( getPhotosImportSource()
			, usersIds
			, UserGender.MALE
			, UserMembershipType.AUTHOR
			, true
			, 0
			, 0
			, getLanguage()
			, true
			, photosightCategories
			, PhotoImageLocationType.WEB ); // TODO: send this as parameter selected on UI

		final AbstractPhotoImportStrategy importStrategy = new RemotePhotoSiteImportStrategy( this, importParameters, services );

		updateTotalOperations( importStrategy );

		importStrategy.doImport();
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.PHOTO_STORAGE_SYNCHRONIZATION;
	}

	// TODO: the same is in PhotosImportJob
	private void updateTotalOperations( final AbstractPhotoImportStrategy importStrategy ) throws IOException {

		final boolean operationCountIsDefinedByUser = totalJopOperations > 0 && totalJopOperations != AbstractJob.OPERATION_COUNT_UNKNOWN;
		if ( !operationCountIsDefinedByUser ) {
			totalJopOperations = importStrategy.calculateTotalPagesToProcess();
		}

		generationMonitor.setTotal( totalJopOperations );

		final JobExecutionHistoryEntry historyEntry = services.getJobExecutionHistoryService().load( jobId );
		services.getJobExecutionHistoryService().updateTotalJobSteps( historyEntry.getId(), totalJopOperations );

		log.debug( String.format( "Update operation count: %d", totalJopOperations ) );
	}

	private List<String> getUsersIds() {
		final List<String> usersIds = newArrayList();
		final File storage = new RemotePhotoSiteCacheXmlUtils( getPhotosImportSource(), services.getSystemVarsService().getRemotePhotoSitesCacheFolder(), services.getRemotePhotoCategoryService(), PhotoImageLocationType.WEB ).getPhotoStorage();

		final File[] userDirList = storage.listFiles( services.getPredicateUtilsService().getDirFilter() );
		for ( final File file : userDirList ) {
			usersIds.add( file.getName() );
		}
		return usersIds;
	}

	private PhotosImportSource getPhotosImportSource() {
		return PhotosImportSource.PHOTOSIGHT;
	}
}
