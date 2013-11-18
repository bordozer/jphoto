package controllers.users.card.data;

import controllers.users.card.UserCardModel;
import core.general.base.PagingModel;

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
