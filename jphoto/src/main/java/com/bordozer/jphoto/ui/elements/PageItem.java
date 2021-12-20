package com.bordozer.jphoto.ui.elements;

public class PageItem {

    private final int number;
    private String title;

    public PageItem(final int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public static PageItem getSeparatorPage() {
        return new PageItem(0);
    }

    @Override
    public int hashCode() {
        return 31 * number;
    }

    @Override
    public String toString() {
        return String.format("Page %d", number);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PageItem)) {
            return false;
        }

        final PageItem pageItem = (PageItem) obj;
        return pageItem.getNumber() == number;
    }
}
