package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport;

public class RemotePhotoSiteSeries {

    private final int id;
    private String name;

    public RemotePhotoSiteSeries(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
