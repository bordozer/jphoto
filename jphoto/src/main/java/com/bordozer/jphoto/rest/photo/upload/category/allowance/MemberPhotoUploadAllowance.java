package com.bordozer.jphoto.rest.photo.upload.category.allowance;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class MemberPhotoUploadAllowance extends AbstractPhotoUploadAllowance {

    public MemberPhotoUploadAllowance(final User user, final User accessor, final Language language, final Services services) {
        super(user, accessor, language, services);
    }

    @Override
    public UserStatus getUserStatus() {
        return UserStatus.MEMBER;
    }

    @Override
    public int getMaxPhotoSize() {
        return services.getConfigurationService().getInt(ConfigurationKey.MEMBERS_FILE_MAX_SIZE_KB);
    }

    @Override
    public int getDailyLimitPhotosQty() {
        return services.getConfigurationService().getInt(ConfigurationKey.MEMBERS_PHOTOS_PER_DAY_LIMIT);
    }

    @Override
    public int getWeeklyLimitPhotosQty() {
        return services.getConfigurationService().getInt(ConfigurationKey.MEMBERS_PHOTOS_PER_WEEK_LIMIT);
    }

    @Override
    public int getDailyLimitUploadSize() {
        return services.getConfigurationService().getInt(ConfigurationKey.MEMBERS_DAILY_FILE_SIZE_LIMIT);
    }

    @Override
    public int getWeeklyLimitUploadSize() {
        return services.getConfigurationService().getInt(ConfigurationKey.MEMBERS_WEEKLY_FILE_SIZE_LIMIT);
    }
}
