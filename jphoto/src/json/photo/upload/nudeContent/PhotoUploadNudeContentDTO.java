package json.photo.upload.nudeContent;

public class PhotoUploadNudeContentDTO {

	private boolean canContainsNude;
	private boolean containsNude;

	private String yesTranslated;
	private String noTranslated;

	public boolean isCanContainsNude() {
		return canContainsNude;
	}

	public void setCanContainsNude( final boolean canContainsNude ) {
		this.canContainsNude = canContainsNude;
	}

	public boolean isContainsNude() {
		return containsNude;
	}

	public void setContainsNude( final boolean containsNude ) {
		this.containsNude = containsNude;
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
