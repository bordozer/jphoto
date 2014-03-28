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

	String getTranslatorTranslatedStartPrefix();

	int getPhotoLinesForNotLoggedUsers();

	int getPhotosInLineForNotLoggedUsers();

	List<String> getAdminUserIds();

	boolean isDevMode();

	String getDatabaseName();

	String getPropertiesPath();

	String getTranslatorUntranslatedStartPrefix();

	String getTranslatorUntranslatedEndPrefix();

	void initSystemVars() throws ConfigurationException;

	String getTranslatorTranslatedEndPrefix();

	Language getSystemDefaultLanguage();

	List<Language> getUsedLanguages();

	boolean getShowLanguageCodeAfterTranslation();
}
