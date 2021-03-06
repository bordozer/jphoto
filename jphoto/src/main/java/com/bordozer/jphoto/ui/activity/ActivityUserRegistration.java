package com.bordozer.jphoto.ui.activity;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import org.dom4j.Document;

public class ActivityUserRegistration extends AbstractActivityStreamEntry {

    public ActivityUserRegistration(final User user, final Services services) {
        super(user, user.getRegistrationTime(), ActivityType.USER_REGISTRATION, services);
    }

    @Override
    public Document getActivityXML() {
        return getEmptyDocument();
    }

    @Override
    protected TranslatableMessage getActivityTranslatableText() {
        return new TranslatableMessage("user registration activity stream: registered", services);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getActivityType(), activityOfUser);
    }
}
