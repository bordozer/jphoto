package rest.admin.restriction;

import java.util.List;

public class FilterDTO {

	private List<String> selectedRestrictionTypeIds;

	public List<String> getSelectedRestrictionTypeIds() {
		return selectedRestrictionTypeIds;
	}

	public void setSelectedRestrictionTypeIds( final List<String> selectedRestrictionTypeIds ) {
		this.selectedRestrictionTypeIds = selectedRestrictionTypeIds;
	}
}
