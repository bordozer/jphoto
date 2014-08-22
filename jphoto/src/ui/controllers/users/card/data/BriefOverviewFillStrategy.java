package ui.controllers.users.card.data;

import core.general.user.User;
import core.services.translator.Language;
import ui.context.EnvironmentContext;
import ui.controllers.users.card.UserCardModel;
import ui.elements.PhotoList;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BriefOverviewFillStrategy extends AbstractUserCardModelFillStrategy {

	public BriefOverviewFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		final User user = getUser();

		final User currentUser = EnvironmentContext.getCurrentUser();
		final Date currentTime = userCardModelFillService.getDateUtilsService().getCurrentTime();
		final Language language = EnvironmentContext.getLanguage();

		final List<PhotoList> photoLists = newArrayList();

		photoLists.add( userCardModelFillService.getUserPhotoListBest( user, currentUser ).getPhotoList( 3, 1, language, currentTime ) );
		photoLists.add( userCardModelFillService.getUserPhotoListLast( user, currentUser ).getPhotoList( 4, 1, language, currentTime ) );
		photoLists.add( userCardModelFillService.getPhotoListLastAppraised( user, currentUser ).getPhotoList( 4, 1, language, currentTime ) );

		model.setPhotoLists( photoLists );

		model.setUserCardGenreInfoMap( userCardModelFillService.getUserCardGenreInfoMap( model.getUser(), getCurrentUser() ) );

		userCardModelFillService.setLastUserActivityTime( model );
		model.setEntryMenu( userCardModelFillService.getUserMenu( user, getCurrentUser() ) );
	}
}
