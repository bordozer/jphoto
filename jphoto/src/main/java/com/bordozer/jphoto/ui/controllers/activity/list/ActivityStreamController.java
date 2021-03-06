package com.bordozer.jphoto.ui.controllers.activity.list;

import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.services.dao.ActivityStreamDaoImpl;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.sql.BaseSqlUtilsService;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlColumnSelectable;
import com.bordozer.jphoto.sql.builder.SqlCondition;
import com.bordozer.jphoto.sql.builder.SqlCriteriaOperator;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlLogicallyJoinable;
import com.bordozer.jphoto.sql.builder.SqlTable;
import com.bordozer.jphoto.ui.activity.AbstractActivityStreamEntry;
import com.bordozer.jphoto.ui.activity.ActivityType;
import com.bordozer.jphoto.ui.services.breadcrumbs.CommonBreadcrumbsService;
import com.bordozer.jphoto.utils.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping("activityStream")
public class ActivityStreamController {

    public static final int ACTIVITY_STREAM_ITEMS_ON_PAGE = 30; // TODO: do configurable

    public static final String MODEL_NAME = "activityStreamModel";
    private static final String VIEW = "/activity/list/ActivityStream";

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private BaseSqlUtilsService baseSqlUtilsService;

    @Autowired
    private CommonBreadcrumbsService commonBreadcrumbsService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private Services services;

    @ModelAttribute(MODEL_NAME)
    public ActivityStreamModel prepareModel() {
        return new ActivityStreamModel();
    }

    @ModelAttribute("pagingModel")
    public PagingModel preparePagingModel(final HttpServletRequest request) {
        final PagingModel pagingModel = new PagingModel(services);
        PagingUtils.initPagingModel(pagingModel, request);
        pagingModel.setItemsOnPage(ACTIVITY_STREAM_ITEMS_ON_PAGE);

        return pagingModel;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showActivityStream(final @ModelAttribute(MODEL_NAME) ActivityStreamModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel) {
        return showActivities(model, pagingModel, null);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/type/{activityTypeId}/")
    public String showActivityStream(final @PathVariable("activityTypeId") int activityTypeId, final @ModelAttribute(MODEL_NAME) ActivityStreamModel model, final @ModelAttribute("pagingModel") PagingModel pagingModel) {
        final ActivityType activityType = ActivityType.getById(activityTypeId);

        return showActivities(model, pagingModel, activityType);
    }

    private String showActivities(final ActivityStreamModel model, final PagingModel pagingModel, final ActivityType activityType) {
        final SqlSelectIdsResult idsResult = getIds(model, pagingModel, activityType);

        final List<AbstractActivityStreamEntry> activities = getActivities(idsResult);

        model.setActivities(activities);

        pagingModel.setTotalItems(idsResult.getRecordQty());

        model.setPageTitleData(commonBreadcrumbsService.getActivityStreamBreadcrumbs(activityType));

        return VIEW;
    }

    private List<AbstractActivityStreamEntry> getActivities(final SqlSelectIdsResult idsResult) {
        final List<AbstractActivityStreamEntry> activities = newArrayList();
        for (final int activityId : idsResult.getIds()) {
            activities.add(activityStreamService.load(activityId));
        }
        return activities;
    }

    private SqlSelectIdsResult getIds(final ActivityStreamModel model, final PagingModel pagingModel, final ActivityType activityType) {
        final SqlIdsSelectQuery selectQuery = getSelectQuery(model, pagingModel, activityType);

        return activityStreamService.load(selectQuery);
    }

    private SqlIdsSelectQuery getSelectQuery(final ActivityStreamModel model, final PagingModel pagingModel, final ActivityType activityType) {
        final SqlTable activityStreamTable = new SqlTable(ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM);
        final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery(activityStreamTable);

        if (activityType != null) {
            final SqlColumnSelectable tActivityColType = new SqlColumnSelect(activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE);
            final SqlLogicallyJoinable condition = new SqlCondition(tActivityColType, SqlCriteriaOperator.EQUALS, activityType.getId(), dateUtilsService);
            selectQuery.addWhereAnd(condition);

            model.setFilterActivityTypeId(activityType.getId());
        }

        final SqlColumnSelectable timeCol = new SqlColumnSelect(activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME);
        selectQuery.addSortingDesc(timeCol);
        baseSqlUtilsService.initLimitAndOffset(selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());
        return selectQuery;
    }
}
