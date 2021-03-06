package com.bordozer.jphoto.core.general.restriction;

import com.bordozer.jphoto.admin.controllers.restriction.entry.RestrictionEntryType;
import com.bordozer.jphoto.core.enums.RestrictionType;
import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.interfaces.Restrictable;

import java.util.Date;

public class EntryRestriction<T extends Restrictable> extends AbstractBaseEntity {

    private final T entry;
    private final RestrictionType restrictionType;

    private Date restrictionTimeFrom;
    private Date restrictionTimeTo;

    private String restrictionMessage;
    private String restrictionRestrictionComment;

    private boolean active;
    private Date creatingTime;
    private User creator;

    private User canceller;
    private Date cancellingTime;

    public EntryRestriction(final T entry, final RestrictionType restrictionType) {
        this.entry = entry;
        this.restrictionType = restrictionType;
    }

    public RestrictionEntryType getRestrictionEntryType() {
        if (RestrictionType.FOR_USERS.contains(restrictionType)) {
            return RestrictionEntryType.USER;
        }

        if (RestrictionType.FOR_PHOTOS.contains(restrictionType)) {
            return RestrictionEntryType.PHOTO;
        }

        throw new IllegalArgumentException(String.format("Illegal restrictionType: %s", restrictionType));
    }

    public T getEntry() {
        return entry;
    }

    public RestrictionType getRestrictionType() {
        return restrictionType;
    }

    public Date getRestrictionTimeFrom() {
        return restrictionTimeFrom;
    }

    public void setRestrictionTimeFrom(final Date restrictionTimeFrom) {
        this.restrictionTimeFrom = restrictionTimeFrom;
    }

    public Date getRestrictionTimeTo() {
        return restrictionTimeTo;
    }

    public void setRestrictionTimeTo(final Date restrictionTimeTo) {
        this.restrictionTimeTo = restrictionTimeTo;
    }

    public String getRestrictionMessage() {
        return restrictionMessage;
    }

    public void setRestrictionMessage(final String restrictionMessage) {
        this.restrictionMessage = restrictionMessage;
    }

    public String getRestrictionRestrictionComment() {
        return restrictionRestrictionComment;
    }

    public void setRestrictionRestrictionComment(final String restrictionRestrictionComment) {
        this.restrictionRestrictionComment = restrictionRestrictionComment;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCancelled() {
        return !active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Date getCreatingTime() {
        return creatingTime;
    }

    public void setCreatingTime(final Date creatingTime) {
        this.creatingTime = creatingTime;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(final User creator) {
        this.creator = creator;
    }

    public User getCanceller() {
        return canceller;
    }

    public void setCanceller(final User canceller) {
        this.canceller = canceller;
    }

    public Date getCancellingTime() {
        return cancellingTime;
    }

    public void setCancellingTime(final Date cancellingTime) {
        this.cancellingTime = cancellingTime;
    }

    @Override
    public String toString() {
        return String.format("#%d %s: %s from %s to %s", getId(), entry, restrictionType, restrictionTimeFrom, restrictionTimeTo);
    }
}
