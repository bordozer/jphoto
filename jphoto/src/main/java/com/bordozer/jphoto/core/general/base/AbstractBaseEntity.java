package com.bordozer.jphoto.core.general.base;

import com.bordozer.jphoto.core.interfaces.BaseEntity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public abstract class AbstractBaseEntity implements BaseEntity {

    private int id;

    protected AbstractBaseEntity() {
    }

    protected AbstractBaseEntity(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == 0;
    }

    protected Document getEmptyDocument() {
        return DocumentHelper.createDocument();
    }
}
