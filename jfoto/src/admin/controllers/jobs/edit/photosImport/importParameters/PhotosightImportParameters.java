package admin.controllers.jobs.edit.photosImport.importParameters;

import core.enums.UserGender;
import core.general.user.UserMembershipType;

import java.util.List;

public class PhotosightImportParameters implements ImportParameters {

	private final List<Integer> photosightUserIds;
	private final String userName;
	private final UserGender userGender;
	private final UserMembershipType membershipType;
	private final boolean importComments;
	private final int delayBetweenRequest;
	private final int pageQty;

	public PhotosightImportParameters( final List<Integer> photosightUserIds, final String userName, final UserGender userGender, final UserMembershipType membershipType, final boolean importComments, final int delayBetweenRequest, final int pageQty ) {
		this.photosightUserIds = photosightUserIds;
		this.userName = userName;
		this.userGender = userGender;
		this.membershipType = membershipType;
		this.importComments = importComments;
		this.delayBetweenRequest = delayBetweenRequest;
		this.pageQty = pageQty;
	}

	public List<Integer> getPhotosightUserIds() {
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
}
