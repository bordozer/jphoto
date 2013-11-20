package controllers.users.login;

import core.general.base.AbstractGeneralModel;

public class UserLoginModel extends AbstractGeneralModel {

	public static final String LOGIN_FORM_LOGIN_CONTROL = "userlogin";
	public static final String LOGIN_FORM_PASSWORD_CONTROL = "userpassword";
	public static final String LOGIN_FORM_LOGIN_USER_AUTOMATICALLY_CONTROL = "loginUserAutomatically";

	private String userlogin;
	private String userpassword;
	private boolean loginUserAutomatically;

	public String getUserlogin() {
		return userlogin;
	}

	public void setUserlogin( String userlogin ) {
		this.userlogin = userlogin;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword( String userpassword ) {
		this.userpassword = userpassword;
	}

	public boolean isLoginUserAutomatically() {
		return loginUserAutomatically;
	}

	public void setLoginUserAutomatically( final boolean loginUserAutomatically ) {
		this.loginUserAutomatically = loginUserAutomatically;
	}
}
