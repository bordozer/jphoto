package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.cache.CacheEntryFactory;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.dao.mappers.IdsRowMapper;
import com.bordozer.jphoto.core.services.system.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Component
public class UserPhotoAlbumDaoImpl extends BaseEntityDaoImpl<UserPhotoAlbum> implements UserPhotoAlbumDao {

    public final static String TABLE_USER_PHOTO_ALBUM = "userPhotoAlbum";

    public final static String TABLE_USER_PHOTO_ALBUM_COL_USER_ID = "userId";
    public final static String TABLE_USER_PHOTO_ALBUM_COL_NAME = "name";
    public final static String TABLE_USER_PHOTO_ALBUM_COL_DESCRIPTION = "description";

    public static final String TABLE_PHOTO_ALBUMS = "photoAlbums";

    public static final String TABLE_PHOTO_ALBUMS_COL_PHOTO_ID = "photoId";
    public static final String TABLE_PHOTO_ALBUMS_COL_ALBUM_ID = "photoAlbumId";

    private static final Map<Integer, String> fields = newLinkedHashMap();

    @Autowired
    private UserDao userDao;

    @Autowired
    private CacheService<UserPhotoAlbum> cacheService;

    private final LogHelper log = new LogHelper();

    static {
        fields.put(1, TABLE_USER_PHOTO_ALBUM_COL_USER_ID);
        fields.put(2, TABLE_USER_PHOTO_ALBUM_COL_NAME);
        fields.put(3, TABLE_USER_PHOTO_ALBUM_COL_DESCRIPTION);
    }

    @Override
    protected String getTableName() {
        return TABLE_USER_PHOTO_ALBUM;
    }

    @Override
    public boolean saveToDB(final UserPhotoAlbum photoAlbum) {
        final boolean isSaved = createOrUpdateEntry(photoAlbum, fields, fields);

        if (isSaved) {
            cacheService.expire(CacheKey.USER_PHOTO_ALBUM, photoAlbum.getId());
        }

        return isSaved;
    }

    @Override
    public UserPhotoAlbum load(final int photoAlbumId) {
        return cacheService.getEntry(CacheKey.USER_PHOTO_ALBUM, photoAlbumId, new CacheEntryFactory<UserPhotoAlbum>() {
            @Override
            public UserPhotoAlbum createEntry() {
                return loadEntryById(photoAlbumId, new UserPhotoAlbumMapper());
            }
        });
    }

    @Override
    protected RowMapper<UserPhotoAlbum> getRowMapper() {
        return new UserPhotoAlbumMapper();
    }

    @Override
    public boolean delete(final int entryId) {
        final boolean isDeleted = deleteEntryById(entryId);

        if (isDeleted) {
            cacheService.expire(CacheKey.USER_PHOTO_ALBUM, entryId);
        }

        return isDeleted;
    }

    @Override
    protected MapSqlParameterSource getParameters(final UserPhotoAlbum entry) {
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue(TABLE_USER_PHOTO_ALBUM_COL_USER_ID, entry.getUser().getId());
        paramSource.addValue(TABLE_USER_PHOTO_ALBUM_COL_NAME, entry.getName());
        paramSource.addValue(TABLE_USER_PHOTO_ALBUM_COL_DESCRIPTION, entry.getDescription());

        return paramSource;
    }

    @Override
    public List<Integer> loadAlbumPhotoIds(int albumId) {
        final String sql = String.format("SELECT p.%1$s FROM %2$s p WHERE p.%1$s IN ( SELECT pa.%4$s FROM %3$s pa WHERE pa.%5$s = :albumId )"
                , ENTITY_ID
                , PhotoDaoImpl.TABLE_PHOTOS
                , TABLE_PHOTO_ALBUMS
                , TABLE_PHOTO_ALBUMS_COL_PHOTO_ID
                , TABLE_PHOTO_ALBUMS_COL_ALBUM_ID
        );

        return jdbcTemplate.query(sql, new MapSqlParameterSource("albumId", albumId), new IdsRowMapper());
    }

    @Override
    public List<UserPhotoAlbum> loadAllUserPhotoAlbums(final int userId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s= :userId", TABLE_USER_PHOTO_ALBUM, TABLE_USER_PHOTO_ALBUM_COL_USER_ID);

        return jdbcTemplate.query(sql, new MapSqlParameterSource("userId", userId), new UserPhotoAlbumMapper());
    }

    @Override
    public void deletePhotoFromAllAlbums(final int photoId) {
        final String sql = String.format("DELETE FROM %s WHERE %s= :photoId", TABLE_PHOTO_ALBUMS, TABLE_PHOTO_ALBUMS_COL_PHOTO_ID);

        jdbcTemplate.update(sql, new MapSqlParameterSource("photoId", photoId));
    }

