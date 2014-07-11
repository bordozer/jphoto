package rest.photo.appraisal;

import java.util.List;

public class PhotoAppraisalForm {

	private List<AppraisalSection> appraisalSections;

	private String customButtonText;
	private String customButtonTitle;

	private String goodButtonText;
	private String goodButtonTitle;

	private String excellentButtonText;
	private String excellentButtonTitle;

	private int userHighestPositiveMarkInGenre;
	private int goodButtonMark;

	public void setAppraisalSections( final List<AppraisalSection> appraisalSections ) {
		this.appraisalSections = appraisalSections;
	}

	public List<AppraisalSection> getAppraisalSections() {
		return appraisalSections;
	}

	public String getCustomButtonText() {
		return customButtonText;
	}

	public void setCustomButtonText( final String customButtonText ) {
		this.customButtonText = customButtonText;
	}

	public String getCustomButtonTitle() {
		return customButtonTitle;
	}

	public void setCustomButtonTitle( final String customButtonTitle ) {
		this.customButtonTitle = customButtonTitle;
	}

	public String getExcellentButtonText() {
		return excellentButtonText;
	}

	public void setExcellentButtonText( final String excellentButtonText ) {
		this.excellentButtonText = excellentButtonText;
	}

	public String getExcellentButtonTitle() {
		return excellentButtonTitle;
	}

	public void setExcellentButtonTitle( final String excellentButtonTitle ) {
		this.excellentButtonTitle = excellentButtonTitle;
	}

	public String getGoodButtonText() {
		return goodButtonText;
	}

	public void setGoodButtonText( final String goodButtonText ) {
		this.goodButtonText = goodButtonText;
	}

	public String getGoodButtonTitle() {
		return goodButtonTitle;
	}

	public void setGoodButtonTitle( final String goodButtonTitle ) {
		this.goodButtonTitle = goodButtonTitle;
	}

	public void setUserHighestPositiveMarkInGenre( final int userHighestPositiveMarkInGenre ) {
		this.userHighestPositiveMarkInGenre = userHighestPositiveMarkInGenre;
	}

	public int getUserHighestPositiveMarkInGenre() {
		return userHighestPositiveMarkInGenre;
	}

	public void setGoodButtonMark( final int goodButtonMark ) {
		this.goodButtonMark = goodButtonMark;
	}

	public int getGoodButtonMark() {
		return goodButtonMark;
	}
}
