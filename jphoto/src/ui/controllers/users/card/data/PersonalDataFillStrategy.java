package ui.controllers.users.card.data;

import ui.controllers.users.card.UserCardModel;
import ui.context.EnvironmentContext;

public class PersonalDataFillStrategy extends AbstractUserCardModelFillStrategy {

	public PersonalDataFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		model.setEntryMenu( userCardModelFillService.getUserMenu( getUser(), EnvironmentContext.getCurrentUser() ) );
	}
}
