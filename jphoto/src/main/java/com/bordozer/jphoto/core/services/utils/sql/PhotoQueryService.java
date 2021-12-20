package com.bordozer.jphoto.core.services.utils.sql;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;

public interface PhotoQueryService {

    SqlIdsSelectQuery getUserIdsForPageSQL(final PagingModel pagingModel);

    SqlIdsSelectQuery getUsersByMembershipTypeSQL(final UserMembershipType membershipType, final PagingModel pagingModel);

    SqlIdsSelectQuery getUsersFavoritesSQL(final PagingModel pagingModel, final int userId, final FavoriteEntryType entryType);

    SqlIdsSelectQuery getAddedToFavoritesBySQL(final PagingModel pagingModel, final int userId);
}
