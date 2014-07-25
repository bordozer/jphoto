package admin.controllers.jobs.edit.photosImport.importParameters;

import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import core.enums.UserGender;
import core.general.user.UserMembershipType;
import core.services.translator.Language;

import java.util.List;

public class PhotosightImportParameters extends AbstractImportParameters {

	private final List<String> photosightUserIds;
	private final String userName;
	private final UserGender userGender;
	private final UserMembershipType membershipType;
	private final boolean importComments;
	private final int delayBetweenRequest;
	private final int pageQty;

	private final boolean breakImportIfAlreadyImportedPhotoFound;

	private List<PhotosightCategory> photosightCategories;

	private final AbstractRemoteContentHelper remoteContentHelper;

	public PhotosightImportParameters( final List<String> photosightUserIds, final String userName, final UserGender userGender, final UserMembershipType membershipType, final boolean importComments, final int delayBetweenRequest, final int pageQty, final Language language, final boolean breakImportIfAlreadyImportedPhotoFound, final List<PhotosightCategory> photosightCategories, final AbstractRemoteContentHelper remoteContentHelper ) {
		super( language );
		this.photosightUserIds = photosightUserIds;
		this.userName = userName;
		this.userGender = userGender;
		this.membershipType = membershipType;
		this.importComments = importComments;
		this.delayBetweenRequest = delayBetweenRequest;
		this.pageQty = pageQty;
		this.breakImportIfAlreadyImportedPhotoFound = breakImportIfAlreadyImportedPhotoFound;
		this.photosightCategories = photosightCategories;
		this.remoteContentHelper = remoteContentHelper;
	}

	public List<String> getPhotosightUserIds() {
		return photosightUserIds;
	}

	public String getUserName() {
		return userName;
	}

	public UserGender getUserGender() {
		return userGender;
	}

	public UserMembershipType getMembershipType() {
		return membershipType;
	}

	public boolean isImportComments() {
		return importComments;
	}

	public int getDelayBetweenRequest() {
		return delayBetweenRequest;
	}

	public int getPageQty() {
		return pageQty;
	}

	public Language getLanguage() {
		return language;
	}

	public List<PhotosightCategory> getPhotosightCategories() {
		return photosightCategories;
	}

	public boolean isBreakImportIfAlreadyImportedPhotoFound() {
		return breakImportIfAlreadyImportedPhotoFound;
	}

	public AbstractRemoteContentHelper getRemoteContentHelper() {
		return remoteContentHelper;
	}
}
