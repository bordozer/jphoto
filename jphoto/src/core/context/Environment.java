package core.context;

import core.general.user.User;
import org.springframework.mobile.device.DeviceType;

public class Environment {

	private User currentUser;
	private DeviceType deviceType;
	private boolean showNudeContent;

	private String hiMessage;

	public Environment( final User currentUser ) {
		this.currentUser = currentUser;
	}

	public Environment( final Environment environment ) {
		currentUser = environment.getCurrentUser();
		deviceType = environment.getDeviceType();
		showNudeContent = environment.isShowNudeContent();
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
