package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web;

public class RemoteUser {

    private final String id;
    private String name;

    public RemoteUser(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Photosight user #%s: %s", id, name);
    }
}
