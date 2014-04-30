package json.photo.appraisal;

import java.util.List;

public class PhotoAppraisalForm {

	private List<AppraisalSection> appraisalSections;

	private String appraisalText;
	private String appraisalTitle;

	private String maxAppraisalText;
	private String maxAppraisalTitle;

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

	public String getMaxAppraisalText() {
		return maxAppraisalText;
	}

	public void setMaxAppraisalText( final String maxAppraisalText ) {
		this.maxAppraisalText = maxAppraisalText;
	}

	public String getMaxAppraisalTitle() {
		return maxAppraisalTitle;
	}

	public void setMaxAppraisalTitle( final String maxAppraisalTitle ) {
		this.maxAppraisalTitle = maxAppraisalTitle;
	}
}
