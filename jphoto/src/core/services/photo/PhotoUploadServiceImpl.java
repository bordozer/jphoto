package core.services.photo;

import core.general.photo.Photo;
import core.services.dao.PhotoDaoImpl;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoSqlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

import java.util.Date;
import java.util.List;

public class PhotoUploadServiceImpl implements PhotoUploadService {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private PhotoSqlFilterService photoSqlFilterService;

	@Override
	public List<Integer> getUploadedTodayPhotosIds( final int userId ) {
		return getPhotoIds( userId, dateUtilsService.getFirstSecondOfToday() );
	}

	@Override
	public List<Integer> getUploadedThisWeekPhotosIds( final int userId ) {
		return getPhotoIds( userId, dateUtilsService.getFirstSecondOfLastMonday() );
	}

	@Override
	public List<Photo> getUploadedThisWeekPhotos( final int userId ) {
		return photoService.load( getUploadedThisWeekPhotosIds( userId ) );
	}

	@Override
	public long getUploadedThisWeekPhotosSummarySize( final int userId ) {
		return getSummaryPhotoSize( getUploadedThisWeekPhotosIds( userId ) );
	}

	@Override
	public long getUploadedTodayPhotosSummarySize( final int userId ) {
		return getSummaryPhotoSize( getUploadedTodayPhotosIds( userId ) );
	}

	private List<Integer> getPhotoIds( final int userId, final Date haveingUploadTimeMoreThen ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		final SqlColumnSelectable tPhotoColUploadTime = new SqlColumnSelect( selectQuery.getMainTable(), PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		final SqlLogicallyJoinable condition = new SqlCondition( tPhotoColUploadTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, haveingUploadTimeMoreThen, dateUtilsService );
		selectQuery.setWhere( condition );

		photoSqlFilterService.addFilterByUser( userId, selectQuery );

		selectQuery.addSortingDesc( tPhotoColUploadTime );

		return photoService.load( selectQuery ).getIds();
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
