package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSitePhotoImageFileUtils;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightRemoteContentHelper;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryEntry;
import core.enums.SavedJobParameterKey;
import core.enums.UserGender;
import core.general.base.CommonProperty;
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

		final List<PhotosightCategory> photosightCategories = Arrays.asList( PhotosightCategory.values() );

		final AbstractImportParameters importParameters = new RemoteSitePhotosImportParameters(
			usersIds
			, UserGender.MALE
			, UserMembershipType.AUTHOR
			, true
			, 0
			, 0
			, getLanguage()
			, true
			, photosightCategories
			, new PhotosightRemoteContentHelper(), new PhotosightContentDataExtractor() ); // TODO: send this as parameter selected on UI

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
		totalJopOperations = importStrategy.getTotalOperations( totalJopOperations );

		generationMonitor.setTotal( totalJopOperations );

		final JobExecutionHistoryEntry historyEntry = services.getJobExecutionHistoryService().load( jobId );
		services.getJobExecutionHistoryService().updateTotalJobSteps( historyEntry.getId(), totalJopOperations );

		log.debug( String.format( "Update operation count: %d", totalJopOperations ) );
	}

	private List<String> getUsersIds() {
		final List<String> usersIds = newArrayList();
		final File storage = new RemotePhotoSitePhotoImageFileUtils( PhotosImportSource.PHOTOSIGHT, services.getSystemVarsService().getRemotePhotoSitesCacheFolder() ).getPhotoStorage();

		final File[] userDirList = storage.listFiles( services.getPredicateUtilsService().getDirFilter() );
		for ( final File file : userDirList ) {
			usersIds.add( file.getName() );
		}
		return usersIds;
	}
}
