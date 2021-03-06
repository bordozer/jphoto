package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.general.cache.CacheEntryFactory;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.cache.entries.UserGenrePhotosQty;
import com.bordozer.jphoto.core.general.cache.keys.UserGenreCompositeKey;
import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoImageLocationType;
import com.bordozer.jphoto.core.general.photo.PhotoImportData;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.dao.mappers.IdsRowMapper;
import com.bordozer.jphoto.core.services.system.CacheService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Component
public class PhotoDaoImpl extends BaseEntityDaoImpl<Photo> implements PhotoDao {

    public final static String TABLE_PHOTOS = "photos";

    public final static String TABLE_COLUMN_NAME = "name";
    public final static String TABLE_COLUMN_USER_ID = "userId";
    public final static String TABLE_COLUMN_GENRE_ID = "genreId";
    public final static String TABLE_COLUMN_KEYWORDS = "keywords";
    public final static String TABLE_COLUMN_DESCRIPTION = "description";
    public final static String TABLE_COLUMN_FILE_PHOTO_IMAGE_SOURCE = "photoImageSource";
    public final static String TABLE_COLUMN_PHOTO_PREVIEW_NAME = "photoPreviewName";
    public final static String TABLE_COLUMN_FILE_SIZE = "fileSize";
    public final static String TABLE_COLUMN_UPLOAD_TIME = "uploadTime";
    public final static String TABLE_COLUMN_CONTAINS_NUDE_CONTENT = "containsNudeContent";
    public final static String TABLE_COLUMN_BGCOLOR = "bgcolor";
    public final static String TABLE_COLUMN_COMMENTS_ALLOWANCE = "commentsAllowance";
    public final static String TABLE_COLUMN_EMAIL_ABOUT_NEW_COMMENT = "notificationEmailAboutNewPhotoComment";
    public final static String TABLE_COLUMN_VOTING_ALLOWANCE = "votingAllowance";
    public final static String TABLE_COLUMN_IS_ANONYMOUS_POSTING = "isAnonymousPosting";
    public final static String TABLE_COLUMN_USER_GENRE_RANK = "userGenreRank";
    public final static String TABLE_COLUMN_IMPORT_ID = "importId";
    public final static String TABLE_COLUMN_IMAGE_WIDTH = "image_width";
    public final static String TABLE_COLUMN_IMAGE_HEIGHT = "image_height";
    public final static String TABLE_COLUMN_IMAGE_LOCATION_TYPE_ID = "imageLocationTypeId";
    public final static String TABLE_COLUMN_IMAGE_SOURCE_ID = "imageSourceId";
    public final static String TABLE_COLUMN_IMPORT_DATA = "importData";
    public final static String TABLE_COLUMN_IS_ARCHIVED = "isArchived";

    public static final Map<Integer, String> fields = newLinkedHashMap();
    public static final Map<Integer, String> updatableFields = newLinkedHashMap();

    private static final String PHOTO_IMPORT_DATA_ROOT_ELEMENT = "photo";
    private static final String PHOTO_IMPORT_DATA_IMPORT_SOURCE_ID = "importSourceId";
    private static final String PHOTO_IMPORT_DATA_REMOTE_USER_ID = "remoteUserId";
    private static final String PHOTO_IMPORT_DATA_REMOTE_USER_NAME = "remoteUserName";
    private static final String PHOTO_IMPORT_DATA_REMOTE_PHOTO_ID = "remotePhotoId";

    @Autowired
    private CacheService<Photo> cacheService;

    @Autowired
    private CacheService<UserGenrePhotosQty> cacheServiceUserGenre;

    @Autowired
    private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    private final LogHelper log = new LogHelper();

    static {
        fields.put(1, TABLE_COLUMN_NAME);
        fields.put(2, TABLE_COLUMN_USER_ID);
        fields.put(3, TABLE_COLUMN_GENRE_ID);
        fields.put(4, TABLE_COLUMN_KEYWORDS);
        fields.put(5, TABLE_COLUMN_DESCRIPTION);
        fields.put(6, TABLE_COLUMN_FILE_PHOTO_IMAGE_SOURCE);
        fields.put(8, TABLE_COLUMN_UPLOAD_TIME);
        fields.put(9, TABLE_COLUMN_CONTAINS_NUDE_CONTENT);
        fields.put(10, TABLE_COLUMN_BGCOLOR);
        fields.put(11, TABLE_COLUMN_COMMENTS_ALLOWANCE);
        fields.put(12, TABLE_COLUMN_EMAIL_ABOUT_NEW_COMMENT);
        fields.put(14, TABLE_COLUMN_VOTING_ALLOWANCE);
        fields.put(15, TABLE_COLUMN_IS_ANONYMOUS_POSTING);
        fields.put(16, TABLE_COLUMN_USER_GENRE_RANK);
        fields.put(17, TABLE_COLUMN_IMPORT_ID);
        fields.put(18, TABLE_COLUMN_IMAGE_WIDTH);
        fields.put(19, TABLE_COLUMN_IMAGE_HEIGHT);
        fields.put(20, TABLE_COLUMN_IMAGE_LOCATION_TYPE_ID);
        fields.put(21, TABLE_COLUMN_FILE_SIZE);
        fields.put(22, TABLE_COLUMN_PHOTO_PREVIEW_NAME);
        fields.put(23, TABLE_COLUMN_IMAGE_SOURCE_ID);
        fields.put(24, TABLE_COLUMN_IMPORT_DATA);
        fields.put(25, TABLE_COLUMN_IS_ARCHIVED);
    }

