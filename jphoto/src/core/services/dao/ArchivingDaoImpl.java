package core.services.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Date;

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
}
