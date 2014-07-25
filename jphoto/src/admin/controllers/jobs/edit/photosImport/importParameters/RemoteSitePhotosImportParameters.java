package admin.controllers.jobs.edit.photosImport.importParameters;

import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
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

	private List<PhotosightCategory> remotePhotoSiteCategories;

	private final AbstractRemoteContentHelper remoteContentHelper;
	private final AbstractRemotePhotoSitePageContentDataExtractor remotePhotoSitePageContentDataExtractor;

	public RemoteSitePhotosImportParameters( final List<String> remoteUserIds, final UserGender userGender, final UserMembershipType membershipType, final boolean importComments, final int delayBetweenRequest, final int pageQty, final Language language, final boolean breakImportIfAlreadyImportedPhotoFound, final List<PhotosightCategory> remotePhotoSiteCategories, final AbstractRemoteContentHelper remoteContentHelper, final AbstractRemotePhotoSitePageContentDataExtractor remotePhotoSitePageContentDataExtractor ) {
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
		this.remotePhotoSitePageContentDataExtractor = remotePhotoSitePageContentDataExtractor;
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

	public List<PhotosightCategory> getRemotePhotoSiteCategories() {
		return remotePhotoSiteCategories;
	}

	public boolean isBreakImportIfAlreadyImportedPhotoFound() {
		return breakImportIfAlreadyImportedPhotoFound;
	}

	public AbstractRemoteContentHelper getRemoteContentHelper() {
		return remoteContentHelper;
	}

	public AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSitePageContentDataExtractor() {
		return remotePhotoSitePageContentDataExtractor;
	}
}
