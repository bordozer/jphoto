package core.services.photo;

import core.enums.FavoriteEntryType;
import core.enums.PhotoActionAllowance;
import core.exceptions.SaveToDBException;
import core.general.cache.entries.UserPhotosByGenresContainer;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.user.User;
import core.general.photo.PhotoPreviewWrapper;
import core.general.photoTeam.PhotoTeam;
import core.general.user.UserPhotosByGenre;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;
import sql.SqlSelectResult;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlSelectQuery;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PhotoService extends BaseEntityService<Photo>, IdsSqlSelectable {

	void savePhotoWithTeamAndAlbums( final Photo photo, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException;

	@Deprecated
	SqlSelectResult<Photo> load( final SqlSelectQuery selectQuery );

	List<Photo> load( final List<Integer> photoIds );

	List<Photo> loadPhotosByIdsQuery( final SqlIdsSelectQuery selectQuery );

	List<Photo> loadUserPhotos( final int userId );

	UserPhotosByGenresContainer getUserPhotosByGenresEntry( final User user, final User votingUser );

	int getPhotoQty();

	int getPhotoQtyByGenre( final int genreId );

	int getPhotoQtyByUser( final int userId );

	List<Photo> getUserPhotos( final int userId );

	List<Integer> getUserPhotosIds( final int userId );

	int getPhotoQtyByUserAndGenre( final int userId, final int genreId );

	Set<Genre> getUserPhotoGenres( final int userId );

	PhotoInfo getPhotoInfo( final Photo photo, final User accessor );

	PhotoInfo getPhotoInfo( final Photo photo, final Date timeFrom, final Date timeTo, final User accessor );

	List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final User accessor );

	List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final Date timeFrom, final Date timeTo, final User accessor );

	List<PhotoInfo> getPhotoInfos( List<Photo> photos, List<FavoriteEntryType> showIconsForFavoriteEntryTypes, User currentUser );

	boolean updatePhotoFileData( int photoId, final File file );

	int getPhotoQtyByGenreForPeriod( final int genreId, final Date timeFrom, final Date timeTo );

	Photo getLastUserPhoto( final int userId );

	boolean isPhotoAuthorNameMustBeHidden( final Photo photo, final User accessor );

	Date getPhotoAnonymousPeriodExpirationTime( final Photo photo );

	void hidePhotoPreviewForAnonymouslyPostedPhotos( final List<PhotoInfo> photoInfos );

	PhotoActionAllowance getPhotoCommentAllowance( final Photo photo );

	PhotoActionAllowance getPhotoVotingAllowance( final Photo photo );

	List<UserPhotosByGenre> getUserPhotosByGenres( int userId );

	PhotoPreviewWrapper getPhotoPreviewWrapper( final Photo photo, final User user );

	List<Photo> getBestUserPhotos( final User user, final int photosQty, final User accessor );

	List<Photo> getLastUserPhotos( final User user, final int photosQty, final User accessor );

	List<Photo> getLastVotedPhotos( final User user, final int photosQty, final User accessor );

	List<Photo> getLastPhotosOfUserVisitors( final User user, final int photosQty );
}
