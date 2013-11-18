package controllers.users.card.data;

import core.context.EnvironmentContext;
import controllers.users.card.UserCardModel;
import core.general.base.PagingModel;

public class PersonalDataFillStrategy extends AbstractUserCardModelFillStrategy {

	public PersonalDataFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		model.setEntryMenu( userCardModelFillService.getUserMenu( getUser(), EnvironmentContext.getCurrentUser() ) );
	}
}
