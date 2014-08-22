package core.services.utils.sql;

import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.dao.PhotoDaoImpl;
import core.services.dao.UserDaoImpl;
import core.services.system.Services;
import core.services.utils.DateUtilsService;
import sql.builder.*;

import java.util.Date;

public class PhotoListQueryBuilder {

	private final Services services;
	private SqlIdsSelectQuery query = new SqlIdsSelectQuery( new SqlTable( PhotoDaoImpl.TABLE_PHOTOS ) );

	public PhotoListQueryBuilder( final Services services ) {
		this.services = services;
	}

	public PhotoListQueryBuilder filterByAuthor( final User user ) {
		getPhotoSqlFilterService().addFilterByUser( user.getId(), query );
		return this;
	}

	public PhotoListQueryBuilder filterByGenre( final Genre genre ) {
		getPhotoSqlFilterService().addFilterByGenre( genre.getId(), query );
		return this;
	}

	public PhotoListQueryBuilder filterByMembershipType( final UserMembershipType userMembershipType ) {
		final SqlTable tPhotos = query.getMainTable();
		final SqlTable tUsers = new SqlTable( UserDaoImpl.TABLE_USERS );

		final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tUsersColId = new SqlColumnSelect( tUsers, UserDaoImpl.ENTITY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColUserId, tUsersColId );
		final SqlJoin join = SqlJoin.inner( tUsers, joinCondition );
		query.joinTable( join );

		final SqlColumnSelectable tUsersColMemberType = new SqlColumnSelect( tUsers, UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE );
		final SqlLogicallyJoinable condition = new SqlCondition( tUsersColMemberType, SqlCriteriaOperator.EQUALS, userMembershipType.getId(), getDateUtilsService() );
		query.addWhereAnd( condition );

		return this;
	}

	public PhotoListQueryBuilder uploaded( final Date uploadTime ) {
		return this;
	}

	public PhotoListQueryBuilder uploaded( final Date from, final Date to ) {
		return this;
	}

	private DateUtilsService getDateUtilsService() {
		return services.getDateUtilsService();
	}

	private PhotoSqlFilterService getPhotoSqlFilterService() {
		return services.getPhotoSqlFilterService();
	}
}
