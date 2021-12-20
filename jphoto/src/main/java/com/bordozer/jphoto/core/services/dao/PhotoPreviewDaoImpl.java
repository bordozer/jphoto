package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.dao.mappers.IdsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Component
public class PhotoPreviewDaoImpl extends BaseEntityDaoImpl<PhotoPreview> implements PhotoPreviewDao {

    public final static String TABLE_PHOTO_PREVIEW = "photoPreview";

    public final static String TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID = "photoId";
    public final static String TABLE_PHOTO_PREVIEW_COLUMN_USER_ID = "userId";
    public final static String TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME = "previewTime";

    @Autowired
    private UserDao userDao;

    @Autowired
    private PhotoDao photoDao;

    public static final Map<Integer, String> fields = newLinkedHashMap();

    static {
        fields.put(1, TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID);
        fields.put(2, TABLE_PHOTO_PREVIEW_COLUMN_USER_ID);
        fields.put(3, TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME);
    }

    @Override
    public boolean hasUserAlreadySeenThisPhoto(final int photoId, final int userId) {
        final String sql = String.format("SELECT 1 FROM %s WHERE %s=:photoId AND %s=:userId LIMIT 1;"
                , TABLE_PHOTO_PREVIEW, TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID, TABLE_PHOTO_PREVIEW_COLUMN_USER_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);
        paramSource.addValue("userId", userId);

        try {
            return jdbcTemplate.queryForObject(sql, paramSource, Integer.class) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public PhotoPreview load(final int photoId, final int userId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:photoId AND %s=:userId;"
                , TABLE_PHOTO_PREVIEW, TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID, TABLE_PHOTO_PREVIEW_COLUMN_USER_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);
        paramSource.addValue("userId", userId);

        return getEntryOrNull(sql, paramSource, new PhotoPreviewMapper());
    }

    @Override
    public void deletePreviews(final int photoId) {
        final String sql = String.format("DELETE FROM %s WHERE %s = :photoId;", TABLE_PHOTO_PREVIEW, TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);

        jdbcTemplate.update(sql, paramSource);
    }

    @Override
    public int getPreviewCount(final int photoId) {
        final String sql = String.format("SELECT COUNT( %s ) FROM %s WHERE %s=:photoId;", BaseEntityDao.ENTITY_ID, TABLE_PHOTO_PREVIEW, TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);

        return jdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }

    @Override
    public int getPreviewCount() {
        final String sql = String.format("SELECT COUNT( %s ) FROM %s;", BaseEntityDao.ENTITY_ID, TABLE_PHOTO_PREVIEW);

        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    @Override
    public List<PhotoPreview> getPreviews(final int photoId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:photoId ORDER BY %s DESC;"
                , TABLE_PHOTO_PREVIEW, TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID, TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);

        return jdbcTemplate.query(sql, paramSource, new PhotoPreviewMapper());
    }

    @Override
    public List<Integer> getLastUsersWhoViewedUserPhotos(final int userId, final int qty) {
        final String sql = String.format("SELECT DISTINCT pp.%s FROM %s pp WHERE pp.%s IN ( SELECT p.%s FROM %s p WHERE p.%s = :userId ) ORDER BY pp.%s DESC LIMIT %d;"
                , TABLE_PHOTO_PREVIEW_COLUMN_USER_ID, TABLE_PHOTO_PREVIEW, TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID, BaseEntityDao.ENTITY_ID
                , PhotoDaoImpl.TABLE_PHOTOS, PhotoDaoImpl.TABLE_COLUMN_USER_ID, TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME
                , qty
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);

        return jdbcTemplate.query(sql, paramSource, new IdsRowMapper());
    }

    @Override
    public boolean saveToDB(final PhotoPreview entry) {
        return createOrUpdateEntry(entry, fields, fields);
    }

    @Override
    protected MapSqlParameterSource getParameters(final PhotoPreview entry) {
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue(TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID, entry.getPhoto().getId());
        paramSource.addValue(TABLE_PHOTO_PREVIEW_COLUMN_USER_ID, entry.getUser().getId());
        paramSource.addValue(TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME, entry.getPreviewTime());

        return paramSource;
    }

    @Override
    protected String getTableName() {
        return TABLE_PHOTO_PREVIEW;
    }

    @Override
    protected RowMapper<PhotoPreview> getRowMapper() {
        return new PhotoPreviewMapper();
    }

    private class PhotoPreviewMapper implements RowMapper<PhotoPreview> {

        @Override
        public PhotoPreview mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final int photoId = rs.getInt(TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID);
            final int userId = rs.getInt(TABLE_PHOTO_PREVIEW_COLUMN_USER_ID);

            final Photo photo = photoDao.load(photoId);
            final User user = userDao.load(userId);

            final PhotoPreview result = new PhotoPreview(photo, user);
            result.setId(rs.getInt(BaseEntityDao.ENTITY_ID));
            result.setPreviewTime(rs.getTimestamp(TABLE_PHOTO_PREVIEW_COLUMN_PREVIEW_TIME));

            return result;
        }
    }
}
