package com.bordozer.jphoto.rest.photo.upload.category.allowance;

public class PhotoUploadDescription {

    private String uploadRuleDescription;
    private boolean passed = true;

    public String getUploadRuleDescription() {
        return uploadRuleDescription;
    }

    public void setUploadRuleDescription(final String uploadRuleDescription) {
        this.uploadRuleDescription = uploadRuleDescription;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(final boolean passed) {
        this.passed = passed;
    }
}
