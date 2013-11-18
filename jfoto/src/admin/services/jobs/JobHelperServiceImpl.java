package admin.services.jobs;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.services.entry.AnonymousDaysService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.dao.PhotoDaoImpl;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoSqlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import core.services.utils.DateUtilsService;
import core.services.utils.RandomUtilsService;

import java.util.Date;
import java.util.List;

public class JobHelperServiceImpl implements JobHelperService {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private AnonymousDaysService anonymousDaysService;

	@Autowired
	private RandomUtilsService randomUtilsService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private DateUtilsService dateUtilsService;
	
	@Autowired
	private PhotoSqlFilterService photoSqlFilterService;
	
	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Override
	public Date getFirstPhotoUploadTime() {
		final SqlIdsSelectQuery selectQuery = getFirstPhotoUploadTimeSQL();

		final SqlSelectIdsResult photoIds = photoService.load( selectQuery );

		final List<Integer> photoIdsIds = photoIds.getIds();

		if ( photoIdsIds.size() == 0 ) {
			return dateUtilsService.getDatesOffsetFromCurrentDate( -365 );
		}

		final Photo theFirstUploadedPhoto = photoService.load( photoIdsIds.get( 0 ) );
		return theFirstUploadedPhoto.getUploadTime();
	}

	@Override
	public Date getFirstPhotoUploadTime( final int userId ) {
		final SqlIdsSelectQuery selectQuery = getFirstPhotoUploadTimeSQL();
		photoSqlFilterService.addFilterByUser( userId, selectQuery );

		final SqlSelectIdsResult photoIds = photoService.load( selectQuery );

		final List<Integer> photoIdsIds = photoIds.getIds();

		if ( photoIdsIds.size() == 0 ) {
			return dateUtilsService.getDatesOffsetFromCurrentDate( -365 );
		}

		final Photo theFirstUploadedPhoto = photoService.load( photoIdsIds.get( 0 ) );
		return theFirstUploadedPhoto.getUploadTime();
	}

	@Override
	public boolean getAnonymousOption( final Date uploadTime ) {
		final boolean isPhotoUploadDayAnonymous = anonymousDaysService.isDayAnonymous( uploadTime );

		return isPhotoUploadDayAnonymous || randomUtilsService.getRandomIntegerArrayElement( new int[] {0, 0, 0, 1} ) == 1;

	}

	@Override
	public void initPhotoNudeContentOption( final Photo photo ) {
		final int[] arrayOfProbabilityOfNudeContent = {0, 0, 0, 0, 0, 1};

		final Genre genre = genreService.load( photo.getGenreId() );
		if ( genre.isContainsNudeContent() ) {
			photo.setContainsNudeContent( true );
		} else if ( genre.isCanContainNudeContent() ) {
			photo.setContainsNudeContent( randomUtilsService.getRandomIntegerArrayElement( arrayOfProbabilityOfNudeContent ) == 1 );
		}
	}

	@Override
	public boolean doesUserPhotoExist( final int userId, final int importId ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();
		photoSqlFilterService.addFilterByUser( userId, selectQuery );

		final SqlColumnSelectable tPhotoColName = new SqlColumnSelect( selectQuery.getMainTable(), PhotoDaoImpl.TABLE_COLUMN_IMPORT_ID );
		final SqlLogicallyJoinable condition = new SqlCondition( tPhotoColName, SqlCriteriaOperator.EQUALS, importId, dateUtilsService );
		selectQuery.addWhereAnd( condition );

		List<Integer> ids = photoService.load( selectQuery ).getIds();

		return ids != null && ids.size() > 0;
	}

	private SqlIdsSelectQuery getFirstPhotoUploadTimeSQL() {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		final SqlColumnSelectable sort = new SqlColumnSelect( selectQuery.getMainTable(), PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		selectQuery.addSortingAsc( sort );
		selectQuery.setLimit( 1 );
		return selectQuery;
	}
}
