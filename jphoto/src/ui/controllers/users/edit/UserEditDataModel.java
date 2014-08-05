package ui.controllers.users.edit;

import core.enums.PhotoActionAllowance;
import core.general.base.AbstractGeneralModel;
import core.general.user.User;
import core.services.translator.Language;
import ui.services.validation.DataRequirementService;
import ui.translatable.GenericTranslatableList;

import java.util.List;
import java.util.Set;

public class UserEditDataModel extends AbstractGeneralModel {

	public static final String USER_ID_FORM_CONTROL = "userId";
	public static final String USER_LOGIN_FORM_CONTROL = "login";
	public static final String USER_PASSWORD_FORM_CONTROL = "password";
	public static final String USER_CONFIRM_PASSWORD_FORM_CONTROL = "confirmPassword";
	public static final String USER_NAME_FORM_CONTROL = "name";
	public static final String USER_EMAIL_FORM_CONTROL = "email";
	public static final String USER_DATE_OF_BIRTH_FORM_CONTROL = "dateOfBirth";
	public static final String USER_HOME_SITE_FORM_CONTROL = "homeSite";
	public static final String USER_SELF_DESCRIPTION_FORM_CONTROL = "selfDescription";
	public static final String USER_GENDER_FORM_CONTROL = "userGender";
	public static final String MEMBERSHIP_TYPE_FORM_CONTROL = "membershipTypeId";
	public static final String FORM_CONTROL_EMAIL_NOTIFICATION_OPTION_IDS = "emailNotificationOptionIds";
	public static final String FORM_CONTROL_DEFAULT_COMMENTS_ALLOWANCE_ID = "defaultPhotoCommentsAllowanceId";
	public static final String FORM_CONTROL_DEFAULT_VOTING_ALLOWANCE_ID = "defaultPhotoVotingAllowanceId";
	public static final String FORM_CONTROL_SHOW_NUDE_CONTENT = "showNudeContent";

	public static final int MIN_PASSWORD_LENGTH = 6;
	public static final int MAX_PASSWORD_LENGTH = 16;

	private int userId;
	private String login;
	private String name;
	private String email;
	private String dateOfBirth;
	private String homeSite;
	private String selfDescription;
	private int membershipTypeId;
	private int userGenderId;
	private boolean showNudeContent;
	private int userUILanguageId;

	private GenericTranslatableList<Language> usedLanguageTranslatableList;

	private int photosOnPage;

	private Set<String> emailNotificationOptionIds;

	private int defaultPhotoCommentsAllowanceId;
	private int defaultPhotoVotingAllowanceId;

	private String password;
	private String confirmPassword;

	private int minLoginLength;
	private int maxLoginLength;

	private int minUserNameLength;
	private int maxUserNameLength;

	private User beingChangedUser;

	private List<PhotoActionAllowance> accessibleCommentAllowances;
	private List<PhotoActionAllowance> accessibleVotingAllowances;

