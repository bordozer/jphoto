package admin.controllers.votingCategories.edit;

import core.general.base.AbstractGeneralModel;
import core.general.photo.PhotoVotingCategory;
import core.services.validation.DataRequirementService;

public class VotingCategoryEditDataModel extends AbstractGeneralModel {

	public static final String VOTING_CATEGORIES_ID_FORM_CONTROL = "categoryId";
	public final static String VOTING_CATEGORIES_NAME_FORM_CONTROL = "name";
	public final static String VOTING_CATEGORIES_DESCRIPTION_FORM_CONTROL = "description";

	private PhotoVotingCategory photoVotingCategory;
	private DataRequirementService dataRequirementService;

	public PhotoVotingCategory getPhotoVotingCategory() {
		return photoVotingCategory;
	}

	public void setPhotoVotingCategory( final PhotoVotingCategory photoVotingCategory ) {
		this.photoVotingCategory = photoVotingCategory;
	}

	@Override
	public void clear() {
		super.clear();

		photoVotingCategory = null;
	}

	public int getCategoryId() {
		return photoVotingCategory.getId();
	}

	public void setCategoryId( final int categoryId ) {
		photoVotingCategory.setId( categoryId );
	}

	public String getName() {
		return photoVotingCategory.getName();
	}

	public void setName( final String name ) {
		photoVotingCategory.setName( name );
	}

	public String getDescription() {
		return photoVotingCategory.getDescription();
	}

	public void setDescription( final String description ) {
		photoVotingCategory.setDescription( description );
	}

	public DataRequirementService getDataRequirementService() {
		return dataRequirementService;
	}

	public void setDataRequirementService( final DataRequirementService dataRequirementService ) {
		this.dataRequirementService = dataRequirementService;
	}
}
