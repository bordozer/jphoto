package ui.controllers.users.card.data;

import ui.context.EnvironmentContext;
import ui.controllers.users.card.UserCardModel;

public class PersonalDataFillStrategy extends AbstractUserCardModelFillStrategy {

	public PersonalDataFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		model.setEntryMenu( userCardModelFillService.getUserMenu( getUser(), EnvironmentContext.getCurrentUser() ) );
	}
}
