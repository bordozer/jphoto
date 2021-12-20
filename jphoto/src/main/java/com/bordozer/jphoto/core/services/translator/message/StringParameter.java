package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class StringParameter extends AbstractTranslatableMessageParameter {

    private final String value;

    public StringParameter(final String value, final Services services) {
        super(services);

        this.value = value;
    }

    @Override
    public String getValue(final Language language) {
        return value;
    }
}
