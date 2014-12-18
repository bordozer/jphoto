package core.services.archiving;

public interface ArchivingService {

	void archivePhotosPreviewsOlderThen( final int days );

	void archivePhotosAppraisalsOlderThen( final int days );

	void archivePhoto( final int photoId );
}
