package core.services.archiving;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.log.LogHelper;
import core.services.dao.ArchivingDao;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ArchivingServiceImpl implements ArchivingService {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private ArchivingDao archivingDao;

	@Autowired
	private PhotoCommentDaoArchImpl photoCommentArchDao;

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
	public void archivePhoto( final Photo photo ) {

		log.debug( String.format( "Archiving photo %s", photo ) );

		final List<Integer> commentsToArchive = newArrayList();

		final List<Integer> rootCommentsIds = photoCommentService.loadRootCommentsIds( photo.getId() );
		for ( final int rootCommentsId : rootCommentsIds ) {

			commentsToArchive.add( rootCommentsId );

			final PhotoComment comment = photoCommentService.load( rootCommentsId );
			comment.setId( 0 );

			photoCommentArchDao.archive( comment );

			processAnswers( rootCommentsId, commentsToArchive );
		}

		for ( final int photoCommentId : commentsToArchive ) {
			photoCommentService.delete( photoCommentId );
		}

		photo.setArchived( true );

		photoService.save( photo );
	}

	private void processAnswers( final Integer parentCommentsId, final List<Integer> commentsToArchive ) {

		final List<PhotoComment> answers = photoCommentService.loadAnswersOnComment( parentCommentsId );

		for ( final PhotoComment answer : answers ) {
			commentsToArchive.add( answer.getId() );
			answer.setId( 0 );
			answer.setReplyToCommentId( parentCommentsId );

			photoCommentArchDao.archive( answer );

			processAnswers( parentCommentsId, commentsToArchive );
		}
	}

	@Override
	public Date getArchiveStartDate( final int days ) {
		return dateUtilsService.getFirstSecondOfTheDayNDaysAgo( days - 1 );
	}
}
