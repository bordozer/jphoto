package controllers.users.card.data;

import controllers.users.card.UserCardModel;
import core.general.base.PagingModel;

public class PhotosFillStrategy extends AbstractUserCardModelFillStrategy {

	public PhotosFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		userCardModelFillService.setUserPhotosByGenresPhotoList( model );
	}
}
