package core.services.dao;

import java.util.Date;

public interface ArchivingDao {

	void deletePhotosPreviewsOlderThen( final Date time );
}
