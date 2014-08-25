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
import sql.builder.SqlIdsSelectQuery;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PhotoService extends BaseEntityService<Photo>, IdsSqlSelectable {

	void uploadNewPhoto( final Photo photo, final File photoFile, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException, IOException;

	void uploadNewPhoto( final Photo photo, final File photoImageFile, final String photoImageUrl, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException, IOException;

	void updatePhoto( final Photo photo, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException;

	List<Photo> load( final List<Integer> photoIds );

	int getPhotosCount();

	int getPhotosCountByGenre( final int genreId );

	int getPhotosCountByUser( final int userId );

	int getPhotosCountByUserAndGenre( final int userId, final int genreId );

	int getPhotosCountByGenreForPeriod( final Genre genre, final Date timeFrom, final Date timeTo );

	List<Integer> getUserPhotosIds( final int userId );

	Set<Genre> getUserPhotoGenres( final int userId );

	int getLastGenrePhotoId( final int genreId );

	Date getPhotoAnonymousPeriodExpirationTime( final Photo photo );

	PhotoActionAllowance getPhotoCommentAllowance( final Photo photo );

	PhotoActionAllowance getPhotoVotingAllowance( final Photo photo );

	List<UserPhotosByGenre> getUserPhotosByGenres( int userId );

	List<Integer> getLastVotedPhotosIds( final User user, final int photosQty, final User accessor );

	boolean movePhotoToGenreWithNotification( final int photoId, final int genreId, final User userWhoIsMoving );

	List<Photo> getUserPhotos( final int userId );

	List<Photo> loadPhotosByIdsQuery( final SqlIdsSelectQuery selectQuery );

	int getLastUserPhotoId( final int userId );
}
