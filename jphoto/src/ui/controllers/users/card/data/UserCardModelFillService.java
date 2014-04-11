package ui.controllers.users.card.data;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.menus.EntryMenu;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import elements.PhotoList;
import ui.activity.AbstractActivityStreamEntry;
import ui.controllers.users.card.UserCardGenreInfo;
import ui.controllers.users.card.UserCardModel;

import java.util.List;
import java.util.Map;

public interface UserCardModelFillService {

	void setUserAvatar( final UserCardModel model );

	void setUserStatistic( final UserCardModel model );

	PhotoList getBestUserPhotoList( final User user );

	PhotoList getLastUserPhotoList( final User user );

	PhotoList getLastVotedPhotoList( final User user );

	PhotoList getLastPhotosOfUserVisitors( final User user );

	Map<Genre,UserCardGenreInfo> getUserCardGenreInfoMap( final User user, final User accessor );

	void setMarksByCategoryInfos( final UserCardModel model );

	void setUserTeam( final UserCardModel model );

	void setPhotoAlbums( final UserCardModel model );

	void setLastUserActivityTime( final UserCardModel model );

	void setUserPhotosByGenresPhotoList( final UserCardModel model );

	PhotoList getUserTeamMemberLastPhotos( final int userId, final UserTeamMember userTeamMember, final Map<UserTeamMember, Integer> teamMemberPhotosQtyMap );

	PhotoList getUserPhotoAlbumLastPhotos( final int userId, final UserPhotoAlbum userPhotoAlbum, final Map<UserPhotoAlbum, Integer> userPhotoAlbumsQtyMap );

	EntryMenu getUserMenu( final User user, final User userWhoIsCallingMenu );

	List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int activityTypeId, final PagingModel pagingModel );
}
