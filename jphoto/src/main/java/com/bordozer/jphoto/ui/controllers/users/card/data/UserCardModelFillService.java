package com.bordozer.jphoto.ui.controllers.users.card.data;

import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoListFactory;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.activity.AbstractActivityStreamEntry;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardGenreInfo;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardModel;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardTabDTO;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenu;

import java.util.List;
import java.util.Map;

public interface UserCardModelFillService {

    void setUserAvatar(final UserCardModel model);

    void setUserStatistic(final UserCardModel model);

    AbstractPhotoListFactory getUserCardPhotoListBest(final User user, final User accessor);

    AbstractPhotoListFactory getUserCardPhotoListLast(final User user, final User accessor);

    AbstractPhotoListFactory getUserCardPhotoListLastAppraised(final User user, final User accessor);

    Map<Genre, UserCardGenreInfo> getUserCardGenreInfoMap(final User user, final User accessor);

    void setMarksByCategoryInfos(final UserCardModel model);

    void setUserTeam(final UserCardModel model);

    void setPhotoAlbums(final UserCardModel model);

    void setLastUserActivityTime(final UserCardModel model);

    void setUserPhotosByGenresPhotoList(final UserCardModel model);

    AbstractPhotoListFactory getUserTeamMemberLastPhotos(final User user, final UserTeamMember userTeamMember, final User accessor);

    AbstractPhotoListFactory getUserPhotoAlbumLastPhotos(final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor);

    EntryMenu getUserMenu(final User user, final User userWhoIsCallingMenu);

    List<AbstractActivityStreamEntry> getUserLastActivities(final int userId, final int activityTypeId, final PagingModel pagingModel);

    List<UserPhotoAlbum> getUserPhotoAlbums(final int userId);

    Map<Integer, Integer> setUserPhotosCountByAlbums(final int userId);

    DateUtilsService getDateUtilsService();

    List<UserCardTabDTO> getUserCardTabDTOs(final User user);
}
