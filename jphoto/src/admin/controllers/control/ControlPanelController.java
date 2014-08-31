package admin.controllers.control;

import core.general.cache.CacheKey;
import core.general.user.User;
import core.services.entry.PrivateMessageService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoPreviewService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.CacheService;
import core.services.user.UserService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsService;
import org.apache.commons.configuration.ConfigurationException;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsAdminService;
import utils.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping( value = "control-panel" )
public class ControlPanelController {

	public static final String MODEL_NAME = "controlPanelModel";

	private static final String VIEW = "admin/control/ControlPanel";

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoPreviewService photoPreviewService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private SecurityService securityService;

	@ModelAttribute( MODEL_NAME )
	public ControlPanelModel prepareModel() {
		final ControlPanelModel model = new ControlPanelModel();

		model.setPageTitleData( breadcrumbsAdminService.getControlPanelTitleBreadcrumbs() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String controlPanel( final @ModelAttribute( MODEL_NAME ) ControlPanelModel model ) {

		model.setUsersTotal( userService.getUserCount() );
		model.setPhotosTotal( photoService.getPhotosCount() );
		model.setPhotoPreviewsTotal( photoPreviewService.getPreviewCount() );
		model.setPhotoCommentsTotal( photoCommentService.getPhotoCommentsCount() );
		model.setPrivateMessagesTotal( privateMessageService.getPrivateMessagesCount() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/reload-system-properties/" )
	public String reloadSystemProperties( final @ModelAttribute( MODEL_NAME ) ControlPanelModel model ) throws ConfigurationException {

		systemVarsService.initSystemVars();

		return getRedirectUrl();
	}

	@RequestMapping( method = RequestMethod.POST, value = "/cleanup-user/" )
	public String reloadTranslations( final @ModelAttribute( MODEL_NAME ) ControlPanelModel model, final HttpServletRequest request ) {

		final String userPickerId = request.getParameter( "userIdToCleanup" );
		securityService.assertUserExists( userPickerId );

		final int userId = NumberUtils.convertToInt( userPickerId );

		securityService.assertSuperAdminAccess( EnvironmentContext.getCurrentUser() );

		final List<Integer> photosIds = photoService.getUserPhotosIds( userId );
		for ( final Integer photosId : photosIds ) {
			photoService.delete( photosId );
		}

		return getRedirectUrl();
	}

	@RequestMapping( method = RequestMethod.POST, value = "/clear-cache/" )
	public String clearSystemCache( final @ModelAttribute( MODEL_NAME ) ControlPanelModel model ) throws DocumentException {

		for ( final CacheKey cacheKey : CacheKey.values() ) {
			cacheService.expire( cacheKey );
		}

		return getRedirectUrl();
	}

	private String getRedirectUrl() {
		return String.format( "redirect:%s", urlUtilsService.getAdminControlPanelLink() );
	}
}
