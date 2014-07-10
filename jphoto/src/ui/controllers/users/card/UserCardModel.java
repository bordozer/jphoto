package ui.controllers.users.card;

import core.enums.UserCardTab;
import core.general.base.AbstractGeneralModel;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserAvatar;
import core.general.user.UserPhotosByGenre;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import org.springframework.mobile.device.DeviceType;
import ui.activity.AbstractActivityStreamEntry;
import ui.elements.PhotoList;
import ui.services.menu.entry.items.EntryMenu;
import utils.PhotoUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class UserCardModel extends AbstractGeneralModel {

	private User user;

	private PhotoList bestUserPhotoList;
	private PhotoList lastUserPhotoList;
	private PhotoList lastVotedPhotoList;
	private PhotoList lastPhotosOfUserVisitors;

	private List<PhotoList> photoLists;

	private Map<Genre, UserCardGenreInfo> userCardGenreInfoMap = newHashMap();
	private List<MarksByCategoryInfo> marksByCategoryInfos;
	private UserAvatar userAvatar;

	private DeviceType deviceType;

	private UserStatistic userStatistic;

	private UserTeam userTeam;
	private Map<UserTeamMember, Integer> teamMemberPhotosQtyMap;
	private List<PhotoList> userTeamMemberPhotoLists;
	private List<PhotoList> userPhotoAlbumsPhotoLists;

	private List<UserPhotoAlbum> userPhotoAlbums;
	private Map<UserPhotoAlbum, Integer> userPhotoAlbumsQtyMap;

	private boolean editingUserDataIsAccessible;
	private Date lastUserActivityTime;

	private UserCardTab selectedUserCardTab;
	private List<UserPhotosByGenre> userPhotosByGenres;

	private EntryMenu entryMenu;

	private List<AbstractActivityStreamEntry> userLastActivities;

	private int filterActivityTypeId;

	public User getUser() {
		return user;
	}

	public void setUser( User user ) {
		this.user = user;
	}

	public PhotoList getBestUserPhotoList() {
		return bestUserPhotoList;
	}

	public void setBestUserPhotoList( final PhotoList bestUserPhotoList ) {
		this.bestUserPhotoList = bestUserPhotoList;
	}

	public PhotoList getLastUserPhotoList() {
		return lastUserPhotoList;
	}

	public void setLastUserPhotoList( PhotoList lastUserPhotoList ) {
		this.lastUserPhotoList = lastUserPhotoList;
	}

	public PhotoList getLastVotedPhotoList() {
		return lastVotedPhotoList;
	}

	public void setLastVotedPhotoList( final PhotoList lastVotedPhotoList ) {
		this.lastVotedPhotoList = lastVotedPhotoList;
	}

	public void setLastPhotosOfUserVisitors( final PhotoList lastPhotosOfUserVisitors ) {
		this.lastPhotosOfUserVisitors = lastPhotosOfUserVisitors;
	}

	public PhotoList getLastPhotosOfUserVisitors() {
		return lastPhotosOfUserVisitors;
	}

	public Map<Genre, UserCardGenreInfo> getUserCardGenreInfoMap() {
		return userCardGenreInfoMap;
	}

	public void setUserCardGenreInfoMap( final Map<Genre, UserCardGenreInfo> userCardGenreInfoMap ) {
		this.userCardGenreInfoMap = userCardGenreInfoMap;
	}

	public List<MarksByCategoryInfo> getMarksByCategoryInfos() {
		return marksByCategoryInfos;
	}

	public void setMarksByCategoryInfos( final List<MarksByCategoryInfo> marksByCategoryInfos ) {
		this.marksByCategoryInfos = marksByCategoryInfos;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType( final DeviceType deviceType ) {
		this.deviceType = deviceType;
	}

	public boolean isMobileDevice() {
		return PhotoUtils.isMobileDevice( deviceType );
	}

	public UserAvatar getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar( final UserAvatar userAvatar ) {
		this.userAvatar = userAvatar;
	}

	public UserStatistic getUserStatistic() {
		return userStatistic;
	}

	public void setUserStatistic( final UserStatistic userStatistic ) {
		this.userStatistic = userStatistic;
	}

	public void setUserTeam( final UserTeam userTeam ) {
		this.userTeam = userTeam;
	}

	public UserTeam getUserTeam() {
		return userTeam;
	}

	public void setTeamMemberPhotosQtyMap( final Map<UserTeamMember,Integer> teamMemberPhotosQtyMap ) {
		this.teamMemberPhotosQtyMap = teamMemberPhotosQtyMap;
	}

	public Map<UserTeamMember, Integer> getTeamMemberPhotosQtyMap() {
		return teamMemberPhotosQtyMap;
	}

	public List<PhotoList> getUserTeamMemberPhotoLists() {
		return userTeamMemberPhotoLists;
	}

	public void setUserTeamMemberPhotoLists( final List<PhotoList> userTeamMemberPhotoLists ) {
		this.userTeamMemberPhotoLists = userTeamMemberPhotoLists;
	}

	public List<PhotoList> getUserPhotoAlbumsPhotoLists() {
		return userPhotoAlbumsPhotoLists;
	}

	public void setUserPhotoAlbumsPhotoLists( final List<PhotoList> userPhotoAlbumsPhotoLists ) {
		this.userPhotoAlbumsPhotoLists = userPhotoAlbumsPhotoLists;
	}

	public List<UserPhotoAlbum> getUserPhotoAlbums() {
		return userPhotoAlbums;
	}

	public void setUserPhotoAlbums( final List<UserPhotoAlbum> userPhotoAlbums ) {
		this.userPhotoAlbums = userPhotoAlbums;
	}

	public Map<UserPhotoAlbum, Integer> getUserPhotoAlbumsQtyMap() {
		return userPhotoAlbumsQtyMap;
	}

	public void setUserPhotoAlbumsQtyMap( final Map<UserPhotoAlbum, Integer> userPhotoAlbumsQtyMap ) {
		this.userPhotoAlbumsQtyMap = userPhotoAlbumsQtyMap;
	}

	public boolean isEditingUserDataIsAccessible() {
		return editingUserDataIsAccessible;
	}

	public void setEditingUserDataIsAccessible( final boolean editingUserDataIsAccessible ) {
		this.editingUserDataIsAccessible = editingUserDataIsAccessible;
	}

	public Date getLastUserActivityTime() {
		return lastUserActivityTime;
	}

	public void setLastUserActivityTime( final Date lastUserActivityTime ) {
		this.lastUserActivityTime = lastUserActivityTime;
	}

	public UserCardTab getSelectedUserCardTab() {
		return selectedUserCardTab;
	}

	public void setSelectedUserCardTab( final UserCardTab selectedUserCardTab ) {
		this.selectedUserCardTab = selectedUserCardTab;
	}

	public List<PhotoList> getPhotoLists() {
		return photoLists;
	}

	public void setPhotoLists( final List<PhotoList> photoLists ) {
		this.photoLists = photoLists;
	}

	public List<UserPhotosByGenre> getUserPhotosByGenres() {
		return userPhotosByGenres;
	}

	public void setUserPhotosByGenres( final List<UserPhotosByGenre> userPhotosByGenres ) {
		this.userPhotosByGenres = userPhotosByGenres;
	}

	public EntryMenu getEntryMenu() {
		return entryMenu;
	}

	public void setEntryMenu( final EntryMenu entryMenu ) {
		this.entryMenu = entryMenu;
	}

	public List<AbstractActivityStreamEntry> getUserLastActivities() {
		return userLastActivities;
	}

	public void setUserLastActivities( final List<AbstractActivityStreamEntry> userLastActivities ) {
		this.userLastActivities = userLastActivities;
	}

	public int getFilterActivityTypeId() {
		return filterActivityTypeId;
	}

	public void setFilterActivityTypeId( final int filterActivityTypeId ) {
		this.filterActivityTypeId = filterActivityTypeId;
	}
}
