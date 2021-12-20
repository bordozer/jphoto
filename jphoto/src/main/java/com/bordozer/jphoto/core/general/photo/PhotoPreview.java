package com.bordozer.jphoto.core.general.photo;

import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.general.user.User;

import java.util.Date;

public class PhotoPreview extends AbstractBaseEntity {

    private final Photo photo;
    private final User user;
    private Date previewTime;

    public PhotoPreview(final Photo photo, final User user) {
        this.photo = photo;
        this.user = user;
    }

    public Photo getPhoto() {
        return photo;
    }

    public User getUser() {
        return user;
    }

    public Date getPreviewTime() {
        return previewTime;
    }

    public void setPreviewTime(final Date previewTime) {
        this.previewTime = previewTime;
    }
}
