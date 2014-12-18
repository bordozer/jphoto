package core.services.archiving;

import core.log.LogHelper;
import core.services.dao.ArchivingDao;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ArchivingServiceImpl implements ArchivingService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private ArchivingDao archivingDao;

	private final LogHelper log = new LogHelper( ArchivingServiceImpl.class );

	@Override
	public void archivePhotosPreviewsOlderThen( final int days ) {
		archivingDao.deletePhotosPreviewsOlderThen( getArchiveStartDate( days ) );
	}

	@Override
	public void archivePhotosAppraisalsOlderThen( final int days ) {

	}

	@Override
	public void archivePhoto( final int photoId ) {
		log.debug( String.format( "Archiving photo #%d", photoId ) );
		// TODO: blu: do not forget implement this
	}

	@Override
	public Date getArchiveStartDate( final int days ) {
		return dateUtilsService.getFirstSecondOfTheDayNDaysAgo( days - 1 );
	}
}
