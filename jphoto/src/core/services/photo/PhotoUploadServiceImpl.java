package core.services.photo;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.PhotoListQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;
import java.util.List;

public class PhotoUploadServiceImpl implements PhotoUploadService {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public List<Integer> getUploadedTodayPhotosIds( final User user ) {
		return getPhotoIds( user, dateUtilsService.getFirstSecondOfToday() );
	}

	@Override
	public List<Integer> getUploadedThisWeekPhotosIds( final User user ) {
		return getPhotoIds( user, dateUtilsService.getFirstSecondOfLastMonday() );
	}

	@Override
	public List<Photo> getUploadedThisWeekPhotos( final User user ) {
		return photoService.load( getUploadedThisWeekPhotosIds( user ) );
	}

	@Override
	public long getUploadedThisWeekPhotosSummarySize( final User user ) {
		return getSummaryPhotoSize( getUploadedThisWeekPhotosIds( user ) );
	}

	@Override
	public long getUploadedTodayPhotosSummarySize( final User user ) {
		return getSummaryPhotoSize( getUploadedTodayPhotosIds( user ) );
	}

	private List<Integer> getPhotoIds( final User user, final Date uploadedSince ) {

		final SqlIdsSelectQuery query = new PhotoListQueryBuilder( dateUtilsService )
			.filterByAuthor( user )
			.filterByUploadTime( uploadedSince, dateUtilsService.getCurrentTime() )
			.sortByUploadTimeDesc()
			.getQuery();

		return photoService.load( query ).getIds();
	}

	private long getSummaryPhotoSize( final List<Integer> photoIds ) {
		long summaryPhotoSize = 0;
		for ( final int photoId : photoIds ) {
			final Photo photo = photoService.load( photoId );
			summaryPhotoSize += photo.getFileSize();
		}

		return summaryPhotoSize;
	}
}
