package admin.controllers.jobs.edit.users;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.jobs.enums.JobExecutionStatus;

public class UserGenerationModel extends AbstractAdminJobModel {

	public final static String USER_QTY_FORM_CONTROL = "userQtyLimit";
	public final static String AVATAR_DIR_FORM_CONTROL = "avatarsDir";

	private String userQtyLimit = "5";
	private String avatarsDir = "/home/blu/jphoto/avatars/"; // TODO:

	private JobExecutionStatus generationResult;

	public JobExecutionStatus getGenerationResult() {
		return generationResult;
	}

	public void setGenerationResult( final JobExecutionStatus generationResult ) {
		this.generationResult = generationResult;
	}

	public String getAvatarsDir() {
		return avatarsDir;
	}

	public void setAvatarsDir( final String avatarsDir ) {
		this.avatarsDir = avatarsDir;
	}

	public String getUserQtyLimit() {
		return userQtyLimit;
	}

	public void setUserQtyLimit( final String userQtyLimit ) {
		this.userQtyLimit = userQtyLimit;
	}
}
