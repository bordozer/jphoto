package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.controllers.jobs.edit.NoParametersAbstractJob;
import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.services.services.UpgradeDaoImpl;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.dao.BaseEntityDao;
import com.bordozer.jphoto.core.services.dao.ConfigurationDaoImpl;
import com.bordozer.jphoto.core.services.dao.GenreDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoAwardDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoCommentDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoPreviewDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoRatingDaoImpl;
import com.bordozer.jphoto.core.services.dao.PhotoVotingDaoImpl;
import com.bordozer.jphoto.core.services.dao.SavedJobDaoImpl;
import com.bordozer.jphoto.core.services.dao.SchedulerTasksDaoImpl;
import com.bordozer.jphoto.core.services.dao.UserDaoImpl;
import com.bordozer.jphoto.core.services.dao.UserPhotoAlbumDaoImpl;
import com.bordozer.jphoto.core.services.dao.UserRankDaoImpl;
import com.bordozer.jphoto.core.services.dao.UserTeamMemberDaoImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class ReindexJob extends NoParametersAbstractJob {

    public ReindexJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {
        performUpgrade();
    }

    private List<String> tables = newArrayList();
    private List<UTIndex> indexes = newArrayList();
    private Map<String, List<UTConstraint>> constraintsMap = newHashMap();

    {
        tables.add(UserDaoImpl.TABLE_USERS);
        tables.add(PhotoDaoImpl.TABLE_PHOTOS);
        tables.add(GenreDaoImpl.TABLE_GENRES);
        tables.add(PhotoCommentDaoImpl.TABLE_COMMENTS);

        tables.add(ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION);
        tables.add(ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION_KEYS);

        tables.add(SavedJobDaoImpl.TABLE_JOBS);
        tables.add(SavedJobDaoImpl.TABLE_JOB_PARAMETERS);

        tables.add(PhotoVotingDaoImpl.TABLE_VOTING_CATEGORIES);
        tables.add(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING);
        tables.add(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY);

        tables.add(SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS);
        tables.add(SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS_PROPERTIES);

        tables.add(UserRankDaoImpl.TABLE_USERS_RANKS_BY_GENRES);
        tables.add(UserRankDaoImpl.TABLE_USERS_RANK_BY_GENRES_VOTING);

        tables.add(UpgradeDaoImpl.TABLE_UPGRADE);
        tables.add(PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW);
        tables.add(PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS);
        tables.add(PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS);

        tables.add(UserTeamMemberDaoImpl.TABLE_USER_TEAM);
        tables.add(UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM);

        tables.add(UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM);
        tables.add(UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS);
    }

    {
        indexes.add(new UTIndex(UserDaoImpl.TABLE_USERS));
        indexes.add(new UTIndex(UserDaoImpl.TABLE_USERS, UserDaoImpl.TABLE_COLUMN_NAME, true));
        indexes.add(new UTIndex(UserDaoImpl.TABLE_USERS, UserDaoImpl.TABLE_COLUMN_LOGIN, true));
        indexes.add(new UTIndex(UserDaoImpl.TABLE_USERS, UserDaoImpl.TABLE_COLUMN_EMAIL, true));

        indexes.add(new UTIndex(PhotoDaoImpl.TABLE_PHOTOS));
        indexes.add(new UTIndex(PhotoDaoImpl.TABLE_PHOTOS, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME));

        indexes.add(new UTIndex(GenreDaoImpl.TABLE_GENRES));
        indexes.add(new UTIndex(GenreDaoImpl.TABLE_GENRES, GenreDaoImpl.TABLE_COLUMN_NAME, true));

        indexes.add(new UTIndex(PhotoCommentDaoImpl.TABLE_COMMENTS));

        indexes.add(new UTIndex(ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION));

        indexes.add(new UTIndex(ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION_KEYS));
        indexes.add(new UTIndex(ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION_KEYS, Lists.<String>newArrayList(ConfigurationDaoImpl.TABLE_CONFIGURATION_COLUMN_SYSTEM_CONFIGURATION_ID, ConfigurationDaoImpl.TABLE_CONFIGURATION_COLUMN_CONFIGURATION_KEY_ID), true));

        indexes.add(new UTIndex(SavedJobDaoImpl.TABLE_JOBS));
        indexes.add(new UTIndex(SavedJobDaoImpl.TABLE_JOB_PARAMETERS));

        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_VOTING_CATEGORIES));
        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_VOTING_CATEGORIES, PhotoVotingDaoImpl.TABLE_COLUMN_NAME, true));
        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING));
        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME, false));
        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_USER_ID));
        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_PHOTO_ID));
        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY));
        indexes.add(new UTIndex(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY, Lists.<String>newArrayList(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY_CATEGORY_ID), true));

        indexes.add(new UTIndex(SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS));
        indexes.add(new UTIndex(SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS, SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS_COLUMN_NAME, true));
        indexes.add(new UTIndex(SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS_PROPERTIES));

        indexes.add(new UTIndex(UserRankDaoImpl.TABLE_USERS_RANKS_BY_GENRES));
        indexes.add(new UTIndex(UserRankDaoImpl.TABLE_USERS_RANK_BY_GENRES_VOTING));

        indexes.add(new UTIndex(UpgradeDaoImpl.TABLE_UPGRADE));
        indexes.add(new UTIndex(PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW));
        indexes.add(new UTIndex(PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_PHOTO_ID));
        //		indexes.add( new UTIndex( PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW, PhotoPreviewDaoImpl.TABLE_PHOTO_PREVIEW_COLUMN_USER_ID ) );

        indexes.add(new UTIndex(PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS, PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS_COL_PHOTO_ID));
        indexes.add(new UTIndex(PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS, Lists.<String>newArrayList(PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS_COL_TIME_FROM, PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS_COL_TIME_TO)));

        indexes.add(new UTIndex(PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS));
        indexes.add(new UTIndex(PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS, PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS_COL_PHOTO_ID));
        indexes.add(new UTIndex(PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS, Lists.<String>newArrayList(PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS_COL_PHOTO_ID, PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS_COL_AWARD_ID, PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS_COL_TIME_FROM, PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS_COL_TIME_TO)));

        indexes.add(new UTIndex(UserTeamMemberDaoImpl.TABLE_USER_TEAM));
        indexes.add(new UTIndex(UserTeamMemberDaoImpl.TABLE_USER_TEAM, UserTeamMemberDaoImpl.TABLE_USER_TEAM_COL_USER_ID));
        indexes.add(new UTIndex(UserTeamMemberDaoImpl.TABLE_USER_TEAM, UserTeamMemberDaoImpl.TABLE_USER_TEAM_COL_TEAM_MEMBER_USER_ID));
        indexes.add(new UTIndex(UserTeamMemberDaoImpl.TABLE_USER_TEAM, Lists.<String>newArrayList(UserTeamMemberDaoImpl.TABLE_USER_TEAM_COL_USER_ID, UserTeamMemberDaoImpl.TABLE_USER_TEAM_COL_TEAM_MEMBER_NAME), true));

        indexes.add(new UTIndex(UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_PHOTO_ID));
        indexes.add(new UTIndex(UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID));

        indexes.add(new UTIndex(UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM));
        indexes.add(new UTIndex(UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM, UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM_COL_NAME, true));
        indexes.add(new UTIndex(UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS, Lists.<String>newArrayList(UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_PHOTO_ID, UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_ALBUM_ID), true));
    }

    {
        constraintsMap.put(
                PhotoCommentDaoImpl.TABLE_COMMENTS, newArrayList(new UTConstraint(PhotoCommentDaoImpl.TABLE_COMMENTS, PhotoCommentDaoImpl.TABLE_COLUMN_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(PhotoCommentDaoImpl.TABLE_COMMENTS, PhotoCommentDaoImpl.TABLE_COLUMN_AUTHOR_ID, UserDaoImpl.TABLE_USERS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                PhotoDaoImpl.TABLE_PHOTOS, newArrayList(new UTConstraint(PhotoDaoImpl.TABLE_PHOTOS, PhotoDaoImpl.TABLE_COLUMN_GENRE_ID, GenreDaoImpl.TABLE_GENRES, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(PhotoDaoImpl.TABLE_PHOTOS, PhotoDaoImpl.TABLE_COLUMN_USER_ID, UserDaoImpl.TABLE_USERS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION_KEYS, newArrayList(new UTConstraint(ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION_KEYS, ConfigurationDaoImpl.TABLE_CONFIGURATION_COLUMN_SYSTEM_CONFIGURATION_ID, ConfigurationDaoImpl.TABLE_SYSTEM_CONFIGURATION, BaseEntityDao.ENTITY_ID, "fk_systemConfigurationId_systemConfiguration_id")
                )
        );

        constraintsMap.put(
                SavedJobDaoImpl.TABLE_JOB_PARAMETERS, newArrayList(new UTConstraint(SavedJobDaoImpl.TABLE_JOB_PARAMETERS, SavedJobDaoImpl.TABLE_JOBS_PARAMS_COLUMN_JOB_ID, SavedJobDaoImpl.TABLE_JOBS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                PhotoVotingDaoImpl.TABLE_PHOTO_VOTING, newArrayList(new UTConstraint(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_USER_ID, UserDaoImpl.TABLE_USERS, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID, PhotoVotingDaoImpl.TABLE_VOTING_CATEGORIES, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY, newArrayList(new UTConstraint(PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS, newArrayList(new UTConstraint(SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS, SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS_COLUMN_JOB_ID, SavedJobDaoImpl.TABLE_JOBS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS_PROPERTIES, newArrayList(new UTConstraint(SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS_PROPERTIES, SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_TASK_ID, SchedulerTasksDaoImpl.TABLE_SCHEDULER_TASKS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                UserRankDaoImpl.TABLE_USERS_RANKS_BY_GENRES, newArrayList(new UTConstraint(UserRankDaoImpl.TABLE_USERS_RANKS_BY_GENRES, UserRankDaoImpl.TABLE_URBG_COLUMN_USER_ID, UserDaoImpl.TABLE_USERS, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(UserRankDaoImpl.TABLE_USERS_RANKS_BY_GENRES, UserRankDaoImpl.TABLE_URBG_COLUMN_GENRE_ID, GenreDaoImpl.TABLE_GENRES, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                UserRankDaoImpl.TABLE_USERS_RANK_BY_GENRES_VOTING, newArrayList(new UTConstraint(UserRankDaoImpl.TABLE_USERS_RANK_BY_GENRES_VOTING, UserRankDaoImpl.TABLE_COLUMN_USER_ID, UserDaoImpl.TABLE_USERS, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(UserRankDaoImpl.TABLE_USERS_RANK_BY_GENRES_VOTING, UserRankDaoImpl.TABLE_COLUMN_GENRE_ID, GenreDaoImpl.TABLE_GENRES, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(UserRankDaoImpl.TABLE_USERS_RANK_BY_GENRES_VOTING, UserRankDaoImpl.TABLE_COLUMN_VOTER_ID, UserDaoImpl.TABLE_USERS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS, newArrayList(new UTConstraint(PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS, PhotoRatingDaoImpl.TABLE_PHOTO_RATINGS_COL_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS, newArrayList(new UTConstraint(PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS, PhotoAwardDaoImpl.TABLE_PHOTO_AWARDS_COL_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM, newArrayList(new UTConstraint(UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID, UserTeamMemberDaoImpl.TABLE_USER_TEAM, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM, newArrayList(new UTConstraint(UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM, UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM_COL_USER_ID, UserDaoImpl.TABLE_USERS, BaseEntityDao.ENTITY_ID)
                )
        );

        constraintsMap.put(
                UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS, newArrayList(new UTConstraint(UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS, UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, BaseEntityDao.ENTITY_ID)
                        , new UTConstraint(UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS, UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_ALBUM_ID, UserPhotoAlbumDaoImpl.TABLE_USER_PHOTO_ALBUM, BaseEntityDao.ENTITY_ID)
                )
        );
    }

    private void performUpgrade() {

        for (final String table : tables) {

            getLog().debug(String.format("Recreating indexes: %s", table));

            final List<String> tableConstraintNames = getTableConstraintNames(table);

            dropTableConstraints(table, tableConstraintNames);

            final List<String> tableIndexesNames = getTableIndexesNames(table);

            dropTableIndexes(table, tableIndexesNames);

            final List<UTConstraint> tableConstraintsToCreate = getTableConstraintsToCreate(table);
            createConstraint(tableConstraintsToCreate);

            final List<UTIndex> utIndexes = getTableIndexesToCreate(table);
            createIndexes(utIndexes);

            increment();

            if (isFinished()) {
                break;
            }

            if (hasJobFinishedWithAnyResult()) {
                break;
            }
        }
    }

    private void dropTableConstraints(final String table, List<String> tableConstraintNames) {

        for (final String constraintName : tableConstraintNames) {
            if (constraintName.equals("PRIMARY")) {
                continue;
            }
            final String sql = String.format("ALTER TABLE %s DROP FOREIGN KEY `%s`;", table, constraintName);
            services.getSqlUtilsService().execSQL(sql);

            getLog().debug(String.format("Constraints '%s.%s' is dropped ( %s )", table, constraintName, sql));
        }
    }

    private void dropTableIndexes(final String table, final List<String> tableIndexesNames) {
        for (final String indexName : tableIndexesNames) {
            if (indexName.equals("PRIMARY")) {
                continue;
            }
            final String sql = String.format("DROP INDEX `%s` ON %s", indexName, table);
            try {
                services.getSqlUtilsService().execSQL(sql);
            } catch (Throwable t) {

            }

            getLog().debug(String.format("Index '%s.%s' is dropped ( %s )", table, indexName, sql));
        }
    }

    private List<UTConstraint> getTableConstraintsToCreate(final String table) {
        final List<UTConstraint> constraints = newArrayList();
        for (final String key : constraintsMap.keySet()) {
            if (key.equals(table)) {
                final List<UTConstraint> utConstraints = constraintsMap.get(key);
                for (final UTConstraint utConstraint : utConstraints) {
                    constraints.add(utConstraint);
                }
            }
        }

        return constraints;
    }

    private void createConstraint(final List<UTConstraint> constraints) {
        for (final UTConstraint constraint : constraints) {
            final String constraintName = constraint.getName();
            final String sql = String.format("ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s(%s) "
                    , constraint.getTableSource(), constraintName, constraint.getColumnSource(), constraint.getTableTarget(), constraint.getColumnTarget());

            services.getSqlUtilsService().execSQL(sql);

            getLog().debug(String.format("Constraints '%s' ( %s.%s -> %s.%s ) is created ( %s  )"
                    , constraintName, constraint.getTableSource(), constraint.getColumnSource(), constraint.getTableTarget(), constraint.getColumnTarget(), sql));
        }
    }

    private List<UTIndex> getTableIndexesToCreate(final String table) {
        final List<UTIndex> indexes = newArrayList();
        for (final UTIndex index : this.indexes) {
            if (index.getTable().equals(table)) {
                indexes.add(index);
            }
        }

        return indexes;
    }

    private void createIndexes(final List<UTIndex> utIndexes) {
        for (final UTIndex index : utIndexes) {
            final String indexName = index.getDbName();
            final String sql = String.format("CREATE %s INDEX %s ON %s (%s) USING BTREE;"
                    , (index.isUnique() ? "UNIQUE" : ""), indexName, index.getTable(), index.getIndexColumns());

            services.getSqlUtilsService().execSQL(sql);

            getLog().debug(String.format("Constraints %s ( %s.%s ) is created %s ( %s )", indexName, index.getIndexColumns(), index.getTable(), (index.isUnique() ? " ( UNIQUE )" : ""), sql));
        }
    }

    private List<String> getTableConstraintNames(final String table) {
        final String sql = String.format("SELECT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE CONSTRAINT_SCHEMA = '%s' AND TABLE_NAME ='%s' AND REFERENCED_TABLE_SCHEMA IS NOT NULL;",
                "", // TODO: set name
                table);

        return services.getSqlUtilsService().query(sql, new MapSqlParameterSource(), new TableConstraintMapper());
    }

    private List<String> getTableIndexesNames(final String table) {
        final String sql = String.format("SHOW INDEX FROM %s.%s;", "", table); // TODO: set datanase name

        return services.getSqlUtilsService().query(sql, new MapSqlParameterSource(), new TableIndexMapper());
    }

    private class UTConstraint {

        private final String tableSource;
        private final String columnSource;

        private final String tableTarget;
        private final String columnTarget;

        private final String name;

        private UTConstraint(final String tableSource, final String columnSource, final String tableTarget, final String columnTarget, final String name) {
            this.tableSource = tableSource;
            this.columnSource = columnSource;
            this.tableTarget = tableTarget;
            this.columnTarget = columnTarget;
            this.name = name;
        }

        private UTConstraint(final String tableSource, final String columnSource, final String tableTarget, final String columnTarget) {
            this.columnTarget = columnTarget;
            this.tableTarget = tableTarget;
            this.columnSource = columnSource;
            this.tableSource = tableSource;
            this.name = getDbName();
        }

        public String getTableSource() {
            return tableSource;
        }

        public String getColumnSource() {
            return columnSource;
        }

        public String getTableTarget() {
            return tableTarget;
        }

        public String getColumnTarget() {
            return columnTarget;
        }

        public String getName() {
            return name;
        }

        private String getDbName() {
            return String.format("fk_%s_%s_%s_%s", tableSource, columnSource, tableTarget, columnTarget);
        }
    }

    private class UTIndex {
        private final String table;
        private final List<String> indexColumns;
        private final boolean isUnique;

        private UTIndex(final String table, final List<String> indexColumns) {
            this(table, indexColumns, false);
        }

        private UTIndex(final String table, final List<String> indexColumns, final boolean unique) {
            this.table = table;
            this.indexColumns = indexColumns;
            isUnique = unique;
        }

        private UTIndex(final String table, final String indexColumn, final boolean unique) {
            this.table = table;
            this.indexColumns = newArrayList(indexColumn);
            isUnique = unique;
        }

        private UTIndex(final String table, final String indexColumn) {
            this(table, indexColumn, false);
        }

        public UTIndex(final String table) {
            this(table, BaseEntityDao.ENTITY_ID, true);
        }

        public String getTable() {
            return table;
        }

        public String getIndexColumns() {
            return StringUtils.join(indexColumns, ",");
        }

        public boolean isUnique() {
            return isUnique;
        }

        public String getDbName() {
            return String.format("idx_%s", StringUtils.join(indexColumns, "_"));
        }
    }

    private class TableConstraintMapper implements RowMapper<String> {

        @Override
        public String mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return rs.getString("CONSTRAINT_NAME");
        }
    }

    private class TableIndexMapper implements RowMapper<String> {

        @Override
        public String mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return rs.getString("Key_name");
        }
    }

    public int getTablesCount() {
        return tables.size();
    }

    @Override
    public SavedJobType getJobType() {
        return SavedJobType.REINDEX;
    }
}
