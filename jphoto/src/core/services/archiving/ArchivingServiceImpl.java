package core.services.archiving;

import core.general.photo.PhotoComment;
import core.log.LogHelper;
import core.services.dao.ArchivingDao;
import core.services.photo.PhotoCommentService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ArchivingServiceImpl implements ArchivingService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private ArchivingDao archivingDao;

	private final LogHelper log = new LogHelper( ArchivingServiceImpl.class );

	@Override
	public void archivePhotosPreviewsOlderThen( final int days ) {
		archivingDao.deletePhotosPreviewsOlderThen( getArchiveStartDate( days ) );
	}

	@Override
	public void archivePhotosAppraisalsOlderThen( final int days ) {
		// TODO: blu: do not forget implement this
	}

	@Override
	public void archivePhoto( final int photoId ) {

		log.debug( String.format( "Archiving photo #%d", photoId ) );

		final List<PhotoComment> commentsToArchive = newArrayList();

		final List<Integer> rootCommentsIds = photoCommentService.loadRootCommentsIds( photoId );
		for ( final Integer rootCommentsId : rootCommentsIds ) {
			commentsToArchive.add( photoCommentService.load( rootCommentsId ) );

			processAnswers( rootCommentsId, commentsToArchive );
		}

		for ( final PhotoComment photoComment : commentsToArchive ) {
			photoCommentService.archive( photoComment );
		}
	}

	private void processAnswers( final Integer parentCommentsId, final List<PhotoComment> commentsToArchive ) {
		final List<PhotoComment> answers = photoCommentService.loadAnswersOnComment( parentCommentsId );
		for ( final PhotoComment answer : answers ) {
			commentsToArchive.add( answer );
			processAnswers( answer.getId(), commentsToArchive );
		}
	}

	@Override
	public Date getArchiveStartDate( final int days ) {
		return dateUtilsService.getFirstSecondOfTheDayNDaysAgo( days - 1 );
	}
}
