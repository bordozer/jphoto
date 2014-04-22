package ui.services;

import core.enums.FavoriteEntryType;
import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.photo.PhotoPreviewWrapper;
import core.general.user.User;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;
import java.util.List;

public interface PhotoUIService {

	List<PhotoInfo> getPhotoInfos( final SqlIdsSelectQuery selectQuery, final User user );

	PhotoInfo getPhotoInfo( final Photo photo, final User accessor );

	PhotoInfo getPhotoInfo( final Photo photo, final Date timeFrom, final Date timeTo, final User accessor );

	List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final User accessor );

	List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final Date timeFrom, final Date timeTo, final User accessor );

	List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final List<FavoriteEntryType> photoIconsTypes, final List<FavoriteEntryType> userIconsTypes, final User accessor );

//	void hidePhotoPreviewForAnonymouslyPostedPhotos( final List<PhotoInfo> photoInfos );

	PhotoPreviewWrapper getPhotoPreviewWrapper( final Photo photo, final User user );
}
