package admin.controllers.jobs.edit.rankVoting;

import admin.controllers.jobs.edit.AbstractAdminJobModel;

public class RankVotingJobModel extends AbstractAdminJobModel {

	public final static String ACTIONS_QTY_FORM_CONTROL = "actionsQty";

	private String actionsQty;

	public String getActionsQty() {
		return actionsQty;
	}

	public void setActionsQty( final String actionsQty ) {
		this.actionsQty = actionsQty;
	}
}
