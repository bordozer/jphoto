package controllers.users.card.data;

import controllers.users.card.UserCardModel;
import core.general.base.PagingModel;

public class ActivityStreamStrategy extends AbstractUserCardModelFillStrategy {

	private final PagingModel pagingModel;

	public ActivityStreamStrategy( final UserCardModel model, final PagingModel pagingModel, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );

		this.pagingModel = pagingModel;
	}

	@Override
	public void performCustomActions() {
		model.setUserLastActivities( userCardModelFillService.getUserLastActivities( getUserId(), pagingModel ) );
	}
}
