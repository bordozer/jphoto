package ui.controllers.photos.DEL_edit.description;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.general.user.UserStatus;

public class MemberPhotoUploadHelper extends AbstractPhotoUploadAllowance {

	public MemberPhotoUploadHelper( final User user, final User accessor ) {
		super( user, accessor );
	}

	@Override
	public UserStatus getUserStatus() {
		return UserStatus.MEMBER;
	}

	@Override
	public int getMaxPhotoSize() {
		return configurationService.getInt( ConfigurationKey.MEMBERS_FILE_MAX_SIZE_KB );
	}

	@Override
	public int getDailyLimitPhotosQty() {
		return configurationService.getInt( ConfigurationKey.MEMBERS_PHOTOS_PER_DAY_LIMIT );
	}

	@Override
	public int getWeeklyLimitPhotosQty() {
		return configurationService.getInt( ConfigurationKey.MEMBERS_PHOTOS_PER_WEEK_LIMIT );
	}

	@Override
	public int getDailyLimitUploadSize() {
		return configurationService.getInt( ConfigurationKey.MEMBERS_DAILY_FILE_SIZE_LIMIT );
	}

	@Override
	public int getWeeklyLimitUploadSize() {
		return configurationService.getInt( ConfigurationKey.MEMBERS_WEEKLY_FILE_SIZE_LIMIT );
	}
}
