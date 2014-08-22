package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

public interface PhotoSqlHelperService {

	SqlIdsSelectQuery getAllPhotosForPageIdsSQL( final PagingModel pagingModel );

	SqlIdsSelectQuery getAllPhotosBestIdsSQL( final int minMarks, final Date timeFrom, final Date timeTo );

	SqlIdsSelectQuery getPortalPageLastUploadedPhotosSQL();

	SqlIdsSelectQuery getPortalPageBestPhotosIdsSQL( final int minMarksToBeInPhotoOfTheDay, final Date timeFrom );

	SqlIdsSelectQuery getPortalPageBestPhotosIdsSQL( final int minMarksToBeInPhotoOfTheDay, final Date timeFrom, final Date timeTo );

	SqlIdsSelectQuery getFavoritesPhotosSQL( final int userId, final FavoriteEntryType entryType, final int page, final int itemsOnPage );

	SqlIdsSelectQuery getPhotosOfUserFavoritesMembersSQL( final User user, final int page, final int itemsOnPage );

	SqlIdsSelectQuery getUserTeamMemberPhotosQuery( final User user, final UserTeamMember userTeamMember, final int page, final int itemsOnPage );

	SqlIdsSelectQuery getUserPhotoAlbumPhotosQuery( final User user, final UserPhotoAlbum userPhotoAlbum, final int page, final int itemsOnPage );
}
