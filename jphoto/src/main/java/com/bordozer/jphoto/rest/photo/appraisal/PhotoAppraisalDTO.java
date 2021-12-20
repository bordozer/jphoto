package com.bordozer.jphoto.rest.photo.appraisal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhotoAppraisalDTO {

    private int userId;
    private int photoId;
    private int currentUserId;

    private String appraisalBlockTitle;

    private boolean userCanAppraiseThePhoto;
    private String userCanNotAppraiseThePhotoReason;

    private boolean userHasAlreadyAppraisedPhoto;

    private PhotoAppraisalForm photoAppraisalForm;

    private List<PhotoAppraisalResult> photoAppraisalResults;
    private String userCanNotAppraiseThePhotoText;
    private String appraisalSaveCallbackMessage;

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public void setPhotoId(final int photoId) {
        this.photoId = photoId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(final int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public boolean isUserHasAlreadyAppraisedPhoto() {
        return userHasAlreadyAppraisedPhoto;
    }

    public void setUserHasAlreadyAppraisedPhoto(final boolean userHasAlreadyAppraisedPhoto) {
        this.userHasAlreadyAppraisedPhoto = userHasAlreadyAppraisedPhoto;
    }

    public boolean isUserCanAppraiseThePhoto() {
        return userCanAppraiseThePhoto;
    }

    public void setUserCanAppraiseThePhoto(final boolean userCanAppraiseThePhoto) {
        this.userCanAppraiseThePhoto = userCanAppraiseThePhoto;
    }

    public String getUserCanNotAppraiseThePhotoReason() {
        return userCanNotAppraiseThePhotoReason;
    }

    public void setUserCanNotAppraiseThePhotoReason(final String userCanNotAppraiseThePhotoReason) {
        this.userCanNotAppraiseThePhotoReason = userCanNotAppraiseThePhotoReason;
    }

    public PhotoAppraisalForm getPhotoAppraisalForm() {
        return photoAppraisalForm;
    }

    public void setPhotoAppraisalForm(final PhotoAppraisalForm photoAppraisalForm) {
        this.photoAppraisalForm = photoAppraisalForm;
    }

    public void setAppraisalBlockTitle(final String appraisalBlockTitle) {
        this.appraisalBlockTitle = appraisalBlockTitle;
    }

    public String getAppraisalBlockTitle() {
        return appraisalBlockTitle;
    }

    public List<PhotoAppraisalResult> getPhotoAppraisalResults() {
        return photoAppraisalResults;
    }

    public void setPhotoAppraisalResults(final List<PhotoAppraisalResult> photoAppraisalResults) {
        this.photoAppraisalResults = photoAppraisalResults;
    }

    public void setUserCanNotAppraiseThePhotoText(final String userCanNotAppraiseThePhotoText) {
        this.userCanNotAppraiseThePhotoText = userCanNotAppraiseThePhotoText;
    }

    public String getUserCanNotAppraiseThePhotoText() {
        return userCanNotAppraiseThePhotoText;
    }

    public void setAppraisalSaveCallbackMessage(final String appraisalSaveCallbackMessage) {
        this.appraisalSaveCallbackMessage = appraisalSaveCallbackMessage;
    }

    public String getAppraisalSaveCallbackMessage() {
        return appraisalSaveCallbackMessage;
    }
}
