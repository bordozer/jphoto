package com.bordozer.jphoto.core.interfaces;

import org.springframework.validation.BindingResult;

public interface GeneralModel {

    BindingResult getBindingResult();

    void setBindingResult(final BindingResult bindingResult);

    void clear();
}
