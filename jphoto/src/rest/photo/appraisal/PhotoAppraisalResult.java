package rest.photo.appraisal;

public class PhotoAppraisalResult {

	private String appraisalCategoryNameTranslated;
	private String mark;
	private String maxAccessibleMark;

	public String getAppraisalCategoryNameTranslated() {
		return appraisalCategoryNameTranslated;
	}

	public void setAppraisalCategoryNameTranslated( final String appraisalCategoryNameTranslated ) {
		this.appraisalCategoryNameTranslated = appraisalCategoryNameTranslated;
	}

	public void setMark( final String mark ) {
		this.mark = mark;
	}

	public String getMark() {
		return mark;
	}

	public void setMaxAccessibleMark( final String maxAccessibleMark ) {
		this.maxAccessibleMark = maxAccessibleMark;
	}

	public String getMaxAccessibleMark() {
		return maxAccessibleMark;
	}

	@Override
	public String toString() {
		return String.format( "%s ( %s / %s )", appraisalCategoryNameTranslated, mark, maxAccessibleMark );
	}
}
