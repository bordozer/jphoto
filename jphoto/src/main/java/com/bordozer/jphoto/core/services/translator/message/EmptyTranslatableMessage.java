package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.services.translator.Language;

public class EmptyTranslatableMessage extends TranslatableMessage {

    public EmptyTranslatableMessage() {
        super("", null);
    }

    @Override
    public String build(final Language language) {
        return "";
    }
}
