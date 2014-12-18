package core.services.archiving;

import java.util.Date;

public interface ArchivingService {

	void archivePhotosPreviewsOlderThen( final int days );

	void archivePhotosAppraisalsOlderThen( final int days );

	void archivePhoto( final int photoId );

	Date getArchiveStartDate( int days );
}
