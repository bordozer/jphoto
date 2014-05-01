package json.photo.appraisal;

import java.util.List;

public class PhotoAppraisalForm {

	private List<AppraisalSection> appraisalSections;

	private String appraisalText;
	private String appraisalTitle;

	private String excellentButtonText;
	private String excellentButtonTitle;

	private String goodButtonText;
	private String goodButtonTitle;

	private int userHighestPositiveMarkInGenre;

	public void setAppraisalSections( final List<AppraisalSection> appraisalSections ) {
		this.appraisalSections = appraisalSections;
	}

	public List<AppraisalSection> getAppraisalSections() {
		return appraisalSections;
	}

	public String getAppraisalText() {
		return appraisalText;
	}

	public void setAppraisalText( final String appraisalText ) {
		this.appraisalText = appraisalText;
	}

	public String getAppraisalTitle() {
		return appraisalTitle;
	}

	public void setAppraisalTitle( final String appraisalTitle ) {
		this.appraisalTitle = appraisalTitle;
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
}
