package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.user.UserMembershipType;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlSelectQuery;

public interface UserSqlUtilsService {

	SqlSelectQuery getAllUsersForPageSQL( PagingModel pagingModel );

	SqlIdsSelectQuery getUserIdsForPageSQL( PagingModel pagingModel );

	SqlIdsSelectQuery getUsersByMembershipTypeSQL( UserMembershipType membershipType, PagingModel pagingModel );

	SqlIdsSelectQuery getUsersFavoritesSQL( PagingModel pagingModel, int userId, FavoriteEntryType entryType );

	SqlIdsSelectQuery getAddedToFavoritesBySQL( PagingModel pagingModel, int userId );
}
