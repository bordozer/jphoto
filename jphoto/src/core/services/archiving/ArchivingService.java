package core.services.archiving;

import core.general.photo.Photo;

import java.util.Date;
import java.util.List;

public interface ArchivingService {

	void archivePhotosPreviewsOlderThen( final int days );

	void archivePhotosAppraisalsOlderThen( final int days );

	void archivePhoto( final Photo photo );

	Date getArchiveStartDate( int days );

	List<Integer> getNotArchivedPhotosIdsUploadedAtOrEarlieThen( final Date time );
}
