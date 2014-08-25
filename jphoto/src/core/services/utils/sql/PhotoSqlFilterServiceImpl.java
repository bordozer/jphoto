package core.services.utils.sql;

import core.services.dao.BaseEntityDao;
import core.services.dao.PhotoDaoImpl;
import core.services.dao.PhotoVotingDaoImpl;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

import java.util.Date;

public class PhotoSqlFilterServiceImpl implements PhotoSqlFilterService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Override
	public void addFilterByUser( final int userId, final BaseSqlSelectQuery selectQuery ) {
		selectQuery.addWhereAnd( baseSqlUtilsService.equalsCondition( PhotoDaoImpl.TABLE_PHOTOS, PhotoDaoImpl.TABLE_COLUMN_USER_ID, userId ) );
	}

	@Override
	public void addJoinWithPhotoVotingTable( final BaseSqlSelectQuery selectQuery ) {
		final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );

		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( selectQuery.getMainTable(), BaseEntityDao.ENTITY_ID );

		final SqlColumnSelect tPhotoVotingColPhotoId = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_PHOTO_ID );
		final SqlJoin joinVotingTable = SqlJoin.inner( tPhotoVoting, new SqlJoinCondition( tPhotoColId, tPhotoVotingColPhotoId ) );
		selectQuery.joinTable( joinVotingTable );
		selectQuery.addGrouping( tPhotoColId );
	}

	@Override
	public void addFilterByMinVotedMark( final BaseSqlSelectQuery selectQuery, final int minMarks ) {
		final SqlTable tPhotoVoting = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tPhotoVotingColMark = new SqlColumnSelect( tPhotoVoting, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_MARK );
		final SqlColumnAggregate tPhotoVotingColSumMark = new SqlColumnAggregate( tPhotoVotingColMark, SqlFunctions.SUM, PhotoQueryServiceImpl.SUM_MARK_COLUMN_ALIAS );
		final SqlCondition havingSumMarkMoreThen = new SqlCondition( tPhotoVotingColSumMark, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, minMarks > 0 ? minMarks : PhotoQueryServiceImpl.MIN_MARK_FOR_BEST, dateUtilsService );
		selectQuery.setHaving( havingSumMarkMoreThen );
	}

	@Override
	public void addFilterForVotingTimeForLastNDays( final BaseSqlSelectQuery selectQuery, final Date timeFrom, final Date timeTo ) {
		final SqlTable tPhotoVoting1 = new SqlTable( PhotoVotingDaoImpl.TABLE_PHOTO_VOTING );
		final SqlColumnSelect tPhotoVotingColTime = new SqlColumnSelect( tPhotoVoting1, PhotoVotingDaoImpl.TABLE_PHOTO_VOTING_TIME );

		selectQuery.addWhereAnd( new SqlCondition( tPhotoVotingColTime, SqlCriteriaOperator.GREATER_THAN_OR_EQUAL_TO, timeFrom, dateUtilsService ) );
		selectQuery.addWhereAnd( new SqlCondition( tPhotoVotingColTime, SqlCriteriaOperator.LESS_THAN_OR_EQUAL_TO, timeTo, dateUtilsService ) );
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setBaseSqlUtilsService( final BaseSqlUtilsService baseSqlUtilsService ) {
		this.baseSqlUtilsService = baseSqlUtilsService;
	}
}