    static {
        updatableFields.put(1, TABLE_COLUMN_NAME);
        updatableFields.put(3, TABLE_COLUMN_GENRE_ID);
        updatableFields.put(4, TABLE_COLUMN_KEYWORDS);
        updatableFields.put(5, TABLE_COLUMN_DESCRIPTION);
        updatableFields.put(9, TABLE_COLUMN_CONTAINS_NUDE_CONTENT);
        updatableFields.put(10, TABLE_COLUMN_BGCOLOR);
        updatableFields.put(11, TABLE_COLUMN_COMMENTS_ALLOWANCE);
        updatableFields.put(12, TABLE_COLUMN_EMAIL_ABOUT_NEW_COMMENT);
        updatableFields.put(14, TABLE_COLUMN_VOTING_ALLOWANCE);
        updatableFields.put(15, TABLE_COLUMN_IS_ANONYMOUS_POSTING);
        updatableFields.put(25, TABLE_COLUMN_IS_ARCHIVED);
    }

    @Override
    protected String getTableName() {
        return TABLE_PHOTOS;
    }

    @Override
    public boolean saveToDB(final Photo photo) {

        final boolean isSaved = createOrUpdateEntry(photo, fields, updatableFields);

        if (isSaved) {
            cacheService.expire(CacheKey.PHOTO, photo.getId());
        }

        return isSaved;
    }

    @Override
    public Photo load(final int entryId) {
        return cacheService.getEntry(CacheKey.PHOTO, entryId, new CacheEntryFactory<Photo>() {
            @Override
            public Photo createEntry() {
                return loadEntryById(entryId, new PhotoMapper());
            }
        });
    }

    @Override
    protected RowMapper<Photo> getRowMapper() {
        return new PhotoMapper();
    }

    @Override
    public int getPhotoQty() {
        final String sql = String.format("SELECT COUNT(id) FROM %s;", TABLE_PHOTOS);
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    @Override
    public int getPhotoQtyByGenre(final int genreId) {
        final String sql = String.format("SELECT COUNT(id) FROM %s WHERE %s=:genreId;", TABLE_PHOTOS, TABLE_COLUMN_GENRE_ID);
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("genreId", genreId), Integer.class);
    }

