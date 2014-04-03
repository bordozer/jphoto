package ui.controllers.photos.activity;

import ui.controllers.activity.list.ActivityStreamController;
import core.context.EnvironmentContext;
import core.general.activity.AbstractActivityStreamEntry;
import core.general.base.PagingModel;
import core.general.photo.Photo;
import core.services.dao.ActivityStreamDaoImpl;
import core.services.entry.ActivityStreamService;
import ui.services.breadcrumbs.PageTitleService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.Services;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.BaseSqlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sql.SqlSelectIdsResult;
import sql.builder.*;
import utils.NumberUtils;
import utils.PagingUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "photo/{photoId}/activity" )
public class PhotoActivityStreamController {

	public static final String MODEL_NAME = "photoActivityStreamModel";

	private static final String VIEW = "photos/activity/PhotoActivityStream";

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private PageTitleService pageTitleService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private Services services;

	@ModelAttribute( MODEL_NAME )
	public PhotoActivityStreamModel prepareModel( final @PathVariable( "photoId" ) String _photoId ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		return new PhotoActivityStreamModel( photoService.load( photoId ) );
	}

	@ModelAttribute( "pagingModel" )
	public PagingModel preparePagingModel( final HttpServletRequest request ) {
		final PagingModel pagingModel = new PagingModel( services );
		PagingUtils.initPagingModel( pagingModel, request );
		pagingModel.setItemsOnPage( ActivityStreamController.ACTIVITY_STREAM_ITEMS_ON_PAGE );

		return pagingModel;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showActivityStream( final @ModelAttribute( MODEL_NAME ) PhotoActivityStreamModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {
		return showActivity( model, pagingModel );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/type/{activityTypeId}/" )
	public String showActivityStreamFiltered( final @PathVariable( "activityTypeId" ) String _activityTypeId, final @ModelAttribute( MODEL_NAME ) PhotoActivityStreamModel model, final @ModelAttribute( "pagingModel" ) PagingModel pagingModel ) {

		final int activityTypeId = NumberUtils.isNumeric( _activityTypeId ) ? NumberUtils.convertToInt( _activityTypeId ) : 0;
		model.setFilterActivityTypeId( activityTypeId );

		return showActivity( model, pagingModel );
	}

	private String showActivity( final PhotoActivityStreamModel model, final PagingModel pagingModel ) {
		final Photo photo = model.getPhoto();

		final SqlTable activityStreamTable = new SqlTable( ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM );
		final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery( activityStreamTable );

		final SqlColumnSelectable tActivityConUserId = new SqlColumnSelect( activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_PHOTO_ID );
		final SqlLogicallyJoinable where = new SqlCondition( tActivityConUserId, SqlCriteriaOperator.EQUALS, photo.getId(), dateUtilsService );
		selectQuery.addWhereAnd( where );

		final int filterActivityTypeId = model.getFilterActivityTypeId();
		if ( filterActivityTypeId > 0 ) {
			final SqlColumnSelectable tActivityColActivityTypeId = new SqlColumnSelect( activityStreamTable, ActivityStreamDaoImpl.TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE );
			final SqlLogicallyJoinable whereActivityTypeId = new SqlCondition( tActivityColActivityTypeId, SqlCriteriaOperator.EQUALS, filterActivityTypeId, dateUtilsService );
			selectQuery.addWhereAnd( whereActivityTypeId );
		}

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

		model.setPageTitleData( pageTitleService.photoCardTitle( photo, EnvironmentContext.getCurrentUser(), translatorService.translate( "Activity", EnvironmentContext.getLanguage() ) ) );

		return VIEW;
	}
}
