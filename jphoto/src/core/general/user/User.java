package core.general.user;

import core.enums.PhotoActionAllowance;
import core.enums.UserGender;
import core.general.base.AbstractBaseEntity;
import core.interfaces.Cacheable;
import core.interfaces.Favoritable;
import core.interfaces.Nameable;
import core.interfaces.Restrictable;
import core.services.translator.Language;
import ui.services.menu.entry.items.PopupMenuAssignable;
import utils.StringUtilities;

import java.util.Date;
import java.util.Set;


public class User extends AbstractBaseEntity implements Nameable, Favoritable, Cacheable, PopupMenuAssignable, Restrictable {

	private String login;
	private String name;
	private String email;
	private Date dateOfBirth;
	private String homeSite;
	private String selfDescription;
	private UserMembershipType membershipType;
	private int photosOnPage;
	private UserStatus userStatus;
	private Date registrationTime;
	private UserGender gender;
	private boolean showNudeContent;

	private Set<EmailNotificationType> emailNotificationTypes;

	private PhotoActionAllowance defaultPhotoCommentsAllowance;
	private PhotoActionAllowance defaultPhotoVotingAllowance;

	private Language language;

	public User() {
	}

	public User( int id ) {
		super( id );
	}

	public User( final User user ) {
		setId( user.getId() );

		login = user.getLogin();
		name = user.getName();
		email = user.getEmail();
		dateOfBirth = user.getDateOfBirth();
		homeSite = user.getHomeSite();
		selfDescription = user.getSelfDescription();
		membershipType = user.getMembershipType();
		photosOnPage = user.getPhotosOnPage();
		userStatus = user.getUserStatus();
		registrationTime = user.getRegistrationTime();
		gender = user.getGender();
		showNudeContent = user.isShowNudeContent();

		emailNotificationTypes = user.getEmailNotificationTypes();
		defaultPhotoCommentsAllowance = user.getDefaultPhotoCommentsAllowance();
		defaultPhotoVotingAllowance = user.getDefaultPhotoVotingAllowance();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin( final String login ) {
		this.login = login;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getNameEscaped() {
		return StringUtilities.escapeHtml( name );
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( final String email ) {
		this.email = email;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth( final Date dateOfBirth ) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setHomeSite( final String homeSite ) {
		this.homeSite = homeSite;
	}

	public String getHomeSite() {
		return homeSite;
	}

	public String getSelfDescription() {
		return selfDescription;
	}

	public void setSelfDescription( final String selfDescription ) {
		this.selfDescription = selfDescription;
	}

	public UserMembershipType getMembershipType() {
		return membershipType;
	}

	public void setMembershipType( final UserMembershipType membershipType ) {
		this.membershipType = membershipType;
	}

	public int getPhotosOnPage() {
		return photosOnPage;
	}

	public void setPhotosOnPage( final int photosOnPage ) {
		this.photosOnPage = photosOnPage;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus( final UserStatus userStatus ) {
		this.userStatus = userStatus;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime( final Date registrationTime ) {
		this.registrationTime = registrationTime;
	}

	public UserGender getGender() {
		return gender;
	}

	public void setGender( final UserGender gender ) {
		this.gender = gender;
	}

	public Set<EmailNotificationType> getEmailNotificationTypes() {
		return emailNotificationTypes;
	}

	public void setEmailNotificationTypes( final Set<EmailNotificationType> emailNotificationTypes ) {
		this.emailNotificationTypes = emailNotificationTypes;
	}

	public PhotoActionAllowance getDefaultPhotoCommentsAllowance() {
		return defaultPhotoCommentsAllowance;
	}

	public void setDefaultPhotoCommentsAllowance( final PhotoActionAllowance defaultPhotoCommentsAllowance ) {
		this.defaultPhotoCommentsAllowance = defaultPhotoCommentsAllowance;
	}

	public PhotoActionAllowance getDefaultPhotoVotingAllowance() {
		return defaultPhotoVotingAllowance;
	}

	public void setDefaultPhotoVotingAllowance( final PhotoActionAllowance defaultPhotoVotingAllowance ) {
		this.defaultPhotoVotingAllowance = defaultPhotoVotingAllowance;
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

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof User ) ) {
			return false;
		}

		final User user = ( User ) obj;
		return user.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", getId(), getName() );
	}
}
