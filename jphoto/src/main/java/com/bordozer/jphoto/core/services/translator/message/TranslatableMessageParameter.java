package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class TranslatableMessageParameter extends AbstractTranslatableMessageParameter {

    private TranslatableMessage translatableMessage;

    protected TranslatableMessageParameter(final TranslatableMessage translatableMessage, final Services services) {
        super(services);

        this.translatableMessage = translatableMessage;
    }

    @Override
    public String getValue(final Language language) {
        return translatableMessage.build(language);
    }
}
