package core.services.utils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import utils.ListUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SystemVarsServiceImpl implements SystemVarsService {

	private final CompositeConfiguration config = new CompositeConfiguration();

	@Override
	public void initSystemVars() throws ConfigurationException {

		final File basePropertiesFile = new File( String.format( "%s/Base.properties", getPropertiesPath() ) );
		final File systemPropertiesFile = new File( String.format( "%s/System.properties", getPropertiesPath() ) );
		final File photoPropertiesFile = new File( String.format( "%s/Photo.properties", getPropertiesPath() ) );
		final File dbPropertiesFile = new File( String.format( "%s/Database.properties", getPropertiesPath() ) );

		config.addConfiguration( new PropertiesConfiguration( basePropertiesFile ) );
		config.addConfiguration( new PropertiesConfiguration( systemPropertiesFile ) );
		config.addConfiguration( new PropertiesConfiguration( photoPropertiesFile ) );
		config.addConfiguration( new PropertiesConfiguration( dbPropertiesFile ) );
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
	public String getTranslatedSign() {
		return config.getString( "translator.TranslatedSign" );
	}

	@Override
	public String getTranslatorStartPrefix() {
		return config.getString( "translator.startPrefix" );
	}

	@Override
	public String getTranslatorEndPrefix() {
		return config.getString( "translator.endPrefix" );
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
