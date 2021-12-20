package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.cache.CacheEntryFactory;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.cache.keys.UserGenreCompositeKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserGenreRank;
import com.bordozer.jphoto.core.general.user.UserGenreRankHistoryEntry;
import com.bordozer.jphoto.core.general.user.UserRankInGenreVoting;
import com.bordozer.jphoto.core.general.user.UserRankPhotoVote;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.system.CacheService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class UserRankDaoImpl extends BaseDaoImpl implements UserRankDao {

    public final static String TABLE_USERS_RANK_BY_GENRES_VOTING = "usersRanksByGenresVoting";
    public final static String TABLE_COLUMN_USER_ID = "userId";
    public final static String TABLE_COLUMN_VOTER_ID = "voterId";
    public final static String TABLE_COLUMN_GENRE_ID = "genreId";
    public final static String TABLE_COLUMN_RANK = "userRankWhenVoting";
    public final static String TABLE_COLUMN_POINTS = "points";
    public final static String TABLE_COLUMN_VOTING_TIME = "votingtime";

    public final static String TABLE_USERS_RANKS_BY_GENRES = "usersRanksByGenres";
    public final static String TABLE_URBG_COLUMN_USER_ID = "userId";
    public final static String TABLE_URBG_COLUMN_GENRE_ID = "genreId";
    public final static String TABLE_URBG_COLUMN_USER_RANK_WHEN_VOTING = "rank";

    public final static String TABLE_USERS_RANKS_HISTORY = "usersRankHistory";
    public final static String TABLE_URH_COLUMN_USER_ID = "userId";
    public final static String TABLE_URH_COLUMN_GENRE_ID = "genreId";
    public final static String TABLE_URH_COLUMN_RANK = "rank";
    public final static String TABLE_URH_COLUMN_ASSIGN_TIME = "assignTime";

    @Autowired
    private UserDao userDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private CacheService<UserGenreRank> cacheService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Override
    public boolean saveVotingForUserRankInGenre(final UserRankInGenreVoting rankInGenreVoting) {
        final String sql = String.format("INSERT INTO %s ( %s, %s, %s, %s, %s, %s ) VALUES( :userId, :voterId, :genreId, :rank, :points, :votingtime );", TABLE_USERS_RANK_BY_GENRES_VOTING, TABLE_COLUMN_USER_ID, TABLE_COLUMN_VOTER_ID, TABLE_COLUMN_GENRE_ID, TABLE_COLUMN_RANK, TABLE_COLUMN_POINTS, TABLE_COLUMN_VOTING_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", rankInGenreVoting.getUserId());
        paramSource.addValue("voterId", rankInGenreVoting.getVoterId());
        paramSource.addValue("genreId", rankInGenreVoting.getGenreId());
        paramSource.addValue("rank", rankInGenreVoting.getUserRankWhenVoting());
        paramSource.addValue("points", rankInGenreVoting.getPoints());
        paramSource.addValue("votingtime", rankInGenreVoting.getVotingTime());

        return jdbcTemplate.update(sql, paramSource) > 0;
    }

    @Override
    public boolean isUserVotedLastTimeForThisRankInGenre(final int voterId, final int forUserId, final int genreId, final int rank) {
        final String sql = String.format("SELECT 1 FROM %s WHERE %s=:userId AND %s=:voterId AND %s=:genreId AND %s=:rank ORDER BY %s DESC LIMIT 1", TABLE_USERS_RANK_BY_GENRES_VOTING, TABLE_COLUMN_USER_ID, TABLE_COLUMN_VOTER_ID, TABLE_COLUMN_GENRE_ID, TABLE_COLUMN_RANK, TABLE_COLUMN_VOTING_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", forUserId);
        paramSource.addValue("voterId", voterId);
        paramSource.addValue("genreId", genreId);
        paramSource.addValue("rank", rank);

        return hasEntry(sql, paramSource);
    }

    @Override
    public int getUserRankInGenre(final int userId, final int genreId) {
        final UserGenreCompositeKey compositeKey = new UserGenreCompositeKey(userId, genreId);

        return cacheService.getEntry(CacheKey.USER_GENRE_RANK, compositeKey, new CacheEntryFactory<UserGenreRank>() {
            @Override
            public UserGenreRank createEntry() {
                final UserGenreRank userGenreRank = new UserGenreRank(userId, genreId);
                userGenreRank.setUserRankInGenre(loadUserRankInGenre(userId, genreId));
                return userGenreRank;
            }
        }).getUserRankInGenre();
    }

    @Override
    public boolean saveUserRankForGenre(final int userId, final int genreId, final int rank) {

        final String updateSql = String.format("UPDATE %1$s SET %4$s = :rank WHERE %2$s = :userId AND %3$s = :genreId;"
                , TABLE_USERS_RANKS_BY_GENRES, TABLE_URBG_COLUMN_USER_ID, TABLE_URBG_COLUMN_GENRE_ID, TABLE_URBG_COLUMN_USER_RANK_WHEN_VOTING);

        if (doesUsersRanksByGenresRecordExist(userId, genreId)) {
            return doSelect(userId, genreId, rank, updateSql);
        }

        synchronized (this) {
            if (doesUsersRanksByGenresRecordExist(userId, genreId)) {
                return doSelect(userId, genreId, rank, updateSql);
            }

            final String insertSql = String.format("INSERT INTO %s ( %s, %s, %s ) VALUES( :userId, :genreId, :rank );"
                    , TABLE_USERS_RANKS_BY_GENRES, TABLE_URBG_COLUMN_USER_ID, TABLE_URBG_COLUMN_GENRE_ID, TABLE_URBG_COLUMN_USER_RANK_WHEN_VOTING);

            return doSelect(userId, genreId, rank, insertSql);
        }
    }

    private boolean doSelect(final int userId, final int genreId, final int rank, final String sql) {
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);
        paramSource.addValue("rank", rank);

        boolean isSuccessful = jdbcTemplate.update(sql, paramSource) > 0;

        if (isSuccessful) {
            cacheService.expire(CacheKey.USER_GENRE_RANK, new UserGenreCompositeKey(userId, genreId));

            createUserGenreRankHistoryEntry(userId, genreId, rank);
        }

        return isSuccessful;
    }

    @Override
    public int setUserLastVotingResult(final int voterId, final int userId, final int genreId) {
        final String sql = String.format("SELECT %1$s FROM %2$s WHERE %3$s=:userId AND %4$s=:voterId AND %5$s=:genreId ORDER BY %6$s DESC LIMIT 1", TABLE_COLUMN_POINTS, TABLE_USERS_RANK_BY_GENRES_VOTING, TABLE_COLUMN_USER_ID, TABLE_COLUMN_VOTER_ID, TABLE_COLUMN_GENRE_ID, TABLE_COLUMN_VOTING_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("voterId", voterId);
        paramSource.addValue("genreId", genreId);

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, paramSource, Integer.class)).orElse(0);
    }

    @Override
    public int getUserVotePointsForRankInGenre(final int userId, final int genreId) {
        final String sql = String.format("SELECT SUM(%s) FROM %s WHERE %s=:userId AND %s=:genreId"
                , TABLE_COLUMN_POINTS, TABLE_USERS_RANK_BY_GENRES_VOTING, TABLE_COLUMN_USER_ID, TABLE_COLUMN_GENRE_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, paramSource, Integer.class)).orElse(0);
    }

    @Override
    public List<UserRankPhotoVote> getUsersIdsWhoVotedForUserRankInGenre(final int userId, final int genreId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:userId AND %s=:genreId ORDER BY %s DESC;"
                , TABLE_USERS_RANK_BY_GENRES_VOTING, TABLE_COLUMN_USER_ID, TABLE_COLUMN_GENRE_ID, TABLE_COLUMN_VOTING_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);

        return jdbcTemplate.query(sql, paramSource, new UserRankPhotoVoteMapper());
    }

    @Override
    public List<UserGenreRankHistoryEntry> getUserGenreRankHistoryEntries(final int userId, final int genreId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:userId AND %s=:genreId ORDER BY %s ASC;"
                , TABLE_USERS_RANKS_HISTORY, TABLE_URH_COLUMN_USER_ID, TABLE_URH_COLUMN_GENRE_ID, TABLE_URH_COLUMN_ASSIGN_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);

        return jdbcTemplate.query(sql, paramSource, new RowMapper<UserGenreRankHistoryEntry>() {
            @Override
            public UserGenreRankHistoryEntry mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final User user = userService.load(rs.getInt(TABLE_URH_COLUMN_USER_ID));
                final Genre genre = genreService.load(rs.getInt(TABLE_URH_COLUMN_GENRE_ID));
                return new UserGenreRankHistoryEntry(user, genre, rs.getInt(TABLE_URH_COLUMN_RANK), rs.getTimestamp(TABLE_URH_COLUMN_ASSIGN_TIME));
            }
        });
    }

    private void createUserGenreRankHistoryEntry(final int userId, final int genreId, final int rank) {
        final String sql = String.format("INSERT INTO %s ( %s, %s, %s, %s ) VALUES( :userId, :genreId, :rank, :assignTime );"
                , TABLE_USERS_RANKS_HISTORY, TABLE_URH_COLUMN_USER_ID, TABLE_URH_COLUMN_GENRE_ID, TABLE_URH_COLUMN_RANK, TABLE_URH_COLUMN_ASSIGN_TIME);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);
        paramSource.addValue("rank", rank);
        paramSource.addValue("assignTime", dateUtilsService.getCurrentTime());

        jdbcTemplate.update(sql, paramSource);
    }

    private int loadUserRankInGenre(final int userId, final int genreId) {
        final String sql = String.format("SELECT %s FROM %s WHERE %s=:userId AND %s=:genreId", TABLE_URBG_COLUMN_USER_RANK_WHEN_VOTING, TABLE_USERS_RANKS_BY_GENRES, TABLE_URBG_COLUMN_USER_ID, TABLE_URBG_COLUMN_GENRE_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);

        return getIntValueOrZero(sql, paramSource);
    }

    private boolean doesUsersRanksByGenresRecordExist(final int userId, final int genreId) {
        final String sql = String.format("SELECT 1 FROM %s WHERE %s=:userId AND %s=:genreId"
                , TABLE_USERS_RANKS_BY_GENRES, TABLE_URBG_COLUMN_USER_ID, TABLE_URBG_COLUMN_GENRE_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("userId", userId);
        paramSource.addValue("genreId", genreId);

        return existsInt(sql, paramSource);
    }

    private class UserRankPhotoVoteMapper implements RowMapper<UserRankPhotoVote> {

        @Override
        public UserRankPhotoVote mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final User user = userDao.load(rs.getInt(TABLE_COLUMN_VOTER_ID));
            final Genre genre = genreDao.load(rs.getInt(TABLE_COLUMN_GENRE_ID));

            final UserRankPhotoVote result = new UserRankPhotoVote(user, genre);

            result.setVotePoints(rs.getInt(TABLE_COLUMN_POINTS));
            result.setVoteTime(rs.getTimestamp(TABLE_COLUMN_VOTING_TIME));

            return result;
        }
    }
}
