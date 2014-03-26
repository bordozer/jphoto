package core.context;

import core.general.user.User;
import core.services.translator.Language;
import org.springframework.mobile.device.DeviceType;

public class Environment {

//	public static final Language SYSTEM_DEFAULT_LANGUAGE = Language.RU;

	private User currentUser;
	private DeviceType deviceType;
	private boolean showNudeContent;

	private Language language;

	private String hiMessage;

	public Environment( final User currentUser, final Language language ) {
		this.currentUser = currentUser;
		this.language = language;
	}

	public Environment( final Environment environment ) {
		currentUser = environment.getCurrentUser();
		deviceType = environment.getDeviceType();
		showNudeContent = environment.isShowNudeContent();
		language = environment.getLanguage();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser( final User currentUser ) {
		this.currentUser = currentUser;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType( final DeviceType deviceType ) {
		this.deviceType = deviceType;
	}

	public boolean isShowNudeContent() {
		return showNudeContent;
	}

	public void setShowNudeContent( final boolean showNudeContent ) {
		this.showNudeContent = showNudeContent;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage( final Language language ) {
		this.language = language;
	}

	public String getHiMessage() {
		return hiMessage;
	}

	public void setHiMessage( final String hiMessage ) {
		this.hiMessage = hiMessage;
	}

	@Override
	public String toString() {
		return String.format( "Environment: %s", currentUser );
	}
}
