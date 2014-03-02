package controllers.users.card.data;

import controllers.users.card.UserCardModel;
import core.context.EnvironmentContext;

public class PersonalDataFillStrategy extends AbstractUserCardModelFillStrategy {

	public PersonalDataFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		model.setEntryMenu( userCardModelFillService.getUserMenu( getUser(), EnvironmentContext.getCurrentUser() ) );
	}
}
