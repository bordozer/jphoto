package mocks;

import core.enums.PhotoActionAllowance;
import core.exceptions.SaveToDBException;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.user.User;
import core.general.cache.entries.UserPhotosByGenresEntry;
import core.general.photo.PhotoPreviewWrapper;
import core.general.photoTeam.PhotoTeam;
import core.general.user.UserPhotosByGenre;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.photo.PhotoService;
import sql.SqlSelectIdsResult;
import sql.SqlSelectResult;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlSelectQuery;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class PhotoServiceMock implements PhotoService {

	@Override
	public void savePhotoWithTeamAndAlbums( final Photo photo, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException {
	}

	@Override
	public SqlSelectResult<Photo> load( final SqlSelectQuery selectQuery ) {
		return null;
	}

	@Override
	public List<Photo> load( final List<Integer> photoIds ) {
		return null;
	}

	@Override
	public List<Photo> loadPhotosByIdsQuery( final SqlIdsSelectQuery selectQuery ) {
		return null;
	}

	@Override
	public List<Photo> loadUserPhotos( final int userId ) {
		return null;
	}

	@Override
	public UserPhotosByGenresEntry getUserPhotosByGenresEntry( final User user, final User votingUser  ) {
		return null;
	}

	@Override
	public int getPhotoQty() {
		return 0;
	}

	@Override
	public int getPhotoQtyByGenre( final int genreId ) {
		return 0;
	}

	@Override
	public int getPhotoQtyByUser( final int userId ) {
		return 0;
	}

	@Override
	public int getPhotoQtyByUserAndGenre( final int userId, final int genreId ) {
		return 0;
	}

	@Override
	public Set<Genre> getUserPhotoGenres( final int userId ) {
		return null;
	}

	@Override
	public PhotoInfo getPhotoInfo( final Photo photo, final User accessor ) {
		return null;
	}

	@Override
	public PhotoInfo getPhotoInfo( final Photo photo, final Date timeFrom, final Date timeTo, final User accessor ) {
		return null;
	}

	@Override
	public List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final User accessor ) {
		return null;
	}

	@Override
	public List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final Date timeFrom, final Date timeTo, final User accessor ) {
		return null;
	}

	@Override
	public boolean updatePhotoFileData( final int photoId, final File file ) {
		return false;
	}

	@Override
	public int getPhotoQtyByGenreForPeriod( final int genreId, final Date timeFrom, final Date timeTo ) {
		return 0;
	}

	@Override
	public Photo getLastUserPhoto( final int userId ) {
		return null;
	}

	@Override
	public boolean isPhotoAuthorNameMustBeHidden( final Photo photo, final User accessor ) {
		return false;
	}

	@Override
	public Date getPhotoAnonymousPeriodExpirationTime( final Photo photo ) {
		return new Date( 0 );
	}

	@Override
	public void hidePhotoPreviewForAnonymouslyPostedPhotos( final List<PhotoInfo> photoInfos ) {
	}

	@Override
	public PhotoActionAllowance getPhotoCommentAllowance( final Photo photo ) {
		return null;
	}

	@Override
	public PhotoActionAllowance getPhotoVotingAllowance( final Photo photo ) {
		return null;
	}

	@Override
	public List<UserPhotosByGenre> getUserPhotosByGenres( final int userId ) {
		return null;
	}

	@Override
	public PhotoPreviewWrapper getPhotoPreviewWrapper( final Photo photo, final User user ) {
		return null;
	}

	@Override
	public boolean save( final Photo entry ) {
		return false;
	}

	@Override
	public Photo load( final int id ) {
		return null;
	}

	@Override
	public boolean delete( final int entryId ) {
		return false;
	}

	@Override
	public boolean exists( final int entryId ) {
		return false;
	}

	@Override
	public boolean exists( final Photo entry ) {
		return false;
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return null;
	}

	@Override
	public List<Integer> getUserPhotosIds( final int userId ) {
		return null;
	}

	@Override
	public List<Photo> getUserPhotos( final int userId ) {
		return null;
	}

	@Override
	public List<Photo> getBestUserPhotos( final User user, final int photosQty, final User accessor ) {
		return null;
	}

	@Override
	public List<Photo> getLastUserPhotos( final User user, final int photosQty, final User accessor ) {
		return null;
	}

	@Override
	public List<Photo> getLastVotedPhotos( final User user, final int photosQty, final User accessor ) {
		return null;
	}

	@Override
	public List<Photo> getLastPhotosOfUserVisitors( final User user, final int photosQty ) {
		return null;
	}
}
