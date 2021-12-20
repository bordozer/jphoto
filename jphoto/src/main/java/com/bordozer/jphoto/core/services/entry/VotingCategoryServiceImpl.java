package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.genre.GenreVotingCategories;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.services.dao.PhotoVotingDao;
import com.bordozer.jphoto.core.services.dao.PhotoVotingDaoImpl;
import com.bordozer.jphoto.core.services.system.CacheService;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service("votingCategoryService")
public class VotingCategoryServiceImpl implements VotingCategoryService {

    @Autowired
    private PhotoVotingDao photoVotingDao;

    @Autowired
    private CacheService cacheService;

    @Override
    public boolean save(final PhotoVotingCategory entry) {
        final boolean isSuccessful = photoVotingDao.saveToDB(entry);

        cacheService.expire(CacheKey.PHOTO_VOTING_CATEGORY, entry.getId());
        cacheService.expire(CacheKey.GENRE_VOTING_CATEGORY);
        cacheService.expire(CacheKey.GENRE);

        return isSuccessful;
    }

    @Override
    public PhotoVotingCategory load(final int votingCategoryId) {
        return photoVotingDao.load(votingCategoryId);
    }

    @Override
    public PhotoVotingCategory loadByName(final String name) {
        return photoVotingDao.loadByName(name);
    }

    @Override
    public boolean delete(final int entryId) {
        return photoVotingDao.delete(entryId);
    }

    @Override
    public List<PhotoVotingCategory> loadAll() {

        final SqlTable tVotingCategory = new SqlTable(PhotoVotingDaoImpl.TABLE_VOTING_CATEGORIES);
        final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery(tVotingCategory);

        final SqlColumnSelect tVotCatColName = new SqlColumnSelect(tVotingCategory, PhotoVotingDaoImpl.TABLE_COLUMN_NAME);
        selectQuery.addSortingAsc(tVotCatColName);

        final List<Integer> idsResults = load(selectQuery).getIds();

        final List<PhotoVotingCategory> votingCategories = newArrayList();

        for (final Integer id : idsResults) {
            votingCategories.add(load(id));
        }

        return votingCategories;
    }

    @Override
    public SqlSelectIdsResult load(final SqlIdsSelectQuery selectIdsQuery) {
        return photoVotingDao.load(selectIdsQuery);
    }

    @Override
    public GenreVotingCategories getGenreVotingCategories(final int genreId) {
        return photoVotingDao.getGenreVotingCategories(genreId);
    }

    @Override
    public boolean exists(final int entryId) {
        return photoVotingDao.exists(entryId);
    }

    @Override
    public boolean exists(final PhotoVotingCategory entry) {
        return photoVotingDao.exists(entry);
    }
}