    @Override
    public int getPhotoQtyByUser(final int userId) {
        final String sql = String.format("SELECT COUNT(id) FROM %s WHERE %s=:userId;", TABLE_PHOTOS, TABLE_COLUMN_USER_ID);
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("userId", userId), Integer.class);
    }

    @Override
    public int getPhotoQtyByUserAndGenre(final int userId, final int genreId) {

        return cacheServiceUserGenre.getEntry(CacheKey.USER_GENRE_PHOTOS_QTY, new UserGenreCompositeKey(userId, genreId), new CacheEntryFactory<UserGenrePhotosQty>() {
            @Override
            public UserGenrePhotosQty createEntry() {
                return new UserGenrePhotosQty(userId, genreId, getUserGenrePhotosQty(userId, genreId));
            }
        }).getPhotosQty();
    }

    @Override
    protected MapSqlParameterSource getParameters(final Photo entry) {
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue(TABLE_COLUMN_NAME, entry.getName());
        paramSource.addValue(TABLE_COLUMN_USER_ID, entry.getUserId());
        paramSource.addValue(TABLE_COLUMN_GENRE_ID, entry.getGenreId());
        paramSource.addValue(TABLE_COLUMN_KEYWORDS, entry.getKeywords());
        paramSource.addValue(TABLE_COLUMN_DESCRIPTION, entry.getDescription());

        switch (entry.getPhotoImageLocationType()) {
            case FILE:
                if (entry.getPhotoImageFile() != null) {
                    paramSource.addValue(TABLE_COLUMN_FILE_PHOTO_IMAGE_SOURCE, entry.getPhotoImageFile().getName());
                }
                break;
            default:
                paramSource.addValue(TABLE_COLUMN_FILE_PHOTO_IMAGE_SOURCE, entry.getPhotoImageUrl());
                break;
        }

        paramSource.addValue(TABLE_COLUMN_FILE_SIZE, entry.getFileSize());
        paramSource.addValue(TABLE_COLUMN_PHOTO_PREVIEW_NAME, entry.getPhotoPreviewName());

        paramSource.addValue(TABLE_COLUMN_UPLOAD_TIME, entry.getUploadTime());

        paramSource.addValue(TABLE_COLUMN_CONTAINS_NUDE_CONTENT, entry.isContainsNudeContent());
        paramSource.addValue(TABLE_COLUMN_BGCOLOR, entry.getBgColor());
        paramSource.addValue(TABLE_COLUMN_COMMENTS_ALLOWANCE, entry.getCommentsAllowance().getId());
        paramSource.addValue(TABLE_COLUMN_EMAIL_ABOUT_NEW_COMMENT, entry.isNotificationEmailAboutNewPhotoComment());
        paramSource.addValue(TABLE_COLUMN_VOTING_ALLOWANCE, entry.getVotingAllowance().getId());

        paramSource.addValue(TABLE_COLUMN_IS_ANONYMOUS_POSTING, entry.isAnonymousPosting());
        paramSource.addValue(TABLE_COLUMN_USER_GENRE_RANK, entry.getUserGenreRank());
        paramSource.addValue(TABLE_COLUMN_IMPORT_ID, entry.getImportId());

        paramSource.addValue(TABLE_COLUMN_IMAGE_LOCATION_TYPE_ID, entry.getPhotoImageLocationType().getId());
        paramSource.addValue(TABLE_COLUMN_IMAGE_SOURCE_ID, entry.getPhotosImportSource().getId());

        final Dimension imageDimension = entry.getImageDimension();
        if (imageDimension != null) {
            paramSource.addValue(TABLE_COLUMN_IMAGE_WIDTH, imageDimension.getWidth());
            paramSource.addValue(TABLE_COLUMN_IMAGE_HEIGHT, imageDimension.getHeight());
        }

        paramSource.addValue(TABLE_COLUMN_IMPORT_DATA, toXml(entry.getPhotoImportData()));

        paramSource.addValue(TABLE_COLUMN_IS_ARCHIVED, entry.isArchived());

        return paramSource;
    }

    @Override
    public int getLastUserPhotoId(final int userId) {
        final String sql = String.format("SELECT %s FROM %s WHERE %s=:userId ORDER BY %s DESC LIMIT 1;", ENTITY_ID, TABLE_PHOTOS, TABLE_COLUMN_USER_ID, TABLE_COLUMN_UPLOAD_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);

        return getIntValueOrZero(sql, paramSource);
    }

    @Override
    public int getLastGenrePhotoId(final int genreId) {
        final String sql = String.format("SELECT %s FROM %s WHERE %s=:genreId ORDER BY %s DESC LIMIT 1;", ENTITY_ID, TABLE_PHOTOS, TABLE_COLUMN_GENRE_ID, TABLE_COLUMN_UPLOAD_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("genreId", genreId);

        return getIntValueOrZero(sql, paramSource);
    }

    @Override
    public List<Integer> getUserPhotosIds(final int userId) {
        final String sql = String.format("SELECT %s FROM %s WHERE %s=:userId;", BaseEntityDao.ENTITY_ID, TABLE_PHOTOS, TABLE_COLUMN_USER_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);

        return jdbcTemplate.query(sql, paramSource, new IdsRowMapper());
    }

    @Override
    public boolean isUserPhotoImported(final int userId, final int importId) {
        final String sql = String.format("SELECT 1 FROM %s WHERE %s=:userId AND %s=:importId LIMIT 1;", TABLE_PHOTOS, TABLE_COLUMN_USER_ID, TABLE_COLUMN_IMPORT_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("importId", importId);

        return hasEntry(sql, paramSource);
    }

    private int getUserGenrePhotosQty(final int userId, final int genreId) {
        final String sql = String.format("SELECT COUNT(id) FROM %s WHERE %s=:userId AND %s=:genreId;", TABLE_PHOTOS, TABLE_COLUMN_USER_ID, TABLE_COLUMN_GENRE_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource("userId", userId);
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);

        return jdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }

    private String toXml(final PhotoImportData photoImportData) {

        if (photoImportData == null) {
            return "";
        }

        final Document document = DocumentHelper.createDocument();
        final Element rootElement = document.addElement(PHOTO_IMPORT_DATA_ROOT_ELEMENT);

        rootElement.addElement(PHOTO_IMPORT_DATA_IMPORT_SOURCE_ID).addText(String.valueOf(photoImportData.getPhotosImportSource().getId()));
        rootElement.addElement(PHOTO_IMPORT_DATA_REMOTE_USER_ID).addText(String.valueOf(photoImportData.getRemoteUserId()));
        rootElement.addElement(PHOTO_IMPORT_DATA_REMOTE_USER_NAME).addText(String.valueOf(photoImportData.getRemoteUserName()));
        rootElement.addElement(PHOTO_IMPORT_DATA_REMOTE_PHOTO_ID).addText(String.valueOf(photoImportData.getRemotePhotoId()));

        return document.asXML();
    }

    private PhotoImportData fromXML(final String xml) {

        if (StringUtils.isEmpty(xml)) {
            return null;
        }

        final Document document;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (final DocumentException e) {
            log.error(String.format("Can npt parce XML: %s", xml), e);
            return null;
        }

        final Element rootElement = document.getRootElement();

        final PhotosImportSource photosImportSource = PhotosImportSource.getById(rootElement.element(PHOTO_IMPORT_DATA_IMPORT_SOURCE_ID).getText());
        final String remoteUserId = rootElement.element(PHOTO_IMPORT_DATA_REMOTE_USER_ID).getText();
        final String remoteUserName = rootElement.element(PHOTO_IMPORT_DATA_REMOTE_USER_NAME) != null ? rootElement.element(PHOTO_IMPORT_DATA_REMOTE_USER_NAME).getText() : "";
        final int remotePhotoId = Integer.parseInt(rootElement.element(PHOTO_IMPORT_DATA_REMOTE_PHOTO_ID).getText());

        return new PhotoImportData(photosImportSource, remoteUserId, remoteUserName, remotePhotoId);
    }

    private class PhotoMapper implements RowMapper<Photo> {
        @Override
        public Photo mapRow(final ResultSet rs, final int rowNum) throws SQLException {

            final Photo result = new Photo();

            result.setId(rs.getInt(BaseEntityDao.ENTITY_ID));

            result.setName(rs.getString(TABLE_COLUMN_NAME));

            result.setUserId(rs.getInt(TABLE_COLUMN_USER_ID));
            result.setGenreId(rs.getInt(TABLE_COLUMN_GENRE_ID));

            result.setKeywords(rs.getString(TABLE_COLUMN_KEYWORDS));
            result.setDescription(rs.getString(TABLE_COLUMN_DESCRIPTION));

            result.setPhotosImportSource(PhotosImportSource.getById(rs.getInt(TABLE_COLUMN_IMAGE_SOURCE_ID)));

            final PhotoImageLocationType photoImageLocationType = PhotoImageLocationType.getById(rs.getInt(TABLE_COLUMN_IMAGE_LOCATION_TYPE_ID));
            result.setPhotoImageLocationType(photoImageLocationType);

            final String fileName = rs.getString(TABLE_COLUMN_FILE_PHOTO_IMAGE_SOURCE);
            switch (photoImageLocationType) {
                case FILE:
                    final File file = new File(userPhotoFilePathUtilsService.getUserPhotoDir(rs.getInt(TABLE_COLUMN_USER_ID)), fileName);
                    result.setPhotoImageFile(file);
                    break;
                default:
                    result.setPhotoImageUrl(fileName);
            }
            result.setFileSize(rs.getLong(TABLE_COLUMN_FILE_SIZE));
            result.setPhotoPreviewName(rs.getString(TABLE_COLUMN_PHOTO_PREVIEW_NAME));

            result.setUploadTime(rs.getTimestamp(TABLE_COLUMN_UPLOAD_TIME));

            result.setContainsNudeContent(rs.getBoolean(TABLE_COLUMN_CONTAINS_NUDE_CONTENT));
            result.setBgColor(rs.getString(TABLE_COLUMN_BGCOLOR));

            result.setCommentsAllowance(PhotoActionAllowance.getById(rs.getInt(TABLE_COLUMN_COMMENTS_ALLOWANCE)));
            result.setNotificationEmailAboutNewPhotoComment(rs.getBoolean(TABLE_COLUMN_EMAIL_ABOUT_NEW_COMMENT));
            result.setVotingAllowance(PhotoActionAllowance.getById(rs.getInt(TABLE_COLUMN_VOTING_ALLOWANCE)));

            result.setAnonymousPosting(rs.getBoolean(TABLE_COLUMN_IS_ANONYMOUS_POSTING));
            result.setUserGenreRank(rs.getInt(TABLE_COLUMN_USER_GENRE_RANK));
            result.setImportId(rs.getInt(TABLE_COLUMN_IMPORT_ID));

            result.setImageDimension(new Dimension(rs.getInt(TABLE_COLUMN_IMAGE_WIDTH), rs.getInt(TABLE_COLUMN_IMAGE_HEIGHT)));

            result.setPhotoImportData(fromXML(rs.getString(TABLE_COLUMN_IMPORT_DATA)));

            result.setArchived(rs.getBoolean(TABLE_COLUMN_IS_ARCHIVED));

            return result;
        }
    }
}
