package admin.controllers.anonymousDays;

import core.general.configuration.ConfigurationKey;
import core.services.entry.AnonymousDaysService;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.system.ConfigurationService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utils.NumberUtils;

@Controller
@RequestMapping( "anonymousDays" )
public class AnonymousDaysAdminController {

	public static final String MODEL_NAME = "anonymousDaysAdminModel";

	private static final String VIEW = "/admin/anonymousDays/AnonymousDaysAdmin";

	@Autowired
	private AnonymousDaysService anonymousDaysService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@ModelAttribute( MODEL_NAME )
	public AnonymousDaysAdminModel prepareModel() {
		return new AnonymousDaysAdminModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String anonymousDaysForCurrentYear( final @ModelAttribute( MODEL_NAME ) AnonymousDaysAdminModel model ) {

		initModel( model, getCurrentYear() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{year}/" )
	public String anonymousDaysForCustomYear( final @PathVariable( "year" ) String _year, final @ModelAttribute( MODEL_NAME ) AnonymousDaysAdminModel model ) {

		int year = NumberUtils.convertToInt( _year );
		if ( year == 0 ) {
			year = getCurrentYear();
		}

		initModel( model, year );

		return VIEW;
	}

	private void initModel( final AnonymousDaysAdminModel model, final int year ) {
		model.setPageTitleData( pageTitleAdminUtilsService.getAnonymousDaysData() );
		model.setAnonymousDays( anonymousDaysService.loadAll() );
		model.setAnonymousPeriod( configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_PERIOD ) );
		model.setAnonymousDaysForYear( year );

		model.setCurrentYear( getCurrentYear() );
		model.setCalendarMinDate( dateUtilsService.getFirstSecondOfYear( year ) );
		model.setCalendarMaxDate( dateUtilsService.getLastSecondOfYear( year ) );
	}

	private int getCurrentYear() {
		return dateUtilsService.getYear( dateUtilsService.getCurrentDate() );
	}
}
