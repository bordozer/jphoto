package mocks;

import common.AbstractTestCase;
import core.services.translator.Language;
import core.services.utils.SystemVarsService;
import org.apache.commons.configuration.ConfigurationException;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SystemVarsServiceMock implements SystemVarsService {

	@Override
	public String getProjectUrl() {
		return "http://127.0.0.1:8085";
	}

	@Override
	public String getProjectUrlClosed() {
		return String.format( "%s/", getProjectUrl() );
	}

	@Override
	public String getTomcatWorkerName() {
		return "/worker";
	}

	@Override
	public String getApplicationPrefix() {
		return "/test";
	}

	@Override
	public Language getSystemDefaultLanguage() {
		return Language.RU;
	}

	@Override
	public List<Language> getActiveLanguages() {
		return newArrayList( Language.RU, Language.EN );
	}

	@Override
	public boolean isShowTranslationSigns() {
		return false;
	}

	@Override
	public String getAdminPrefix() {
		return "/adm";
	}

	@Override
	public String getPhotoStoragePath() {
		return "photo/storage/path";
	}

	@Override
	public String getSystemTempFolder() {
		return "system/temp/folder/";
	}

	@Override
	public String getPreviewFolderName() {
		return "previews";
	}

	@Override
	public String getProjectName() {
		return "Test jPhoto";
	}

	@Override
	public String getSystemDateFormat() {
		return "yyyy-MM-dd";
	}

	@Override
	public String getSystemTimeFormat() {
		return "HH:mm:ss";
	}

	@Override
	public String getSystemTimeFormatShort() {
		return "HH:mm";
	}

	@Override
	public String getJavaScriptDateFormat() {
		return "yy-mm-dd";
	}

	@Override
	public void initSystemVars() throws ConfigurationException {
	}

	@Override
	public int getPhotoLinesForNotLoggedUsers() {
		return -1;
	}

	@Override
	public int getPhotosInLineForNotLoggedUsers() {
		return 4;
	}

	@Override
	public List<String> getAdminUserIds() {
		return newArrayList( String.valueOf( AbstractTestCase.SUPER_ADMIN_1.getId() ), String.valueOf( AbstractTestCase.SUPER_ADMIN_2.getId() ) );
	}

	@Override
	public String getDatabaseName() {
		return "jPhoto_test";
	}

	@Override
	public boolean isDevMode() {
		return true;
	}

	@Override
	public String getPropertiesPath() {
		return "/path/to/properties";
	}
}
