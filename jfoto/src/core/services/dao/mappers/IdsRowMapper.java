package core.services.dao.mappers;

import core.services.dao.BaseEntityDao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdsRowMapper implements RowMapper<Integer> {

	@Override
	public Integer mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
		return rs.getInt( 1 ); //BaseEntityDao.ENTITY_ID
	}
}
