package com.bordozer.jphoto.rest.admin.restriction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestrictionHistoryEntryDTO {

    private int id;

    private String dateFrom;
    private String timeFrom;

    private String dateTo;
    private String timeTo;

    private String restrictionDuration;
    private String expiresAfter;

    private String restrictionName;
    private String restrictionIcon;

    private String creatorLink;
    private String creationDate;
    private String creationTime;

    private boolean active;
    private boolean finished;

    private String cancellerLink;
    private String cancellingDate;
    private String cancellingTime;

    private String cssClass;
    private String wasRestricted;

    private String status;
    private String entryLink;
    private String entryLinkUrl;

    private String entryImage;
    private String restrictionEntryTypeName;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(final String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(final String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(final String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(final String timeTo) {
        this.timeTo = timeTo;
    }

    public String getRestrictionDuration() {
        return restrictionDuration;
    }

    public void setRestrictionDuration(final String restrictionDuration) {
        this.restrictionDuration = restrictionDuration;
    }

    public String getExpiresAfter() {
        return expiresAfter;
    }

    public void setExpiresAfter(final String expiresAfter) {
        this.expiresAfter = expiresAfter;
    }

    public String getRestrictionName() {
        return restrictionName;
    }

    public void setRestrictionName(final String restrictionName) {
        this.restrictionName = restrictionName;
    }

    public String getRestrictionIcon() {
        return restrictionIcon;
    }

    public void setRestrictionIcon(final String restrictionIcon) {
        this.restrictionIcon = restrictionIcon;
    }

    public String getCreatorLink() {
        return creatorLink;
    }

    public void setCreatorLink(final String creatorLink) {
        this.creatorLink = creatorLink;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final String creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(final boolean finished) {
        this.finished = finished;
    }

    public String getCancellerLink() {
        return cancellerLink;
    }

    public void setCancellerLink(final String cancellerLink) {
        this.cancellerLink = cancellerLink;
    }

    public String getCancellingDate() {
        return cancellingDate;
    }

    public void setCancellingDate(final String cancellingDate) {
        this.cancellingDate = cancellingDate;
    }

    public String getCancellingTime() {
        return cancellingTime;
    }

    public void setCancellingTime(final String cancellingTime) {
        this.cancellingTime = cancellingTime;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(final String cssClass) {
        this.cssClass = cssClass;
    }

    public void setWasRestricted(final String wasRestricted) {
        this.wasRestricted = wasRestricted;
    }

    public String getWasRestricted() {
        return wasRestricted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setEntryLink(final String entryLink) {
        this.entryLink = entryLink;
    }

    public String getEntryLink() {
        return entryLink;
    }

    public String getEntryLinkUrl() {
        return entryLinkUrl;
    }

    public void setEntryLinkUrl(final String entryLinkUrl) {
        this.entryLinkUrl = entryLinkUrl;
    }

    public String getEntryImage() {
        return entryImage;
    }

    public void setEntryImage(final String entryImage) {
        this.entryImage = entryImage;
    }

    public void setRestrictionEntryTypeName(final String restrictionEntryTypeName) {
        this.restrictionEntryTypeName = restrictionEntryTypeName;
    }

    public String getRestrictionEntryTypeName() {
        return restrictionEntryTypeName;
    }

    @Override
    public String toString() {
        return String.format("#%d %s: from %s to %s", id, restrictionName, timeFrom, timeTo);
    }
}
