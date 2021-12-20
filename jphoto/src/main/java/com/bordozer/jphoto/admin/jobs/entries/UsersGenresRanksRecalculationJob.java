package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.controllers.jobs.edit.NoParametersAbstractJob;
import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.message.PrivateMessage;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;

import java.util.List;
import java.util.Map;

public class UsersGenresRanksRecalculationJob extends NoParametersAbstractJob {

    public UsersGenresRanksRecalculationJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {

        final List<Integer> genreIds = services.getGenreService().load(services.getBaseSqlUtilsService().getGenresIdsSQL()).getIds();

        final UserRankService userRankService = services.getUserRankService();
        final ActivityStreamService activityStreamService = services.getActivityStreamService();
        final DateUtilsService dateUtilsService = services.getDateUtilsService();

        for (final User user : beingProcessedUsers) {
            final int userId = user.getId();

            for (final int genreId : genreIds) {


                final int userCurrentRank = userRankService.getUserRankInGenre(userId, genreId);
                final int userNewRank = userRankService.calculateUserRankInGenre(userId, genreId);

                final boolean isUserCanVote = userNewRank != userCurrentRank; // TODO: add conf settings checking and move to the service
                if (isUserCanVote) {
                    userRankService.saveUserRankForGenre(userId, genreId, userNewRank);

                    final Genre genre = services.getGenreService().load(genreId);

                    sendSystemNotificationAboutGotNewRankToUser(user, userCurrentRank, userNewRank, genre);

                    getLog().info(String.format("User %s has bees given a new rank %s in %s (the previous one was %s)", user, userNewRank, genre, userCurrentRank));

                    final TranslatableMessage translatableMessage = new TranslatableMessage("User $1 has bees given a new rank $2 in $3 ( the previous one was $4 )", services)
                            .userCardLink(user)
                            .addIntegerParameter(userNewRank)
                            .addPhotosByUserByGenreLinkParameter(user, genre)
                            .addIntegerParameter(userCurrentRank);
                    addJobRuntimeLogMessage(translatableMessage);

                    activityStreamService.saveUserRankInGenreChanged(user, genre, userCurrentRank, userNewRank, dateUtilsService.getCurrentTime(), services);
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

        services.getCacheService().expire(CacheKey.PHOTO_INFO);

        getLog().debug("Photo info cache has been cleared");
    }

    private void sendSystemNotificationAboutGotNewRankToUser(final User user, final int userCurrentRank, final int userNewRank, final Genre genre) {

        final PrivateMessage message = new PrivateMessage();

        message.setToUser(user);
        message.setCreationTime(services.getDateUtilsService().getCurrentTime());
        message.setPrivateMessageType(PrivateMessageType.SYSTEM_NOTIFICATIONS);

        final TranslatableMessage translatableMessage = new TranslatableMessage("UsersGenresRanksRecalculationJob: You have bees given a new rank $1 in category $2 ( the previous one was $3 )", services)
                .addIntegerParameter(userNewRank)
                .string(services.getEntityLinkUtilsService().getPhotosByGenreLink(genre, getLanguage()))
                .addIntegerParameter(userCurrentRank);
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
        return SavedJobType.USER_GENRES_RANKS_RECALCULATING;
    }
}
