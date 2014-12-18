package core.services.archiving;

public interface ArchivingService {

	void archivePhotosPreviews( final int olderThen );

	void archivePhotosAppraisals( final int olderThen );

	void archivePhotos( final int olderThen );
}
