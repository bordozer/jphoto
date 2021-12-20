package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.exceptions.SaveToDBException;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photoTeam.PhotoTeam;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotosByGenre;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PhotoService extends BaseEntityService<Photo>, IdsSqlSelectable {

    void uploadNewPhoto(final Photo photo, final File photoFile, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums) throws SaveToDBException, IOException;

    void uploadNewPhoto(final Photo photo, final File photoImageFile, final String photoImageUrl, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums) throws SaveToDBException, IOException;

    void updatePhoto(final Photo photo, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums) throws SaveToDBException;

    List<Photo> load(final List<Integer> photoIds);

    int getPhotosCount();

    int getPhotosCountByGenre(final int genreId);

    int getPhotosCountByUser(final int userId);

    int getPhotosCountByUserAndGenre(final int userId, final int genreId);

    int getPhotosCountByGenreForPeriod(final Genre genre, final Date timeFrom, final Date timeTo);

    List<Integer> getUserPhotosIds(final int userId);

    Set<Genre> getUserPhotoGenres(final int userId);

    int getLastGenrePhotoId(final int genreId);

    Date getPhotoAnonymousPeriodExpirationTime(final Photo photo);

    PhotoActionAllowance getPhotoCommentAllowance(final Photo photo);

    PhotoActionAllowance getPhotoVotingAllowance(final Photo photo);

    List<UserPhotosByGenre> getUserPhotosByGenres(int userId);

    List<Integer> getLastVotedPhotosIds(final User user, final int photosQty, final User accessor);

    boolean movePhotoToGenreWithNotification(final int photoId, final int genreId, final User userWhoIsMoving);

    int getLastUserPhotoId(final int userId);

    boolean isUserPhotoImported(final int userId, final int importId);
}
