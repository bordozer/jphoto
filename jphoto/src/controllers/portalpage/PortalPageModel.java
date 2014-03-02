package controllers.portalpage;

import core.general.activity.AbstractActivityStreamEntry;
import core.general.data.UserRating;
import core.services.translator.TranslatorService;
import elements.PhotoList;

import java.util.List;

public class PortalPageModel {

	public static final int TOP_BEST_USERS_QTY = 10;

	private PhotoList lastUploadedPhotoList;
	private PhotoList theBestPhotoList;
	private List<UserRating> bestWeekUserRating;
	private List<UserRating> bestMonthUserRating;

	private List<PortalPageGenre> portalPageGenres;

	private int bestPhotosMinMarks;
	private int bestPhotosPeriod;

	private int randomBestPhotoArrayIndex;

	private List<AbstractActivityStreamEntry> lastActivities;

	private TranslatorService translatorService;

	public PhotoList getLastUploadedPhotoList() {
		return lastUploadedPhotoList;
	}

	public void setLastUploadedPhotoList( PhotoList lastUploadedPhotoList ) {
		this.lastUploadedPhotoList = lastUploadedPhotoList;
	}

	public PhotoList getTheBestPhotoList() {
		return theBestPhotoList;
	}

	public void setTheBestPhotoList( PhotoList theBestPhotoList ) {
		this.theBestPhotoList = theBestPhotoList;
	}

	public List<UserRating> getBestWeekUserRating() {
		return bestWeekUserRating;
	}

	public void setBestWeekUserRating( final List<UserRating> bestWeekUserRating ) {
		this.bestWeekUserRating = bestWeekUserRating;
	}

	public List<UserRating> getBestMonthUserRating() {
		return bestMonthUserRating;
	}

	public void setBestMonthUserRating( final List<UserRating> bestMonthUserRating ) {
		this.bestMonthUserRating = bestMonthUserRating;
	}

	public List<PortalPageGenre> getPortalPageGenres() {
		return portalPageGenres;
	}

	public void setPortalPageGenres( final List<PortalPageGenre> portalPageGenres ) {
		this.portalPageGenres = portalPageGenres;
	}

	public int getBestPhotosMinMarks() {
		return bestPhotosMinMarks;
	}

	public void setBestPhotosMinMarks( final int bestPhotosMinMarks ) {
		this.bestPhotosMinMarks = bestPhotosMinMarks;
	}

	public int getBestPhotosPeriod() {
		return bestPhotosPeriod;
	}

	public void setBestPhotosPeriod( final int bestPhotosPeriod ) {
		this.bestPhotosPeriod = bestPhotosPeriod;
	}

	public int getRandomBestPhotoArrayIndex() {
		return randomBestPhotoArrayIndex;
	}

	public void setRandomBestPhotoArrayIndex( final int randomBestPhotoArrayIndex ) {
		this.randomBestPhotoArrayIndex = randomBestPhotoArrayIndex;
	}

	public List<AbstractActivityStreamEntry> getLastActivities() {
		return lastActivities;
	}

	public void setLastActivities( final List<AbstractActivityStreamEntry> lastActivities ) {
		this.lastActivities = lastActivities;
	}

	public TranslatorService getTranslatorService() {
		return translatorService;
	}

	public void setTranslatorService( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
	}
}
