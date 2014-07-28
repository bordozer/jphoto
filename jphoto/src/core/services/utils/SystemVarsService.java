package core.services.utils;

import core.services.translator.Language;
import org.apache.commons.configuration.ConfigurationException;

import java.io.File;
import java.util.List;

public interface SystemVarsService {

	String BEAN_NAME = "systemVarsService";

	String getProjectUrl();

	String getTomcatWorkerName();

	String getAdminPrefix();

	String getPhotoStoragePath();

	String getSystemTempFolder();

	File getRemotePhotoSitesCacheFolder();

	String getProjectName();

	String getSystemDateFormat();

	String getSystemTimeFormat();

	String getSystemTimeFormatShort();

	String getJavaScriptDateFormat();

	List<String> getAdminUserIds();

	boolean isDevMode();

	String getDatabaseName();

	String getPropertiesPath();

	void initSystemVars() throws ConfigurationException;

	Language getSystemDefaultLanguage();

	List<Language> getActiveLanguages();

	boolean isShowTranslationSigns();
}
