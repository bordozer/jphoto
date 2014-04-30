package json.photo.appraisal;

import java.util.List;

public class PhotoAppraisalForm {

	private List<AppraisalSection> appraisalSections;

	public void setAppraisalSections( final List<AppraisalSection> appraisalSections ) {
		this.appraisalSections = appraisalSections;
	}

	public List<AppraisalSection> getAppraisalSections() {
		return appraisalSections;
	}
}
