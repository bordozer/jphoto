package core.services.photo;

import core.enums.PhotoActionAllowance;
import core.exceptions.SaveToDBException;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photoTeam.PhotoTeam;
import core.general.user.User;
import core.general.user.UserPhotosByGenre;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;
import sql.SqlSelectResult;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlSelectQuery;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PhotoService extends BaseEntityService<Photo>, IdsSqlSelectable {

	void uploadNewPhoto( final Photo photo, final File photoFile, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException, IOException;

	void save( final Photo photo, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException;

	@Deprecated
	SqlSelectResult<Photo> load( final SqlSelectQuery selectQuery );

	List<Photo> load( final List<Integer> photoIds );

	List<Photo> loadPhotosByIdsQuery( final SqlIdsSelectQuery selectQuery );

	List<Photo> loadUserPhotos( final int userId );

	int getPhotoQty();

	int getPhotoQtyByGenre( final int genreId );

	int getPhotoQtyByUser( final int userId );

	List<Photo> getUserPhotos( final int userId );

	List<Integer> getUserPhotosIds( final int userId );

	int getPhotoQtyByUserAndGenre( final int userId, final int genreId );

	Set<Genre> getUserPhotoGenres( final int userId );

	boolean updatePhotoFileData( int photoId, final File file );

	int getPhotoQtyByGenreForPeriod( final int genreId, final Date timeFrom, final Date timeTo );

	int getLastUserPhotoId( final int userId );

	Date getPhotoAnonymousPeriodExpirationTime( final Photo photo );

	PhotoActionAllowance getPhotoCommentAllowance( final Photo photo );

	PhotoActionAllowance getPhotoVotingAllowance( final Photo photo );

	List<UserPhotosByGenre> getUserPhotosByGenres( int userId );

	List<Integer> getBestUserPhotosIds( final User user, final int photosQty, final User accessor );

	List<Integer> getLastUserPhotosIds( final User user, final int photosQty, final User accessor );

	List<Integer> getLastVotedPhotosIds( final User user, final int photosQty, final User accessor );

	List<Integer> getLastPhotosOfUserVisitors( final User user, final int photosQty );
}