	private DataRequirementService dataRequirementService;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin( final String login ) {
		this.login = login != null ? login.trim() : "";
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name != null ? name.trim() : "";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( final String email ) {
		this.email = email != null ? email.trim() : "";
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth( final String dateOfBirth ) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getHomeSite() {
		return homeSite;
	}

	public void setHomeSite( final String homeSite ) {
		this.homeSite = homeSite != null ? homeSite.trim() : "";
	}

	public String getSelfDescription() {
		return selfDescription;
	}

	public void setSelfDescription( final String selfDescription ) {
		this.selfDescription = selfDescription;
	}

	public int getMembershipTypeId() {
		return membershipTypeId;
	}

	public void setMembershipTypeId( final int membershipTypeId ) {
		this.membershipTypeId = membershipTypeId;
	}

	public int getUserGenderId() {
		return userGenderId;
	}

	public void setUserGenderId( final int userGenderId ) {
		this.userGenderId = userGenderId;
	}

	public int getPhotosOnPage() {
		return photosOnPage;
	}

	public void setPhotosOnPage( final int photosOnPage ) {
		this.photosOnPage = photosOnPage;
	}

	public String getPassword() {
		return password;
	}

	public Set<String> getEmailNotificationOptionIds() {
		return emailNotificationOptionIds;
	}

	public void setEmailNotificationOptionIds( final Set<String> emailNotificationOptionIds ) {
		this.emailNotificationOptionIds = emailNotificationOptionIds;
	}

	public int getDefaultPhotoCommentsAllowanceId() {
		return defaultPhotoCommentsAllowanceId;
	}

	public void setDefaultPhotoCommentsAllowanceId( final int defaultPhotoCommentsAllowanceId ) {
		this.defaultPhotoCommentsAllowanceId = defaultPhotoCommentsAllowanceId;
	}

	public int getDefaultPhotoVotingAllowanceId() {
		return defaultPhotoVotingAllowanceId;
	}

	public void setDefaultPhotoVotingAllowanceId( final int defaultPhotoVotingAllowanceId ) {
		this.defaultPhotoVotingAllowanceId = defaultPhotoVotingAllowanceId;
	}

	public void setPassword( final String password ) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword( final String confirmPassword ) {
		this.confirmPassword = confirmPassword;
	}

	public int getMinLoginLength() {
		return minLoginLength;
	}

	public void setMinLoginLength( final int minLoginLength ) {
		this.minLoginLength = minLoginLength;
	}

	public int getMaxLoginLength() {
		return maxLoginLength;
	}

	public void setMaxLoginLength( final int maxLoginLength ) {
		this.maxLoginLength = maxLoginLength;
	}

	public User getBeingChangedUser() {
		return beingChangedUser;
	}

	public void setBeingChangedUser( final User beingChangedUser ) {
		this.beingChangedUser = beingChangedUser;
	}

	public boolean isShowNudeContent() {
		return showNudeContent;
	}

	public void setShowNudeContent( final boolean showNudeContent ) {
		this.showNudeContent = showNudeContent;
	}

	public int getUserUILanguageId() {
		return userUILanguageId;
	}

	public void setUserUILanguageId( final int userUILanguageId ) {
		this.userUILanguageId = userUILanguageId;
	}

	public GenericTranslatableList<Language> getUsedLanguageTranslatableList() {
		return usedLanguageTranslatableList;
	}

	public void setUsedLanguageTranslatableList( final GenericTranslatableList<Language> usedLanguageTranslatableList ) {
		this.usedLanguageTranslatableList = usedLanguageTranslatableList;
	}

	public List<PhotoActionAllowance> getAccessibleCommentAllowances() {
		return accessibleCommentAllowances;
	}

	public void setAccessibleCommentAllowances( final List<PhotoActionAllowance> accessibleCommentAllowance ) {
		this.accessibleCommentAllowances = accessibleCommentAllowance;
	}

	public List<PhotoActionAllowance> getAccessibleVotingAllowances() {
		return accessibleVotingAllowances;
	}

	public void setAccessibleVotingAllowances( final List<PhotoActionAllowance> accessibleVotingAllowance ) {
		this.accessibleVotingAllowances = accessibleVotingAllowance;
	}

	public int getMinUserNameLength() {
		return minUserNameLength;
	}

	public void setMinUserNameLength( final int minUserNameLength ) {
		this.minUserNameLength = minUserNameLength;
	}

	public int getMaxUserNameLength() {
		return maxUserNameLength;
	}

	public void setMaxUserNameLength( final int maxUserNameLength ) {
		this.maxUserNameLength = maxUserNameLength;
	}

	public DataRequirementService getDataRequirementService() {
		return dataRequirementService;
	}

	public void setDataRequirementService( final DataRequirementService dataRequirementService ) {
		this.dataRequirementService = dataRequirementService;
	}

	@Override
	public void clear() {
		super.clear();

		beingChangedUser = null;
		password = null;
		confirmPassword = null;
		selfDescription = null;
	}
}
