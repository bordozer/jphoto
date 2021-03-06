package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.photo.PhotoImageLocationType;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.translator.Language;

import java.util.List;

public class RemoteSitePhotosImportParameters extends AbstractImportParameters {

    private PhotosImportSource importSource;

    private final List<String> remoteUserIds;
    private final boolean importComments;
    private final int delayBetweenRequest;

    private final int pageQty;

    private final boolean breakImportIfAlreadyImportedPhotoFound;
    private List<RemotePhotoSiteCategory> remotePhotoSiteCategories;

    private final UserGender userGender;
    private final UserMembershipType membershipType;

    private final PhotoImageLocationType photoImageLocationType;

    public RemoteSitePhotosImportParameters(final PhotosImportSource importSource, final List<String> remoteUserIds, final UserGender userGender, final UserMembershipType membershipType, final boolean importComments, final int delayBetweenRequest, final int pageQty, final Language language, final boolean breakImportIfAlreadyImportedPhotoFound, final List<RemotePhotoSiteCategory> remotePhotoSiteCategories, final PhotoImageLocationType photoImageLocationType) {
        super(language);

        this.importSource = importSource;

        this.remoteUserIds = remoteUserIds;
        this.userGender = userGender;
        this.membershipType = membershipType;
        this.importComments = importComments;
        this.delayBetweenRequest = delayBetweenRequest;
        this.pageQty = pageQty;
        this.breakImportIfAlreadyImportedPhotoFound = breakImportIfAlreadyImportedPhotoFound;
        this.remotePhotoSiteCategories = remotePhotoSiteCategories;
        this.photoImageLocationType = photoImageLocationType;
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

    public PhotosImportSource getImportSource() {
        return importSource;
    }

    public PhotoImageLocationType getPhotoImageLocationType() {
        return photoImageLocationType;
    }
}
