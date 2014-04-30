package json.photo.appraisal;

import java.util.List;

public class PhotoAppraisalForm {

	private List<Integer> appraisalSections;
	private List<PhotoAppraisalCategory> accessibleAppraisalCategories;
	private List<Mark> accessibleMarks;

	public void setAppraisalSections( final List<Integer> appraisalSections ) {
		this.appraisalSections = appraisalSections;
	}

	public List<Integer> getAppraisalSections() {
		return appraisalSections;
	}

	public List<PhotoAppraisalCategory> getAccessibleAppraisalCategories() {
		return accessibleAppraisalCategories;
	}

	public void setAccessibleAppraisalCategories( final List<PhotoAppraisalCategory> accessibleAppraisalCategories ) {
		this.accessibleAppraisalCategories = accessibleAppraisalCategories;
	}

	public void setAccessibleMarks( final List<Mark> accessibleMarks ) {
		this.accessibleMarks = accessibleMarks;
	}

	public List<Mark> getAccessibleMarks() {
		return accessibleMarks;
	}
}
