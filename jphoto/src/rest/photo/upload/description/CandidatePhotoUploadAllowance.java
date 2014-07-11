package rest.photo.upload.description;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.system.Services;
import core.services.translator.Language;

public class CandidatePhotoUploadAllowance extends AbstractPhotoUploadAllowance {

	public CandidatePhotoUploadAllowance( final User user, final User accessor, final Language language, final Services services ) {
		super( user, accessor, language, services );
	}

	@Override
	public UserStatus getUserStatus() {
		return UserStatus.CANDIDATE;
	}

	@Override
	public int getMaxPhotoSize() {
		return services.getConfigurationService().getInt( ConfigurationKey.CANDIDATES_FILE_MAX_SIZE_KB );
	}

	@Override
	public int getDailyLimitPhotosQty() {
		return services.getConfigurationService().getInt( ConfigurationKey.CANDIDATES_PHOTOS_PER_DAY_LIMIT );
	}

	@Override
	public int getWeeklyLimitPhotosQty() {
		return services.getConfigurationService().getInt( ConfigurationKey.CANDIDATES_PHOTOS_PER_WEEK_LIMIT );
	}

	@Override
	public int getDailyLimitUploadSize() {
		return services.getConfigurationService().getInt( ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT );
	}

	@Override
	public int getWeeklyLimitUploadSize() {
		return services.getConfigurationService().getInt( ConfigurationKey.CANDIDATES_WEEKLY_FILE_SIZE_LIMIT );
	}
}
