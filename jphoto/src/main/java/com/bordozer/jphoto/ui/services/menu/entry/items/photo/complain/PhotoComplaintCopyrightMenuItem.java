package com.bordozer.jphoto.ui.services.menu.entry.items.photo.complain;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemComplaintCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.ComplaintReasonType;

public class PhotoComplaintCopyrightMenuItem extends AbstractPhotoComplaintMenuItem {

    public PhotoComplaintCopyrightMenuItem(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.PHOTO_COMPLAINT_COPYRIGHT;
    }

    @Override
    protected String getMenuItemText() {
        return "PhotoMenuItem: Complaint copyright";
    }

    @Override
    protected ComplaintReasonType getComplainReasonType() {
        return ComplaintReasonType.PHOTO_COPYRIGHT_COMPLAINT;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemComplaintCommand<Photo>(menuEntry, accessor, EntryMenuType.PHOTO, getComplainReasonType(), services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate(getMenuItemText(), getLanguage());
            }
        };
    }
}
