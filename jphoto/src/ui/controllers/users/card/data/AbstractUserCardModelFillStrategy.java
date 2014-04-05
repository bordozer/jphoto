package ui.controllers.users.card.data;

import ui.controllers.users.card.UserCardModel;
import core.enums.UserCardTab;
import core.general.base.PagingModel;
import core.general.user.User;

public abstract class AbstractUserCardModelFillStrategy {

	protected final UserCardModel model;
	protected final UserCardModelFillService userCardModelFillService;

	public abstract void performCustomActions();

	public void fillModel() {
		userCardModelFillService.setUserAvatar( model );

		performCustomActions();
	}

	protected AbstractUserCardModelFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		this.model = model;
		this.userCardModelFillService = userCardModelFillService;
	}

	public static AbstractUserCardModelFillStrategy getInstance( final UserCardModel model, final UserCardTab userCardTab, final PagingModel pagingModel, final UserCardModelFillService userCardModelFillService ) {
		switch ( userCardTab ) {
			case BRIEF_USER__OVERVIEW:
				return new BriefOverviewFillStrategy( model, userCardModelFillService );
			case PERSONAL_DATA:
				return new PersonalDataFillStrategy( model, userCardModelFillService );
			case PHOTOS_OVERVIEW:
				return new PhotosFillStrategy( model, userCardModelFillService );
			case STATISTICS:
				return new StatisticFillStrategy( model, userCardModelFillService );
			case TEAM:
				return new TeamFillStrategy( model, userCardModelFillService );
			case ALBUMS:
				return new AlbumFillStrategy( model, userCardModelFillService );
			case ACTIVITY_STREAM:
				return new ActivityStreamStrategy( model, pagingModel, userCardModelFillService );
		}

		throw new IllegalArgumentException( String.format( "Incorrect UserCardTab: %s", userCardTab ) );
	}

	protected User getUser() {
		return model.getUser();
	}

	protected int getUserId() {
		return model.getUser().getId();
	}
}