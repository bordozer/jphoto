package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.enums.PhotoAwardKey;
import com.bordozer.jphoto.core.general.photo.PhotoAward;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class PhotoAwardDaoImpl extends BaseDaoImpl implements PhotoAwardDao {

    public final static String TABLE_PHOTO_AWARDS = "photoAwards";

    public final static String TABLE_PHOTO_AWARDS_COL_PHOTO_ID = "photoId";
    public final static String TABLE_PHOTO_AWARDS_COL_AWARD_ID = "awardId";
    public final static String TABLE_PHOTO_AWARDS_COL_TIME_FROM = "timeFrom";
    public final static String TABLE_PHOTO_AWARDS_COL_TIME_TO = "timeTo";

    @Override
    public boolean doesPhotoHaveThisAward(final PhotoAward photoAward) {
        final String sql = String.format("SELECT 1 FROM %s WHERE %s=:photoId AND %s=:awardId AND %s=:timeFrom AND %s=:timeTo;"
                , TABLE_PHOTO_AWARDS
                , TABLE_PHOTO_AWARDS_COL_PHOTO_ID
                , TABLE_PHOTO_AWARDS_COL_AWARD_ID
                , TABLE_PHOTO_AWARDS_COL_TIME_FROM
                , TABLE_PHOTO_AWARDS_COL_TIME_TO
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoAward.getPhotoId());
        paramSource.addValue("awardId", photoAward.getAwardKey().getId());
        paramSource.addValue("timeFrom", photoAward.getTimeFrom());
        paramSource.addValue("timeTo", photoAward.getTimeTo());


        return hasEntry(sql, paramSource);
    }

    @Override
    public void savePhotoAward(final PhotoAward photoAward) {
        final String sql = String.format("INSERT INTO %s ( %s, %s, %s, %s) VALUES( :photoId, :awardId, :timeFrom, :timeTo );"
                , TABLE_PHOTO_AWARDS
                , TABLE_PHOTO_AWARDS_COL_PHOTO_ID
                , TABLE_PHOTO_AWARDS_COL_AWARD_ID
                , TABLE_PHOTO_AWARDS_COL_TIME_FROM
                , TABLE_PHOTO_AWARDS_COL_TIME_TO
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoAward.getPhotoId());
        paramSource.addValue("awardId", photoAward.getAwardKey().getId());
        paramSource.addValue("timeFrom", photoAward.getTimeFrom());
        paramSource.addValue("timeTo", photoAward.getTimeTo());

        jdbcTemplate.update(sql, paramSource);
    }

    @Override
    public List<PhotoAward> getPhotoAwards(final int photoId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:photoId;"
                , TABLE_PHOTO_AWARDS
                , TABLE_PHOTO_AWARDS_COL_PHOTO_ID
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);

        return jdbcTemplate.query(sql, paramSource, new PhotoAwardMapper());
    }

    @Override
    public void deletePhotoAwards(final int photoId) {
        final String sql = String.format("DELETE FROM %s WHERE %s=:photoId;"
                , TABLE_PHOTO_AWARDS
                , TABLE_PHOTO_AWARDS_COL_PHOTO_ID
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);

        jdbcTemplate.update(sql, paramSource);
    }

    private class PhotoAwardMapper implements RowMapper<PhotoAward> {

        @Override
        public PhotoAward mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final PhotoAward result = new PhotoAward(rs.getInt(TABLE_PHOTO_AWARDS_COL_PHOTO_ID));

            result.setAwardKey(PhotoAwardKey.getById(rs.getInt(TABLE_PHOTO_AWARDS_COL_AWARD_ID)));
            result.setTimeFrom(rs.getTimestamp(TABLE_PHOTO_AWARDS_COL_TIME_FROM));
            result.setTimeTo(rs.getTimestamp(TABLE_PHOTO_AWARDS_COL_TIME_TO));

            return result;
        }
    }
}
