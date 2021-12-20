package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.controllers.jobs.edit.NoParametersAbstractJob;
import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.message.PrivateMessage;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;

import java.util.List;
import java.util.Map;

public class UserStatusRecalculationJob extends NoParametersAbstractJob {

    public UserStatusRecalculationJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {
        final DateUtilsService dateUtilsService = services.getDateUtilsService();
        final UserService userService = services.getUserService();
        final ActivityStreamService activityStreamService = services.getActivityStreamService();

        final List<User> users = userService.loadAll();

        for (final User user : users) {

            if (user.getUserStatus() == UserStatus.MEMBER) {
                increment();
                continue;
            }

            final int photoQty = services.getPhotoService().getPhotosCountByUser(user.getId());
            if (photoQty >= services.getConfigurationService().getInt(ConfigurationKey.CANDIDATES_PHOTOS_QTY_TO_BECOME_MEMBER)) {
                if (userService.setUserStatus(user.getId(), UserStatus.MEMBER)) {

                    sendSystemNotificationAboutGotMembershipToUser(user);

                    getLog().info(String.format("Member %s has got new status: %s", user, UserStatus.MEMBER.getName()));

                    final TranslatableMessage translatableMessage = new TranslatableMessage("UserStatusRecalculationJob: Member $1 has got new status: $2", services)
                            .userCardLink(user)
                            .translatableString(UserStatus.MEMBER.getName());
                    addJobRuntimeLogMessage(translatableMessage);

                    activityStreamService.saveUserStatusChange(user, UserStatus.CANDIDATE, UserStatus.MEMBER, dateUtilsService.getCurrentTime(), services);
                } else {
                    getLog().error(String.format("Can not update member status. Id = # %s", user));

                    final TranslatableMessage translatableMessage = new TranslatableMessage("UserStatusRecalculationJob: Member $1 has got new status: $2", services).userCardLink(user);
                    addJobRuntimeLogMessage(translatableMessage);
                }
            }

            increment();

            if (isFinished()) {
                break;
            }

            if (hasJobFinishedWithAnyResult()) {
                break;
            }
        }

        services.getCacheService().expire(CacheKey.USER);
        getLog().debug("User cache has been cleared");
    }

    private void sendSystemNotificationAboutGotMembershipToUser(final User user) {

        final PrivateMessage message = new PrivateMessage();

        message.setToUser(user);
        message.setCreationTime(services.getDateUtilsService().getCurrentTime());
        message.setPrivateMessageType(PrivateMessageType.SYSTEM_NOTIFICATIONS);

        final TranslatableMessage translatableMessage = new TranslatableMessage("UserStatusRecalculationJob: You have bees given a new club status: $1", services)
                .translatableString(UserStatus.MEMBER.getName());
        message.setMessageText(translatableMessage.build(getLanguage()));

        services.getPrivateMessageService().save(message);
    }

    @Override
    public void initJobParameters(final Map<SavedJobParameterKey, CommonProperty> jobParameters) {
        super.initJobParameters(jobParameters);

        totalJopOperations = services.getUserService().getUserCount();
    }

    @Override
    public SavedJobType getJobType() {
        return SavedJobType.USER_STATUS;
    }
}
