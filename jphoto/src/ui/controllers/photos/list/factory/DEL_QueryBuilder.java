package ui.controllers.photos.list.factory;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.dao.PhotoDaoImpl;
import core.services.system.Services;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlTable;

public class DEL_QueryBuilder {

	private final Services services;

	private final SqlIdsSelectQuery query = new SqlIdsSelectQuery( new SqlTable( PhotoDaoImpl.TABLE_PHOTOS ) );

	public DEL_QueryBuilder( final Services services ) {
		this.services = services;
	}

	public DEL_QueryBuilder filterByGenre( final Genre genre ) {
		services.getPhotoSqlFilterService().addFilterByGenre( genre.getId(), query );
		return this;
	}

	public DEL_QueryBuilder filterByUser( final User user ) {
		services.getPhotoSqlFilterService().addFilterByUser( user.getId(), query );
		return this;
	}

	public SqlIdsSelectQuery getQuery() {
		return query;
	}
}
