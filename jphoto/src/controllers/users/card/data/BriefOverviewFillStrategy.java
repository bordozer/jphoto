package controllers.users.card.data;

import controllers.users.card.UserCardModel;
import core.context.EnvironmentContext;
import core.general.user.User;
import elements.PhotoList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BriefOverviewFillStrategy extends AbstractUserCardModelFillStrategy {

	public BriefOverviewFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		final User user = getUser();

		final List<PhotoList> photoLists = newArrayList();
		photoLists.add( userCardModelFillService.getBestUserPhotoList( user ) );
		photoLists.add( userCardModelFillService.getLastUserPhotoList( user ) );
		photoLists.add( userCardModelFillService.getLastVotedPhotoList( user ) );
		photoLists.add( userCardModelFillService.getLastPhotosOfUserVisitors( user ) );
		model.setPhotoLists( photoLists );

		model.setUserCardGenreInfoMap( userCardModelFillService.getUserCardGenreInfoMap( model.getUser(), EnvironmentContext.getCurrentUser() ) );

		userCardModelFillService.setLastUserActivityTime( model );
		model.setEntryMenu( userCardModelFillService.getUserMenu( user, EnvironmentContext.getCurrentUser() ) );
	}
}
