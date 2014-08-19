package rest.admin.restriction;

import java.util.List;

public class RestrictionFilterFormDTO {

	private List<RestrictionHistoryEntryDTO> searchResultEntryDTOs;

	public List<RestrictionHistoryEntryDTO> getSearchResultEntryDTOs() {
		return searchResultEntryDTOs;
	}

	public void setSearchResultEntryDTOs( final List<RestrictionHistoryEntryDTO> searchResultEntryDTOs ) {
		this.searchResultEntryDTOs = searchResultEntryDTOs;
	}
}
