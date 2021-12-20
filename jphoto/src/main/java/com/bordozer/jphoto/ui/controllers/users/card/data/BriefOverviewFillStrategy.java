package com.bordozer.jphoto.ui.controllers.users.card.data;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardModel;
import com.bordozer.jphoto.ui.elements.PhotoList;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class BriefOverviewFillStrategy extends AbstractUserCardModelFillStrategy {

    public BriefOverviewFillStrategy(final UserCardModel model, final UserCardModelFillService userCardModelFillService) {
        super(model, userCardModelFillService);
    }

    @Override
    public void performCustomActions() {
        final User user = getUser();

        final User currentUser = EnvironmentContext.getCurrentUser();
        final Date currentTime = userCardModelFillService.getDateUtilsService().getCurrentTime();
        final Language language = EnvironmentContext.getLanguage();

        final List<PhotoList> photoLists = newArrayList();

        photoLists.add(userCardModelFillService.getUserCardPhotoListBest(user, currentUser).getPhotoList(3, 1, language, currentTime));
        photoLists.add(userCardModelFillService.getUserCardPhotoListLast(user, currentUser).getPhotoList(4, 1, language, currentTime));
        photoLists.add(userCardModelFillService.getUserCardPhotoListLastAppraised(user, currentUser).getPhotoList(4, 1, language, currentTime));

        model.setPhotoLists(photoLists);

        model.setUserCardGenreInfoMap(userCardModelFillService.getUserCardGenreInfoMap(model.getUser(), getCurrentUser()));

        userCardModelFillService.setLastUserActivityTime(model);
        model.setEntryMenu(userCardModelFillService.getUserMenu(user, getCurrentUser()));
    }
}
