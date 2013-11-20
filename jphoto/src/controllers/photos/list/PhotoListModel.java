package controllers.photos.list;

import core.general.base.AbstractGeneralModel;
import core.general.user.User;
import core.general.user.UserPhotosByGenre;
import elements.PhotoList;
import org.springframework.mobile.device.DeviceType;
import utils.PhotoUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoListModel extends AbstractGeneralModel {

	private DeviceType deviceType;
	private List<PhotoList> photoLists = newArrayList();

	private User user;
	private List<UserPhotosByGenre> userPhotosByGenres;

	public List<PhotoList> getPhotoLists() {
		return photoLists;
	}

	public void addPhotoList( final PhotoList photoList ) {
		photoLists.add( photoList );
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

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public List<UserPhotosByGenre> getUserPhotosByGenres() {
		return userPhotosByGenres;
	}

	public void setUserPhotosByGenres( final List<UserPhotosByGenre> userPhotosByGenres ) {
		this.userPhotosByGenres = userPhotosByGenres;
	}
}
