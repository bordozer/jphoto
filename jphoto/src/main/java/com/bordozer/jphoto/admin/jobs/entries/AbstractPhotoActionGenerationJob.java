package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.DateRangeType;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.photo.PhotoPreviewService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.RandomUtilsService;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public abstract class AbstractPhotoActionGenerationJob extends AbstractDateRangeableJob {

    private final static Integer MAX_ITERATIONS = 1000;

    protected final SavedJobType savedJobType;

    public abstract boolean doPhotoAction(final Photo photo, final User user);

    protected AbstractPhotoActionGenerationJob(final SavedJobType savedJobType, final LogHelper log, final JobRuntimeEnvironment jobEnvironment) {
        super(log, jobEnvironment);
        this.savedJobType = savedJobType;
    }

    @Override
    protected void runJob() throws Throwable {

        final RandomUtilsService randomUtilsService = services.getRandomUtilsService();

        int emptyIterations = 0;
        while (!isFinished()) {

            final Photo photo = randomUtilsService.getRandomPhotoId(beingProcessedPhotosIds);
            if (photo == null) {
                getLog().debug(String.format("Random photo can not be found. Photos count: %s", beingProcessedPhotosIds.size()));
                break;
            }

            final User actionCommitter = randomUtilsService.getRandomUser(beingProcessedUsers);

            if (doPhotoAction(photo, actionCommitter)) {
                increment();

                doActionForAnotherPhotoOfPhotoAuthor(photo.getUserId(), actionCommitter);
                doActionForPhotoOfAuthorsForWhomPhotoAuthorHasVotesRecently(photo.getUserId(), actionCommitter);
                emptyIterations = 0;
            } else {
                emptyIterations++;
            }

            if (emptyIterations > MAX_ITERATIONS) {
                getLog().debug(String.format("%s empty iterations. Stopped.", MAX_ITERATIONS));
                break;
            }

            if (hasJobFinishedWithAnyResult()) {
                break;
            }
        }
    }

    @Override
    public SavedJobType getJobType() {
        return savedJobType;
    }

    @Override
    public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

        final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

        parametersMap.put(SavedJobParameterKey.PARAM_ACTIONS_QTY, new CommonProperty(SavedJobParameterKey.PARAM_ACTIONS_QTY.getId(), totalJopOperations));
        parametersMap.put(SavedJobParameterKey.PARAM_PHOTOS_QTY, new CommonProperty(SavedJobParameterKey.PARAM_PHOTOS_QTY.getId(), photoQtyLimit));

        getDateRangeParametersMap(parametersMap);

        return parametersMap;
    }

    @Override
    public String getJobParametersDescription() {

        final StringBuilder builder = new StringBuilder();

        builder.append(translate("Photo actions job parameters: Photos action to generate")).append(": ").append(totalJopOperations).append("<br />");
        builder.append(translate("Photo actions job parameters: Photos")).append(": ").append(photoQtyLimit > 0 ? photoQtyLimit : translate("all")).append("<br />");

        builder.append(translate("Photo actions job parameters: Actions type")).append(": ").append(translate(getJobType().getName())).append("<br />");

        addDateRangeParameters(builder);

        return builder.toString();
    }

    @Override
    public void initJobParameters(final Map<SavedJobParameterKey, CommonProperty> jobParameters) {

        totalJopOperations = jobParameters.get(SavedJobParameterKey.PARAM_ACTIONS_QTY).getValueInt();
        photoQtyLimit = jobParameters.get(SavedJobParameterKey.PARAM_PHOTOS_QTY).getValueInt();

        setDateRangeParameters(jobParameters);
    }

    protected Date getPhotoActionTime(final Date photoUploadTime) {

        if (jobDateRange.getDateRangeType() == DateRangeType.CURRENT_TIME) {
            return jobDateRange.getStartDate();
        }

        Date actionTimeFrom = jobDateRange.getStartDate();
        if (jobDateRange.getStartDate().getTime() < photoUploadTime.getTime()) {
            actionTimeFrom = photoUploadTime;
        }

        return services.getRandomUtilsService().getRandomDate(actionTimeFrom, jobDateRange.getEndDate());
    }

    protected boolean savePhotoPreview(final Photo photo, final User user, final Date previewTime) {

        final PhotoPreviewService photoPreviewService = services.getPhotoPreviewService();
        if (photoPreviewService.hasUserAlreadySeenThisPhoto(photo.getId(), user.getId())) {
            return false;
        }

        final PhotoPreview photoPreview = new PhotoPreview(photo, user);
        photoPreview.setPreviewTime(previewTime);
        photoPreviewService.save(photoPreview);

        getLog().info(String.format("User %s has seen photo %s ( time: %s )", user, photo, getDateUtilsService().formatDateTime(previewTime)));

        services.getUsersSecurityService().saveLastUserActivityTime(user.getId(), previewTime);

        return true;
    }

    private DateUtilsService getDateUtilsService() {
        return services.getDateUtilsService();
    }

    private void doActionForAnotherPhotoOfPhotoAuthor(final int photoAuthorId, final User actionCommitter) {
        final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
        final User photoAuthor = services.getUserService().load(photoAuthorId);

        final boolean isHasVotedUserGoingToDoTheActionForAnotherPhotoOfPhotoAuthor = randomUtilsService.getRandomBoolean();
        if (isHasVotedUserGoingToDoTheActionForAnotherPhotoOfPhotoAuthor) {

            final TranslatableMessage translatableMessage = new TranslatableMessage("User $1 is going to do the action for another photo of $2", services)
                    .userCardLink(actionCommitter)
                    .userCardLink(photoAuthor);
            addJobRuntimeLogMessage(translatableMessage);

            final Photo randomPhotoOfPhotoAuthor = randomUtilsService.getRandomPhotoOfUser(photoAuthorId);
            if (doPhotoAction(randomPhotoOfPhotoAuthor, actionCommitter)) {
                doActionForAnotherPhotoOfPhotoAuthor(photoAuthorId, actionCommitter);
            }
        }
    }

    private void doActionForPhotoOfAuthorsForWhomPhotoAuthorHasVotesRecently(final int photoAuthorId, final User actionCommitter) {
        final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
        final PhotoService photoService = services.getPhotoService();
        final UserService userService = services.getUserService();

        final User photoAuthor = userService.load(photoAuthorId);

        final boolean isHasVotedUserGoingToDoTheActionForPhotosOfUserWhoHaveSeenAuthorPhotos = randomUtilsService.getRandomBoolean();
        if (isHasVotedUserGoingToDoTheActionForPhotosOfUserWhoHaveSeenAuthorPhotos) {
            final List<Integer> lastVotedPhotosIds = photoService.getLastVotedPhotosIds(photoAuthor, 4, actionCommitter);

            if (lastVotedPhotosIds == null || lastVotedPhotosIds.size() == 0) {
                return;
            }

            final Photo randomPhotoFromLastVoted = randomUtilsService.getRandomPhotoId(lastVotedPhotosIds);
            final int authorIdOfRandomPhotoFromLastVoted = randomPhotoFromLastVoted.getUserId();
            final Photo randomPhoto = randomUtilsService.getRandomPhotoOfUser(authorIdOfRandomPhotoFromLastVoted);

            final User authorOfRandomPhotoFromLastVoted = userService.load(authorIdOfRandomPhotoFromLastVoted);

            final TranslatableMessage translatableMessage = new TranslatableMessage("User $1 is going to do the action for photo of $2 because $3 has voted for his photos recently", services)
                    .userCardLink(actionCommitter)
                    .userCardLink(authorOfRandomPhotoFromLastVoted)
                    .userCardLink(photoAuthor);
            addJobRuntimeLogMessage(translatableMessage);

            if (doPhotoAction(randomPhoto, actionCommitter)) {
                doActionForPhotoOfAuthorsForWhomPhotoAuthorHasVotesRecently(authorIdOfRandomPhotoFromLastVoted, actionCommitter);
            }
        }
    }
}
