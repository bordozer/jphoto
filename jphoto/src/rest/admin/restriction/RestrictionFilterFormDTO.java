package rest.admin.restriction;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RestrictionFilterFormDTO {

	private List<RestrictionHistoryEntryDTO> searchResultEntryDTOs;

	private List<CheckboxDTO> restrictionTypesUser;
	private List<CheckboxDTO> restrictionTypesPhoto;
	private List<CheckboxDTO> restrictionStatuses;

	public List<RestrictionHistoryEntryDTO> getSearchResultEntryDTOs() {
		return searchResultEntryDTOs;
	}

	public void setSearchResultEntryDTOs( final List<RestrictionHistoryEntryDTO> searchResultEntryDTOs ) {
		this.searchResultEntryDTOs = searchResultEntryDTOs;
	}

	public List<CheckboxDTO> getRestrictionTypesUser() {
		return restrictionTypesUser;
	}

	public void setRestrictionTypesUser( final List<CheckboxDTO> restrictionTypesUser ) {
		this.restrictionTypesUser = restrictionTypesUser;
	}

	public List<CheckboxDTO> getRestrictionTypesPhoto() {
		return restrictionTypesPhoto;
	}

	public void setRestrictionTypesPhoto( final List<CheckboxDTO> restrictionTypesPhoto ) {
		this.restrictionTypesPhoto = restrictionTypesPhoto;
	}

	public List<CheckboxDTO> getRestrictionStatuses() {
		return restrictionStatuses;
	}

	public void setRestrictionStatuses( final List<CheckboxDTO> restrictionStatuses ) {
		this.restrictionStatuses = restrictionStatuses;
	}
}
