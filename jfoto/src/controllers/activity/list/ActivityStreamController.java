package controllers.activity.list;

import core.general.activity.AbstractActivityStreamEntry;
import core.general.base.PagingModel;
import core.services.dao.ActivityStreamDaoImpl;
import core.services.entry.ActivityStreamService;
import core.services.pageTitle.PageTitleService;
import core.services.utils.sql.BaseSqlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.SqlColumnSelect;
import sql.builder.SqlColumnSelectable;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlTable;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "activityStream" )
public class ActivityStreamController {

	public static final int ACTIVITY_STREAM_ITEMS_ON_PAGE = 30; // TODO: do configurable

	public static final String MODEL_NAME = "activityStreamModel";
	private static final String VIEW = "/activity/list/ActivityStream";

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private PageTitleService pageTitleService;

	@ModelAttribute( MODEL_NAME )
	public ActivityStreamModel prepareModel() {
		return new ActivityStreamModel();
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel();
		PagingUtils.initPagingModel( pagingModel, request );
		pagingModel.setItemsOnPage( ACTIVITY_STREAM_ITEMS_ON_PAGE );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showActivityStream( final @ModelAttribute( MODEL_NAME ) ActivityStreamModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final SqlTable activityStreamTable = new SqlTable( ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM );
		final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery( activityStreamTable );
		final SqlColumnSelectable timeCol = new SqlColumnSelect( activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME );
		selectQuery.addSortingDesc( timeCol );
		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );

		final SqlSelectIdsResult idsResult = activityStreamService.load( selectQuery );
		
		final List<AbstractActivityStreamEntry> activities = newArrayList();
		for ( final int activityId : idsResult.getIds() ) {
			activities.add( activityStreamService.load( activityId ) );
		}

		model.setActivities( activities );

		pagingModel.setTotalItems( idsResult.getRecordQty() );

		model.setPageTitleData( pageTitleService.getActivityStreamData() );

		return VIEW;
	}
}
