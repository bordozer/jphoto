package admin.services.jobs;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.services.entry.AnonymousDaysService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.utils.DateUtilsService;
import core.services.utils.RandomUtilsService;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoListQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;

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
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private SecurityService securityService;

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
	public boolean getAnonymousOption( final int userId, final int genreId, final Date uploadTime ) {
		final boolean forceAnonymousPosting = securityService.forceAnonymousPosting( userId, genreId, uploadTime );

		return forceAnonymousPosting || randomUtilsService.getRandomIntegerArrayElement( new int[] {0, 0, 0, 1} ) == 1;

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
		return photoService.isUserPhotoImported( userId, importId );
	}

	private SqlIdsSelectQuery getFirstPhotoUploadTimeSQL() {
		return new PhotoListQueryBuilder( dateUtilsService ).sortByUploadTimeAsc().forPage( 1, 1 ).getQuery();
	}
}
