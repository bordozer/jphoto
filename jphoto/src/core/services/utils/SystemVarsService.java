package core.services.utils;

import core.services.translator.Language;
import org.apache.commons.configuration.ConfigurationException;

import java.util.List;

public interface SystemVarsService {

	String BEAN_NAME = "systemVarsService";

	String getProjectUrl();

	String getProjectUrlClosed();

	String getTomcatWorkerName();

	String getApplicationPrefix();

	String getAdminPrefix();

	String getPhotoStoragePath();

	String getSystemTempFolder();

	String getPreviewFolderName();

	String getProjectName();

	String getSystemDateFormat();

	String getSystemTimeFormat();

	String getSystemTimeFormatShort();

	String getJavaScriptDateFormat();

	int getPhotoLinesForNotLoggedUsers();

	int getPhotosInLineForNotLoggedUsers();

	List<String> getAdminUserIds();

	boolean isDevMode();

	String getDatabaseName();

	String getPropertiesPath();

	void initSystemVars() throws ConfigurationException;

	Language getSystemDefaultLanguage();

	List<Language> getActiveLanguages();

	boolean isShowTranslationSigns();
}
