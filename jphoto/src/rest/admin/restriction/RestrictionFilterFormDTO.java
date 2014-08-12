package rest.admin.restriction;

import java.util.List;

public class RestrictionFilterFormDTO {

	private List<RestrictionHistoryEntryDTO> searchResultEntryDTOs;
	private List<String> selectedRestrictionTypeIds;

	public List<RestrictionHistoryEntryDTO> getSearchResultEntryDTOs() {
		return searchResultEntryDTOs;
	}

	public void setSearchResultEntryDTOs( final List<RestrictionHistoryEntryDTO> searchResultEntryDTOs ) {
		this.searchResultEntryDTOs = searchResultEntryDTOs;
	}

	public List<String> getSelectedRestrictionTypeIds() {
		return selectedRestrictionTypeIds;
	}

	public void setSelectedRestrictionTypeIds( final List<String> selectedRestrictionTypeIds ) {
		this.selectedRestrictionTypeIds = selectedRestrictionTypeIds;
	}
}
