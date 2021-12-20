package com.bordozer.jphoto.rest.photo.upload.category;

public class PhotoUploadNudeContentDTO {

    private boolean genreCanContainsNude;
    private boolean genreObviouslyContainsNude;

    private String yesTranslated;
    private String noTranslated;

    public boolean isGenreCanContainsNude() {
        return genreCanContainsNude;
    }

    public void setGenreCanContainsNude(final boolean genreCanContainsNude) {
        this.genreCanContainsNude = genreCanContainsNude;
    }

    public boolean isGenreObviouslyContainsNude() {
        return genreObviouslyContainsNude;
    }

    public void setGenreObviouslyContainsNude(final boolean genreObviouslyContainsNude) {
        this.genreObviouslyContainsNude = genreObviouslyContainsNude;
    }

    public String getYesTranslated() {
        return yesTranslated;
    }

    public void setYesTranslated(final String yesTranslated) {
        this.yesTranslated = yesTranslated;
    }

    public String getNoTranslated() {
        return noTranslated;
    }

    public void setNoTranslated(final String noTranslated) {
        this.noTranslated = noTranslated;
    }
}
