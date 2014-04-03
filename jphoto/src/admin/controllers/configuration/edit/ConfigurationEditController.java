package admin.controllers.configuration.edit;

import admin.controllers.configuration.list.SystemConfigurationListController;
import core.exceptions.BaseRuntimeException;
import core.general.cache.CacheKey;
import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.ConfigurationTab;
import core.general.configuration.SystemConfiguration;
import ui.services.breadcrumbs.BreadcrumbsAdminService;
import core.services.system.CacheServiceImpl;
import core.services.system.ConfigurationService;
import core.services.system.SystemConfigurationLoadService;
import core.services.user.UserRankService;
import core.services.utils.UrlUtilsService;
import elements.PageTitleData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes( ConfigurationEditController.MODEL_NAME )
@Controller
@RequestMapping( "configuration" )
public class ConfigurationEditController {

	public static final String MODEL_NAME = "configurationEditModel";

	private static final String EDIT_DATA_VIEW = "admin/configuration/edit/ConfigurationDataEdit";
	private static final String EDIT_TAB_VIEW = "admin/configuration/edit/ConfigurationEdit";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ConfigurationValidator configurationValidator;

	@Autowired
	private SystemConfigurationLoadService systemConfigurationLoadService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private CacheServiceImpl cacheService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@InitBinder
	protected void initBinder( final ServletRequestDataBinder binder ) {
		binder.setValidator( configurationValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public ConfigurationEditModel prepareModel() {
		return new ConfigurationEditModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String configurationNew( final @ModelAttribute( MODEL_NAME ) ConfigurationEditModel model ) {

		final SystemConfiguration defaultSystemConfiguration = systemConfigurationLoadService.getDefaultSystemConfiguration();

		final SystemConfiguration systemConfiguration = new SystemConfiguration();
		systemConfiguration.setName( String.format( "%s copy", defaultSystemConfiguration.getName() ) );
		systemConfiguration.setDescription( String.format( "%s copy", defaultSystemConfiguration.getDescription() ) );
		systemConfiguration.setDefaultConfiguration( false );

		model.setSystemConfiguration( systemConfiguration );
		model.setConfigurationTab( null );

		model.setConfigurationMap( configurationService.getSystemConfigurationParametersMap( systemConfiguration ) );

		model.getPageModel().setPageTitleData( breadcrumbsAdminService.getAdminConfigurationNew() );

		model.setRankInGenrePointsMap( getUserGenreRankPointsMap( model ) );

		return EDIT_DATA_VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{systemConfigurationId}/edit/" )
	public String configurationTabEdit( final @PathVariable( "systemConfigurationId" ) int systemConfigurationId, final @ModelAttribute( MODEL_NAME ) ConfigurationEditModel model, final HttpServletRequest request ) {

		model.setBindingResult( null );

		final String tab = request.getParameter( "tab" );
		final ConfigurationTab configurationTab = ConfigurationTab.getByKey( tab );

		final SystemConfiguration systemConfiguration = systemConfigurationLoadService.load( systemConfigurationId );
		if ( systemConfiguration == null ) {
			return SystemConfigurationListController.REDIRECT_CONFIGURATION_LINK;
		}

		model.setSystemConfiguration( systemConfiguration );

		model.setConfigurationMap( configurationService.getSystemConfigurationParametersMap( systemConfiguration ) );

		model.setConfigurationTab( configurationTab );

		model.setRankInGenrePointsMap( getUserGenreRankPointsMap( model ) );

		model.getPageModel().setPageTitleData( getPageData( model, configurationTab ) );

		return getView( configurationTab );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/{systemConfigurationId}/edit/" )
	public String configurationTabEditPost( @Valid final @ModelAttribute( MODEL_NAME ) ConfigurationEditModel model, final BindingResult bindingResult ) {
		model.setBindingResult( bindingResult );

		cacheService.expire( CacheKey.RANK_IN_GENRE_POINTS_CACHE_ENTRY );

		final ConfigurationTab configurationTab = ConfigurationTab.getByKey( model.getConfigurationTabKey() );

		final SystemConfiguration defaultSystemConfiguration = systemConfigurationLoadService.getDefaultSystemConfiguration();

		final String resetConfigurationKey = model.getResetConfigurationKey();
		if ( StringUtils.isNotEmpty( resetConfigurationKey ) ) {
			resetConfigurationKeyValue( model, defaultSystemConfiguration );
			model.setBindingResult( null ); // to prevent extra validation of reset key
		}

		for ( final String configurationKeyVal : model.getConfigurationMap().keySet() ) {
			final ConfigurationKey configurationKey = ConfigurationKey.getById( Integer.parseInt( configurationKeyVal ) );
			final Configuration configuration = model.getConfigurationMap().get( configurationKeyVal );
			final Configuration defaultConfiguration = defaultSystemConfiguration.getConfiguration( configurationKey );
			if ( ! configuration.equals( defaultConfiguration ) ) {
				configuration.setDefaultSystemConfiguration( defaultConfiguration );
				configuration.setGotFromDefaultSystemConfiguration( false );
			}
		}

		if ( bindingResult.hasErrors() ) {
			return getView( configurationTab );
		}

		model.setRankInGenrePointsMap( getUserGenreRankPointsMap( model ) );

		model.setConfigurationTab( configurationTab );
		model.getPageModel().setPageTitleData( getPageData( model, configurationTab ) );

		return getView( configurationTab );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/{systemConfigurationId}/save/" )
	public String configurationSave( @Valid final @ModelAttribute( MODEL_NAME ) ConfigurationEditModel model, final BindingResult bindingResult ) {
		model.setBindingResult( bindingResult );

		if ( bindingResult.hasErrors() ) {
			return EDIT_TAB_VIEW;
		}

		save( model.getSystemConfiguration(), model.getConfigurationMap() );

		return String.format( "redirect:%s", urlUtilsService.getAdminConfigurationTabsLink( model.getSystemConfiguration().getId() ) );
	}

	private Map<Integer, Integer> getUserGenreRankPointsMap( final ConfigurationEditModel model ) {
		final Configuration configuration1 = model.getConfiguration( ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP );
		final Configuration configuration2 = model.getConfiguration( ConfigurationKey.RANK_VOTING_POINTS_COEFFICIENT );

		return userRankService.getUserGenreRankPointsMap( configuration1.getValueInt(), configuration2.getValueFloat() );
	}

	private void resetConfigurationKeyValue( final ConfigurationEditModel model, final SystemConfiguration defaultSystemConfiguration ) {
		final String resetConfigurationKey = model.getResetConfigurationKey();
		final Configuration configuration = model.getConfigurationMap().get( resetConfigurationKey );
		final ConfigurationKey configurationKey = ConfigurationKey.getById( Integer.parseInt( resetConfigurationKey ) );
		configuration.setValue( defaultSystemConfiguration.getConfiguration( configurationKey ).getValue() );
		configuration.setGotFromDefaultSystemConfiguration( true );
		configuration.setDefaultSystemConfiguration( null );
	}

	private PageTitleData getPageData( final ConfigurationEditModel model, final ConfigurationTab configurationTab ) {
		final PageTitleData pageTitleData;
		if ( configurationTab != null ) {
			pageTitleData = breadcrumbsAdminService.getAdminConfigurationEditTabData( model.getSystemConfiguration().getId(), model.getSystemConfiguration(), configurationTab );
		} else {
			pageTitleData = breadcrumbsAdminService.getAdminConfigurationEditData( model.getSystemConfiguration() );
		}
		return pageTitleData;
	}

	private void save( final SystemConfiguration systemConfiguration, final Map<String, Configuration> configurationMap ) {

		if ( ! systemConfiguration.isDefaultConfiguration() ) {

			final int defaultConfigurationId = systemConfigurationLoadService.getDefaultSystemConfigurationId();
			final SystemConfiguration defaultSystemConfiguration = systemConfigurationLoadService.load( defaultConfigurationId );
			if ( defaultSystemConfiguration == null ) {
				throw new BaseRuntimeException( String.format( "Default System Configuration id=%d does not exists", defaultConfigurationId ) );
			}

			final List<Configuration> changedConfigurations = newArrayList();

			for ( final Configuration configuration : configurationMap.values() ) {
				final Configuration defaultConfiguration = defaultSystemConfiguration.getConfiguration( configuration.getConfigurationKey() );
				if ( ! configuration.isGotFromDefaultSystemConfiguration() || ! defaultConfiguration.getValue().equals( configuration.getValue() ) ) {
					changedConfigurations.add( configuration );
				}
			}

			systemConfiguration.setConfigurations( changedConfigurations );
		} else {
			final List<Configuration> configurations = newArrayList();
			for ( final Configuration configuration : configurationMap.values() ) {
				configurations.add( configuration );
			}
			systemConfiguration.setConfigurations( configurations );
		}

		systemConfigurationLoadService.save( systemConfiguration );

		final int hasChangedSystemConfigurationId = systemConfiguration.getId();
		if ( systemConfiguration.isDefaultConfiguration() || systemConfiguration.isActiveConfiguration() ) {
			configurationService.reloadSystemConfiguration( hasChangedSystemConfigurationId );
		}
	}

	private String getView( final ConfigurationTab configurationTab ) {
		if ( configurationTab == null ) {
			return EDIT_DATA_VIEW;
		}

		return EDIT_TAB_VIEW;
	}
}