    @Override
    public boolean savePhotoAlbums(final Photo photo, final List<UserPhotoAlbum> photoAlbums) {

        final int photoId = photo.getId();

        deletePhotoFromAllAlbums(photoId);

        for (final UserPhotoAlbum photoAlbum : photoAlbums) {
            final String sql = String.format("INSERT INTO %s( %s, %s ) VALUES( :photoId, :albumId );"
                    , TABLE_PHOTO_ALBUMS
                    , TABLE_PHOTO_ALBUMS_COL_PHOTO_ID
                    , TABLE_PHOTO_ALBUMS_COL_ALBUM_ID
            );

            final MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource.addValue("photoId", photoId);
            paramSource.addValue("albumId", photoAlbum.getId());

            if (jdbcTemplate.update(sql, paramSource) == 0) {
                deletePhotoFromAllAlbums(photoId);
                log.error(String.format("Error adding photo to album. Photo Id: %d, albumId: %d", photoId, photoAlbum.getId()));

                return false;
            }
        }

        return true;
    }

    @Override
    public List<UserPhotoAlbum> loadPhotoAlbums(final int photoId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:photoId;"
                , TABLE_PHOTO_ALBUMS
                , TABLE_PHOTO_ALBUMS_COL_PHOTO_ID
        );

        return jdbcTemplate.query(sql, new MapSqlParameterSource("photoId", photoId), new PhotoAlbumMapper());
    }

    @Override
    public UserPhotoAlbum loadPhotoAlbumByName(final int userId, final String albumName) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:userId AND %s=:name;"
                , TABLE_USER_PHOTO_ALBUM
                , TABLE_USER_PHOTO_ALBUM_COL_USER_ID
                , TABLE_USER_PHOTO_ALBUM_COL_NAME
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("name", albumName);

        return getEntryOrNull(sql, paramSource, new UserPhotoAlbumMapper());
    }

    @Override
    public int getUserPhotoAlbumPhotosQty(final int userPhotoAlbumId) {
        final String sql = String.format("SELECT COUNT(%s) FROM %s WHERE %s=:userPhotoAlbumId;"
                , TABLE_PHOTO_ALBUMS_COL_PHOTO_ID
                , TABLE_PHOTO_ALBUMS
                , TABLE_PHOTO_ALBUMS_COL_ALBUM_ID
        );

        return getEntryOrNull(sql, new MapSqlParameterSource("userPhotoAlbumId", userPhotoAlbumId), new IdsRowMapper());
    }

    @Override
    public boolean isPhotoInAlbum(final int photoId, final int photoAlbumId) {
        final String sql = String.format("SELECT 1 FROM %s WHERE %s=:photoId AND %s = :photoAlbumId;"
                , TABLE_PHOTO_ALBUMS
                , TABLE_PHOTO_ALBUMS_COL_PHOTO_ID
                , TABLE_PHOTO_ALBUMS_COL_ALBUM_ID
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);
        paramSource.addValue("photoAlbumId", photoAlbumId);

        return existsInt(sql, paramSource);
    }

    @Override
    public boolean addPhotoToAlbum(final int photoId, final int photoAlbumId) {
        final String sql = String.format("INSERT INTO %s ( %s, %s ) VALUES ( :photoId, :photoAlbumId );"
                , TABLE_PHOTO_ALBUMS
                , TABLE_PHOTO_ALBUMS_COL_PHOTO_ID
                , TABLE_PHOTO_ALBUMS_COL_ALBUM_ID
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);
        paramSource.addValue("photoAlbumId", photoAlbumId);

        return jdbcTemplate.update(sql, paramSource) > 0;
    }

    @Override
    public boolean deletePhotoFromAlbum(final int photoId, final int photoAlbumId) {
        final String sql = String.format("DELETE FROM %s WHERE %s=:photoId AND %s = :photoAlbumId;"
                , TABLE_PHOTO_ALBUMS
                , TABLE_PHOTO_ALBUMS_COL_PHOTO_ID
                , TABLE_PHOTO_ALBUMS_COL_ALBUM_ID
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("photoId", photoId);
        paramSource.addValue("photoAlbumId", photoAlbumId);

        return jdbcTemplate.update(sql, paramSource) > 0;
    }

    private class UserPhotoAlbumMapper implements RowMapper<UserPhotoAlbum> {

        @Override
        public UserPhotoAlbum mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final UserPhotoAlbum result = new UserPhotoAlbum();

            result.setId(rs.getInt(ENTITY_ID));
            result.setUser(userDao.load(rs.getInt(TABLE_USER_PHOTO_ALBUM_COL_USER_ID)));
            result.setName(rs.getString(TABLE_USER_PHOTO_ALBUM_COL_NAME));
            result.setDescription(rs.getString(TABLE_USER_PHOTO_ALBUM_COL_DESCRIPTION));

            return result;
        }
    }

    private class PhotoAlbumMapper implements RowMapper<UserPhotoAlbum> {

        @Override
        public UserPhotoAlbum mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return load(rs.getInt(TABLE_PHOTO_ALBUMS_COL_ALBUM_ID));
        }
    }
}
