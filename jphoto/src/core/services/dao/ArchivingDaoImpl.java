package core.services.dao;

import core.services.dao.mappers.IdsRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Date;
import java.util.List;

public class ArchivingDaoImpl extends BaseDaoImpl implements ArchivingDao {


	@Override
	public void deletePhotosPreviewsOlderThen( final Date time ) {

		final String sql = String.format( "DELETE FROM %s WHERE %s <= :time;"
			, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW
			, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "time", time );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public List<Integer> getNotArchivedPhotosIdsUploadedAtOrEarlieThen( final Date time ) {

		final String sql = String.format( "SELECT %s FROM %s WHERE %s <= :time AND %s = 0;"
			, BaseEntityDao.ENTITY_ID
			, PhotoDaoImpl.TABLE_PHOTOS
			, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME
			, PhotoDaoImpl.TABLE_COLUMN_IS_ARCHIVED
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "time", time );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}
}
