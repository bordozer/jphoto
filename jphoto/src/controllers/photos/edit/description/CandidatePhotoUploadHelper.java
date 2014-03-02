package controllers.photos.edit.description;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.general.user.UserStatus;

public class CandidatePhotoUploadHelper extends AbstractPhotoUploadAllowance {

	public CandidatePhotoUploadHelper( final User user ) {
		super( user );
	}

	@Override
	public UserStatus getUserStatus() {
		return UserStatus.CANDIDATE;
	}

	@Override
	public int getMaxPhotoSize() {
		return configurationService.getInt( ConfigurationKey.CANDIDATES_FILE_MAX_SIZE_KB );
	}

	@Override
	public int getDailyLimitPhotosQty() {
		return configurationService.getInt( ConfigurationKey.CANDIDATES_PHOTOS_PER_DAY_LIMIT );
	}

	@Override
	public int getWeeklyLimitPhotosQty() {
		return configurationService.getInt( ConfigurationKey.CANDIDATES_PHOTOS_PER_WEEK_LIMIT );
	}

	@Override
	public int getDailyLimitUploadSize() {
		return configurationService.getInt( ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT );
	}

	@Override
	public int getWeeklyLimitUploadSize() {
		return configurationService.getInt( ConfigurationKey.CANDIDATES_WEEKLY_FILE_SIZE_LIMIT );
	}
}
