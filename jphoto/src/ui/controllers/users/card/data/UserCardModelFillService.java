package ui.controllers.users.card.data;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.photo.list.factory.AbstractPhotoListFactory;
import core.services.utils.DateUtilsService;
import ui.activity.AbstractActivityStreamEntry;
import ui.controllers.users.card.UserCardGenreInfo;
import ui.controllers.users.card.UserCardModel;
import ui.elements.PhotoList;
import ui.services.menu.entry.items.EntryMenu;

import java.util.List;
import java.util.Map;

public interface UserCardModelFillService {

	void setUserAvatar( final UserCardModel model );

	void setUserStatistic( final UserCardModel model );

	AbstractPhotoListFactory getBestUserPhotoList( final User user, final User accessor );

	PhotoList getLastUserPhotoList( final User user );

	PhotoList getLastVotedPhotoList( final User user );

	PhotoList getLastPhotosOfUserVisitors( final User user );

	Map<Genre,UserCardGenreInfo> getUserCardGenreInfoMap( final User user, final User accessor );

	void setMarksByCategoryInfos( final UserCardModel model );

	void setUserTeam( final UserCardModel model );

	void setPhotoAlbums( final UserCardModel model );

	void setLastUserActivityTime( final UserCardModel model );

	void setUserPhotosByGenresPhotoList( final UserCardModel model );

	AbstractPhotoListFactory getUserTeamMemberLastPhotos( final User user, final UserTeamMember userTeamMember, final User accessor );

	AbstractPhotoListFactory getUserPhotoAlbumLastPhotos( final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor );

	EntryMenu getUserMenu( final User user, final User userWhoIsCallingMenu );

	List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int activityTypeId, final PagingModel pagingModel );

	List<UserPhotoAlbum> getUserPhotoAlbums( final int userId );

	Map<Integer, Integer> setUserPhotosCountByAlbums( final int userId );

	DateUtilsService getDateUtilsService();
}
