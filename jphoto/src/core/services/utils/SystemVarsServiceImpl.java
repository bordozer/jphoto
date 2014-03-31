package core.services.utils;

import core.exceptions.BaseRuntimeException;
import core.services.translator.Language;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import utils.ListUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SystemVarsServiceImpl implements SystemVarsService {

	private final CompositeConfiguration config = new CompositeConfiguration();

	private Language defaultLanguage;
	private final List<Language> activeLanguages = newArrayList();

	@Override
	public void initSystemVars() throws ConfigurationException {

		final List<String> propertyFiles = newArrayList();
		propertyFiles.add( "Base" );
		propertyFiles.add( "System" );
		propertyFiles.add( "Photo" );
		propertyFiles.add( "Database" );

		config.clear();

		for ( final String propertyFile : propertyFiles ) {
			config.addConfiguration( new PropertiesConfiguration( new File( String.format( "%s/%s.properties", getPropertiesPath(), propertyFile ) ) ) );
		}

		initLanguages();
	}

	private void initLanguages() {
		final List<Object> list = config.getList( "application.language.actives" );

		synchronized ( activeLanguages ) {
			activeLanguages.clear();

			for ( final Object code : list ) {
				final String languageCode = ( String ) code;
				activeLanguages.add( Language.getByCode( languageCode ) );
			}
		}

		if ( activeLanguages.size() == 0 ) {
			throw new BaseRuntimeException( "System active languages are not configured!" );
		}

		defaultLanguage = Language.getByCode( config.getString( "application.language.default" ) );

		if ( defaultLanguage == null ) {
			throw new BaseRuntimeException( "Default language is not configured!" );
		}
	}

	// Base properties -->
	@Override
	public String getProjectUrl() {
		return config.getString( "ProjectUrl" );
	}

	@Override
	public String getProjectUrlClosed() {
		return String.format( "%s/", config.getString( "ProjectUrl" ) );
	}

	@Override
	public String getTomcatWorkerName() {
		return config.getString( "TomcatWorkerName" );
	}

	@Override
	public String getApplicationPrefix() {
		return config.getString( "ApplicationPrefix" );
	}

	@Override
	public String getAdminPrefix() {
		return config.getString( "AdminPrefix" );
	}

	@Override
	public String getPhotoStoragePath() {
		return config.getString( "PhotoStoragePath" );
	}

	@Override
	public String getSystemTempFolder() {
		return config.getString( "SystemTempFolder" );
	}

	@Override
	public String getPreviewFolderName() {
		return config.getString( "PreviewFolderName" );
	}
	// Base properties <--

	// Date/time format properties -->
	@Override
	public String getProjectName() {
		return config.getString( "application.ProjectName" );
	}

	@Override
	public Language getSystemDefaultLanguage() {
		return defaultLanguage;
	}

	@Override
	public List<Language> getActiveLanguages() {
		return activeLanguages;
	}

	@Override
	public String getSystemDateFormat() {
		return config.getString( "datetime.SystemDateFormat" );
	}

	@Override
	public String getSystemTimeFormat() {
		return config.getString( "datetime.SystemTimeFormat" );
	}

	@Override
	public String getSystemTimeFormatShort() {
		return config.getString( "datetime.SystemTimeFormatShort" );
	}

	@Override
	public String getJavaScriptDateFormat() {
		return config.getString( "datetime.JavaScriptDateFormat" );
	}
	// Date/time format properties <--

	@Override
	public boolean isShowTranslationSigns() {
		return config.getBoolean( "translator.showTranslationSigns" );
	}

	@Override
	public int getPhotoLinesForNotLoggedUsers() {
		return config.getInt( "PhotoLines" );
	}

	@Override
	public int getPhotosInLineForNotLoggedUsers() {
		return config.getInt( "PhotosInLine" );
	}

	@Override
	public List<String> getAdminUserIds() {
		final List<Object> adminUserIDs = config.getList( "SuperAdminUserIDs", new ArrayList<Object>() );
		return ListUtils.convertObjectListToString( adminUserIDs );
	}

	@Override
	public boolean isDevMode() {
		return config.getBoolean( "isDevMode" );
	}

	@Override
	public String getDatabaseName() {
		return config.getString( "Database" );
	}

	@Override
	public String getPropertiesPath() {
		return "../properties";
	}
}
