package com.bordozer.jphoto.ui.services.page.icons;

import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.services.system.Services;

public class UnreadPrivateMessagesTitleIcon extends AbstractTitleIcon {

    private PrivateMessageType messageType;
    private int unreadMessagesCount;

    public UnreadPrivateMessagesTitleIcon(final PrivateMessageType messageType, final int unreadMessagesCount, final Services services) {
        super(services);
        this.messageType = messageType;
        this.unreadMessagesCount = unreadMessagesCount;
    }

    @Override
    protected String getIconPath() {
        return String.format("messages/%s", messageType.getIcon());
    }

    @Override
    protected String getIconTitle() {
        return String.format("%s: +%d", getTranslatorService().translate(messageType.getName(), getLanguage()), unreadMessagesCount);
    }

    @Override
    protected String getIconUrl() {
        return getUrlUtilsService().getPrivateMessagesList(getCurrentUser().getId(), messageType);
    }

    @Override
    protected String getIconText() {
        return String.format("+%d", unreadMessagesCount);
    }
}
