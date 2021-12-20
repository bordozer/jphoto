package com.bordozer.jphoto.ui.controllers.users.card.data;

import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardModel;

public class ActivityStreamStrategy extends AbstractUserCardModelFillStrategy {

    private final PagingModel pagingModel;

    public ActivityStreamStrategy(final UserCardModel model, final PagingModel pagingModel, final UserCardModelFillService userCardModelFillService) {
        super(model, userCardModelFillService);

        this.pagingModel = pagingModel;
    }

    @Override
    public void performCustomActions() {
        model.setUserLastActivities(userCardModelFillService.getUserLastActivities(getUserId(), model.getFilterActivityTypeId(), pagingModel));
    }
}
