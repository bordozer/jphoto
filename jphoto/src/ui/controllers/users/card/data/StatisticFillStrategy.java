package ui.controllers.users.card.data;

import ui.controllers.users.card.UserCardModel;

public class StatisticFillStrategy extends AbstractUserCardModelFillStrategy {

	public StatisticFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		userCardModelFillService.setUserStatistic( model );
		userCardModelFillService.setMarksByCategoryInfos( model );

		userCardModelFillService.setPhotoAlbums( model );
		userCardModelFillService.setUserTeam( model );
	}
}
