package com.bordozer.jphoto.ui.elements;

public class PageTitleData {

    final private String title;
    final private String header;
    final private String breadcrumbs;

    public PageTitleData(String title, String header, String breadcrumbs) {
        this.title = title;
        this.header = header;
        this.breadcrumbs = breadcrumbs;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public String getBreadcrumbs() {
        return breadcrumbs;
    }
}
