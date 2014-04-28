package ui.controllers.portalpage;

import core.general.data.UserRating;
import core.services.translator.TranslatorService;
import ui.activity.AbstractActivityStreamEntry;

import java.util.List;

public class PortalPageModel {

	public static final int TOP_BEST_USERS_QTY = 10;

	private List<PortalPagePhoto> lastUploadedPhotos;
	private List<PortalPagePhoto> bestPhotos;

	private List<UserRating> bestWeekUserRating;
	private List<UserRating> bestMonthUserRating;

	private List<PortalPageGenre> portalPageGenres;

	private int bestPhotosMinMarks;
	private int bestPhotosPeriod;

	private int randomBestPhotoArrayIndex;

	private List<AbstractActivityStreamEntry> lastActivities;

	private TranslatorService translatorService;

	public List<PortalPagePhoto> getLastUploadedPhotos() {
		return lastUploadedPhotos;
	}

	public void setLastUploadedPhotos( final List<PortalPagePhoto> lastUploadedPhotos ) {
		this.lastUploadedPhotos = lastUploadedPhotos;
	}

	public List<PortalPagePhoto> getBestPhotos() {
		return bestPhotos;
	}

	public void setBestPhotos( final List<PortalPagePhoto> bestPhotos ) {
		this.bestPhotos = bestPhotos;
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
