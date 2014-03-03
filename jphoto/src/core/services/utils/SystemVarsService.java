package core.services.utils;

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

	String getTranslatedSign();

	int getPhotoLinesForNotLoggedUsers();

	int getPhotosInLineForNotLoggedUsers();

	List<String> getAdminUserIds();

	boolean isDevMode();

	String getDatabaseName();

	String getPropertiesPath();

	String getTranslatorStartPrefix();

	String getTranslatorEndPrefix();
}
