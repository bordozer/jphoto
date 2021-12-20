package com.bordozer.jphoto.core.services.notification;

import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;

public class NotificationData {

    private final TranslatableMessage subject;
    private final TranslatableMessage message;

    public NotificationData(final TranslatableMessage subject, final TranslatableMessage message) {
        this.subject = subject;
        this.message = message;
    }

    public TranslatableMessage getSubject() {
        return subject;
    }

    public TranslatableMessage getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", subject, message);
    }
}
