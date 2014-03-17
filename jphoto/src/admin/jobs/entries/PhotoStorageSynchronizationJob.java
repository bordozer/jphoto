package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.controllers.jobs.edit.photosImport.importParameters.ImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.PhotosightImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightImageFileUtils;
import admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightImportStrategy;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryEntry;
import core.enums.SavedJobParameterKey;
import core.enums.UserGender;
import core.general.base.CommonProperty;
import core.general.user.UserMembershipType;
import core.log.LogHelper;
import utils.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoStorageSynchronizationJob extends NoParametersAbstractJob {

	public PhotoStorageSynchronizationJob() {
		super( new LogHelper( PhotoStorageSynchronizationJob.class ) );
	}

	@Override
	protected void runJob() throws Throwable {
		final List<Integer> usersIds = getUsersIds();

		final List<PhotosightCategory> photosightCategories = Arrays.asList( PhotosightCategory.values() );
		final ImportParameters importParameters = new PhotosightImportParameters( usersIds, "", UserGender.MALE, UserMembershipType.AUTHOR, true, 0, 0, photosightCategories );

		final AbstractPhotoImportStrategy importStrategy = new PhotosightImportStrategy( this, importParameters, services );

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

	private List<Integer> getUsersIds() {
		final List<Integer> usersIds = newArrayList();
		final File storage = PhotosightImageFileUtils.getPhotoStorage();

		final File[] userDirList = storage.listFiles( services.getPredicateUtilsService().getDirFilter() );
		for ( final File file : userDirList ) {
			usersIds.add( NumberUtils.convertToInt( file.getName() ) );
		}
		return usersIds;
	}
}
