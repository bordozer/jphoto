package admin.controllers.jobs.edit.photosImport.importParameters;

import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import core.enums.UserGender;
import core.general.user.UserMembershipType;
import core.services.translator.Language;

import java.util.List;

public class RemoteSitePhotosImportParameters extends AbstractImportParameters {

	private final List<String> remoteUserIds;
	private final UserGender userGender;
	private final UserMembershipType membershipType;
	private final boolean importComments;
	private final int delayBetweenRequest;
	private final int pageQty;

	private final boolean breakImportIfAlreadyImportedPhotoFound;

	private List<RemotePhotoSiteCategory> remotePhotoSiteCategories;

	private final AbstractRemoteContentHelper remoteContentHelper;
	private final AbstractRemotePhotoSitePageContentHelper remotePhotoSitePageContentHelper;

	public RemoteSitePhotosImportParameters( final List<String> remoteUserIds, final UserGender userGender, final UserMembershipType membershipType, final boolean importComments, final int delayBetweenRequest, final int pageQty, final Language language, final boolean breakImportIfAlreadyImportedPhotoFound, final List<RemotePhotoSiteCategory> remotePhotoSiteCategories, final AbstractRemoteContentHelper remoteContentHelper, final AbstractRemotePhotoSitePageContentHelper remotePhotoSitePageContentHelper ) {
		super( language );
		this.remoteUserIds = remoteUserIds;
		this.userGender = userGender;
		this.membershipType = membershipType;
		this.importComments = importComments;
		this.delayBetweenRequest = delayBetweenRequest;
		this.pageQty = pageQty;
		this.breakImportIfAlreadyImportedPhotoFound = breakImportIfAlreadyImportedPhotoFound;
		this.remotePhotoSiteCategories = remotePhotoSiteCategories;
		this.remoteContentHelper = remoteContentHelper;
		this.remotePhotoSitePageContentHelper = remotePhotoSitePageContentHelper;
	}

	public List<String> getRemoteUserIds() {
		return remoteUserIds;
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

	public List<RemotePhotoSiteCategory> getRemotePhotoSiteCategories() {
		return remotePhotoSiteCategories;
	}

	public boolean isBreakImportIfAlreadyImportedPhotoFound() {
		return breakImportIfAlreadyImportedPhotoFound;
	}

	public AbstractRemoteContentHelper getRemoteContentHelper() {
		return remoteContentHelper;
	}

	public AbstractRemotePhotoSitePageContentHelper getRemotePhotoSitePageContentHelper() {
		return remotePhotoSitePageContentHelper;
	}
}
