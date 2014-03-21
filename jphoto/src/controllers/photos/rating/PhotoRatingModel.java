package controllers.photos.rating;

import core.general.base.AbstractGeneralModel;
import core.general.photo.PhotoInfo;
import elements.PhotoList;

import java.util.List;

public class PhotoRatingModel extends AbstractGeneralModel {

	private String selectedPhotoRatingRank;
	private List<String> photoRatingRanks;

	private PhotoList photoList;

	public String getSelectedPhotoRatingRank() {
		return selectedPhotoRatingRank;
	}

	public void setSelectedPhotoRatingRank( final String selectedPhotoRatingRank ) {
		this.selectedPhotoRatingRank = selectedPhotoRatingRank;
	}

	public List<String> getPhotoRatingRanks() {
		return photoRatingRanks;
	}

	public void setPhotoRatingRanks( final List<String> photoRatingRanks ) {
		this.photoRatingRanks = photoRatingRanks;
	}

	public PhotoList getPhotoList() {
		return photoList;
	}

	public void setPhotoList( final PhotoList photoList ) {
		this.photoList = photoList;
	}
}
