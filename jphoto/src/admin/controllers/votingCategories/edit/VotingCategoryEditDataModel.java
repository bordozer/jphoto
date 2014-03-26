package admin.controllers.votingCategories.edit;

import core.general.base.AbstractGeneralModel;
import core.services.validation.DataRequirementService;

public class VotingCategoryEditDataModel extends AbstractGeneralModel {

	public static final String VOTING_CATEGORIES_ID_FORM_CONTROL = "votingCategoryId";
	public final static String VOTING_CATEGORIES_NAME_FORM_CONTROL = "votingCategoryName";
	public final static String VOTING_CATEGORIES_DESCRIPTION_FORM_CONTROL = "votingCategoryDescription";

	private DataRequirementService dataRequirementService;

	private int votingCategoryId;
	private String votingCategoryName;
	private String votingCategoryDescription;

	@Override
	public void clear() {
		super.clear();

		votingCategoryName = null;
		votingCategoryDescription = null;
	}

	public int getVotingCategoryId() {
		return votingCategoryId;
	}

	public void setVotingCategoryId( final int votingCategoryId ) {
		this.votingCategoryId = votingCategoryId;
	}

	public String getVotingCategoryName() {
		return votingCategoryName;
	}

	public void setVotingCategoryName( final String votingCategoryName ) {
		this.votingCategoryName = votingCategoryName;
	}

	public String getVotingCategoryDescription() {
		return votingCategoryDescription;
	}

	public void setVotingCategoryDescription( final String votingCategoryDescription ) {
		this.votingCategoryDescription = votingCategoryDescription;
	}

	public DataRequirementService getDataRequirementService() {
		return dataRequirementService;
	}

	public void setDataRequirementService( final DataRequirementService dataRequirementService ) {
		this.dataRequirementService = dataRequirementService;
	}
}
