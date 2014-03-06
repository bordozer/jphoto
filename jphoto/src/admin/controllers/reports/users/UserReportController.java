package admin.controllers.reports.users;

import core.general.user.User;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Controller
@RequestMapping( value = "reports/users" )
public class UserReportController {

	public static final String MODEL_NAME = "userReportModel";

	private static final String VIEW = "admin/reports/users/UsersReport";

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@Autowired
	private UserService userService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@ModelAttribute( MODEL_NAME )
	public UserReportModel prepareModel() {
		final UserReportModel model = new UserReportModel();

		model.setPageTitleData( pageTitleAdminUtilsService.getUsersReportTitleData() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String controlPanel( final @ModelAttribute( MODEL_NAME ) UserReportModel model ) {
		final List<User> users = userService.loadAll();

		Collections.sort( users, new Comparator<User>() {
			@Override
			public int compare( final User o1, final User o2 ) {
				return o1.getRegistrationTime().compareTo( o2.getRegistrationTime() );
			}
		} );

		final Map<Date, UserRegistrationData> registrationsMap = newLinkedHashMap();
		for ( final User user : users ) {
			final Date registrationDate = dateUtilsService.getFirstSecondOfDay( user.getRegistrationTime() );
			if ( registrationsMap.containsKey( registrationDate ) ) {
				final UserRegistrationData userRegistrationData = registrationsMap.get( registrationDate );
				userRegistrationData.increaseUsersCount();
			} else {
				registrationsMap.put( registrationDate, new UserRegistrationData( 1 ) );
			}
		}

		model.setRegistrationsMap( registrationsMap );

		return VIEW;
	}
}
