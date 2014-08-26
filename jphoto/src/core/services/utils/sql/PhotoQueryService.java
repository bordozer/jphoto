package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.user.UserMembershipType;
import sql.builder.SqlIdsSelectQuery;

public interface PhotoQueryService {

	SqlIdsSelectQuery getUserIdsForPageSQL( final PagingModel pagingModel );

	SqlIdsSelectQuery getUsersByMembershipTypeSQL( final UserMembershipType membershipType, final PagingModel pagingModel );

	SqlIdsSelectQuery getUsersFavoritesSQL( final PagingModel pagingModel, final int userId, final FavoriteEntryType entryType );

	SqlIdsSelectQuery getAddedToFavoritesBySQL( final PagingModel pagingModel, final int userId );
}
