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

		final List<Integer> rootCommentsIds = photoCommentService.loadRootCommentsIds( photo.getId() );
		for ( final int rootCommentsId : rootCommentsIds ) {

			final PhotoComment comment = photoCommentService.load( rootCommentsId );
			comment.setId( 0 );

			photoCommentArchDao.archive( comment );

			archiveAnswers( rootCommentsId );
		}

		photoCommentService.deletePhotoComments( photo.getId() );

		photo.setArchived( true );

		photoService.save( photo );
	}

	private void archiveAnswers( final int parentCommentsId ) {

		final List<PhotoComment> answers = photoCommentService.loadAnswersOnComment( parentCommentsId );

		for ( final PhotoComment answer : answers ) {
			answer.setId( 0 );
			answer.setReplyToCommentId( parentCommentsId );

			photoCommentArchDao.archive( answer );

			archiveAnswers( answer.getId() );
		}
	}

	@Override
	public Date getArchiveStartDate( final int days ) {
		return dateUtilsService.getFirstSecondOfTheDayNDaysAgo( days - 1 );
	}

	@Override
	public List<Integer> getNotArchivedPhotosIdsUploadedAtOrEarlieThen( final Date time ) {
		return archivingDao.getNotArchivedPhotosIdsUploadedAtOrEarlieThen( time );
	}

	public void setPhotoService( final PhotoService photoService ) {
		this.photoService = photoService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setPhotoCommentService( final PhotoCommentService photoCommentService ) {
		this.photoCommentService = photoCommentService;
	}

	public void setArchivingDao( final ArchivingDao archivingDao ) {
		this.archivingDao = archivingDao;
	}

	public void setPhotoCommentArchDao( final PhotoCommentDaoArchImpl photoCommentArchDao ) {
		this.photoCommentArchDao = photoCommentArchDao;
	}
}
