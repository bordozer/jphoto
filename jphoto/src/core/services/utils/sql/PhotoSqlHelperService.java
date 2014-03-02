package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

public interface PhotoSqlHelperService {

	SqlIdsSelectQuery getAllPhotosForPageIdsSQL( final PagingModel pagingModel );

	SqlIdsSelectQuery getAllPhotosBestIdsSQL( final int minMarks, final Date timeFrom, final Date timeTo );

	SqlIdsSelectQuery getPortalPageLastUploadedPhotosSQL();

	SqlIdsSelectQuery getPortalPageBestPhotosIdsSQL( final int minMarksToBeInPhotoOfTheDay, final Date timeFrom );

	SqlIdsSelectQuery getPortalPageBestPhotosIdsSQL( final int minMarksToBeInPhotoOfTheDay, final Date timeFrom, final Date timeTo );

	SqlIdsSelectQuery getFavoritesPhotosSQL( final PagingModel pagingModel, final int userId, final FavoriteEntryType entryType );

	SqlIdsSelectQuery getUserTeamMemberLastPhotosQuery( final int userId, final int userTeamMemberId, final PagingModel pagingModel );

	SqlIdsSelectQuery getUserPhotoAlbumLastPhotosQuery( final int userId, final int photoAlbumId, final PagingModel pagingModel );
}
