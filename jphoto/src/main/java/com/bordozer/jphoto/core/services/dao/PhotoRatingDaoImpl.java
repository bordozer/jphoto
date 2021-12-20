package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.data.PhotoRating;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Component
public class PhotoRatingDaoImpl extends BaseDaoImpl implements PhotoRatingDao {

    public final static String TABLE_PHOTO_RATINGS = "photoRatings";

    public final static String TABLE_PHOTO_RATINGS_COL_PHOTO_ID = "photoId";
    public final static String TABLE_PHOTO_RATINGS_COL_TIME_FROM = "timeFrom";
    public final static String TABLE_PHOTO_RATINGS_COL_TIME_TO = "timeTo";
    public final static String TABLE_PHOTO_RATINGS_COL_RATING_POSITION = "ratingPosition";
    public final static String TABLE_PHOTO_RATINGS_COL_SUMMARY_MARK = "summaryMark";

    @Override
    public void savePhotoRatingForPeriod(final PhotoRating photoRating) {
        final String sql = String.format("INSERT INTO %s ( %s,  %s, %s, %s, %s ) VALUES ( :photoId, :timeFrom, :timeTo, :ratingPosition, :marks );"
                , TABLE_PHOTO_RATINGS
                , TABLE_PHOTO_RATINGS_COL_PHOTO_ID
                , TABLE_PHOTO_RATINGS_COL_TIME_FROM
                , TABLE_PHOTO_RATINGS_COL_TIME_TO
                , TABLE_PHOTO_RATINGS_COL_RATING_POSITION
                , TABLE_PHOTO_RATINGS_COL_SUMMARY_MARK
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoRating.getPhotoId());
        paramSource.addValue("timeFrom", photoRating.getTimeFrom());
        paramSource.addValue("timeTo", photoRating.getTimeTo());
        paramSource.addValue("ratingPosition", photoRating.getRatingPosition());
        paramSource.addValue("marks", photoRating.getSummaryMark());

        jdbcTemplate.update(sql, paramSource);
    }

    @Override
    public PhotoRating getPhotoRatingsForPeriod(final int photoId, final Date timeFrom, final Date timeTo) {

        final String sql = String.format("SELECT * FROM %s WHERE %s=:photoId AND %s=:timeFrom AND %s=:timeTo ORDER BY %s;"
                , TABLE_PHOTO_RATINGS, TABLE_PHOTO_RATINGS_COL_PHOTO_ID, TABLE_PHOTO_RATINGS_COL_TIME_FROM, TABLE_PHOTO_RATINGS_COL_TIME_TO, TABLE_PHOTO_RATINGS_COL_RATING_POSITION);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);
        paramSource.addValue("timeFrom", timeFrom);
        paramSource.addValue("timeTo", timeTo);

        return getEntryOrNull(sql, paramSource, new PhotoRatingMapper());
    }

    @Override
    public void deletePhotosRatingsForPeriod(final Date timeFrom, final Date timeTo) {
        final String sql = String.format("DELETE FROM %s WHERE %s=:timeFrom AND %s=:timeTo;"
                , TABLE_PHOTO_RATINGS, TABLE_PHOTO_RATINGS_COL_TIME_FROM, TABLE_PHOTO_RATINGS_COL_TIME_TO);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("timeFrom", timeFrom);
        paramSource.addValue("timeTo", timeTo);

        jdbcTemplate.update(sql, paramSource);
    }

    @Override
    public List<PhotoRating> getPhotosRatingsForPeriod(final Date timeFrom, final Date timeTo) {

        final String sql = String.format("SELECT * FROM %s WHERE %s=:timeFrom AND %s=:timeTo ORDER BY %s;"
                , TABLE_PHOTO_RATINGS, TABLE_PHOTO_RATINGS_COL_TIME_FROM, TABLE_PHOTO_RATINGS_COL_TIME_TO, TABLE_PHOTO_RATINGS_COL_RATING_POSITION);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("timeFrom", timeFrom);
        paramSource.addValue("timeTo", timeTo);

        return jdbcTemplate.query(sql, paramSource, new PhotoRatingMapper());
    }

    @Override
    public void deletePhotoRatings(final int photoId) {
        final String sql = String.format("DELETE FROM %s WHERE %s=:photoId;", TABLE_PHOTO_RATINGS, TABLE_PHOTO_RATINGS_COL_PHOTO_ID);

        jdbcTemplate.update(sql, new MapSqlParameterSource("photoId", photoId));
    }

    private class PhotoRatingMapper implements RowMapper<PhotoRating> {

        @Override
        public PhotoRating mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final PhotoRating result = new PhotoRating();

            result.setPhotoId(rs.getInt(TABLE_PHOTO_RATINGS_COL_PHOTO_ID));
            result.setTimeFrom(rs.getTimestamp(TABLE_PHOTO_RATINGS_COL_TIME_FROM));
            result.setTimeTo(rs.getTimestamp(TABLE_PHOTO_RATINGS_COL_TIME_TO));
            result.setRatingPosition(rs.getInt(TABLE_PHOTO_RATINGS_COL_RATING_POSITION));
            result.setSummaryMark(rs.getInt(TABLE_PHOTO_RATINGS_COL_SUMMARY_MARK));

            return result;
        }
    }
}
