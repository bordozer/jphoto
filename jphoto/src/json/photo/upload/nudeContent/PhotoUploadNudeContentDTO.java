package json.photo.upload.nudeContent;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PhotoUploadNudeContentDTO {

	private boolean genreCanContainsNude;
	private boolean genreObviouslyContainsNude;

	private boolean photoContainsNude;

	private String yesTranslated;
	private String noTranslated;

	public boolean isGenreCanContainsNude() {
		return genreCanContainsNude;
	}

	public void setGenreCanContainsNude( final boolean genreCanContainsNude ) {
		this.genreCanContainsNude = genreCanContainsNude;
	}

	public boolean isGenreObviouslyContainsNude() {
		return genreObviouslyContainsNude;
	}

	public void setGenreObviouslyContainsNude( final boolean genreObviouslyContainsNude ) {
		this.genreObviouslyContainsNude = genreObviouslyContainsNude;
	}

	public boolean isPhotoContainsNude() {
		return photoContainsNude;
	}

	public void setPhotoContainsNude( final boolean photoContainsNude ) {
		this.photoContainsNude = photoContainsNude;
	}

	public String getYesTranslated() {
		return yesTranslated;
	}

	public void setYesTranslated( final String yesTranslated ) {
		this.yesTranslated = yesTranslated;
	}

	public String getNoTranslated() {
		return noTranslated;
	}

	public void setNoTranslated( final String noTranslated ) {
		this.noTranslated = noTranslated;
	}
}
