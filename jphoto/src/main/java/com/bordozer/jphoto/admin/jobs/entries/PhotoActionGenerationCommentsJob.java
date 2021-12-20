package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.entries.resources.FakePhotoCommentLoader;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoActionGenerationCommentsJob extends AbstractPhotoActionGenerationJob {

    private enum CommentType {COMMENT, ANSWER}

    public PhotoActionGenerationCommentsJob(final JobRuntimeEnvironment jobEnvironment) {
        super(SavedJobType.ACTIONS_GENERATION_COMMENTS, new LogHelper(), jobEnvironment);
    }

    @Override
    public boolean doPhotoAction(final Photo photo, final User user) {
        final Date actionTime = getPhotoActionTime(photo.getUploadTime());

        final Language language = getLanguage();
        if (services.getSecurityService().validateUserCanCommentPhoto(user, photo, actionTime, language).isValidationFailed()) {
            return false;
        }

        final PhotoActionAllowance commentsAllowance = photo.getCommentsAllowance();
        if (commentsAllowance == PhotoActionAllowance.ACTIONS_DENIED) {
            return false;
        }

        if (commentsAllowance == PhotoActionAllowance.MEMBERS_ONLY && user.getUserStatus() == UserStatus.CANDIDATE) {
            return false;
        }

        final PhotoComment comment = new PhotoComment();
        comment.setPhotoId(photo.getId());
        comment.setCommentAuthor(user);
        comment.setCreationTime(actionTime);
        comment.setCommentText(FakePhotoCommentLoader.getRandomFakeComment(services));

        if (isGoingToBeAnswer()) {
            final int replyToCommentId = getParentComment(photo, user, actionTime);
            if (replyToCommentId > 0) {
                comment.setReplyToCommentId(replyToCommentId);
            }
        }

        savePhotoPreview(photo, user, actionTime);

        services.getPhotoCommentService().save(comment);

        final DateUtilsService dateUtilsService = services.getDateUtilsService();

        final TranslatableMessage translatableMessage = new TranslatableMessage("User $1 has left a comment for photo $2 ( time: $3 )", services)
                .userCardLink(user)
                .addPhotoCardLinkParameter(photo)
                .dateTimeFormatted(actionTime);
        addJobRuntimeLogMessage(translatableMessage);

        getLog().info(String.format("User %s has commented photo %s ( time: %s )", user, photo, dateUtilsService.formatDateTime(actionTime)));

        return true;
    }

    private int getParentComment(final Photo photo, final User user, final Date beingCreatedCommentTime) {
        final List<PhotoComment> comments = services.getPhotoCommentService().loadAll(photo.getId());

        if (comments.size() == 0) {
            return 0;
        }

        final List<PhotoComment> commentsLeftBeforeBeingCreatedComment = newArrayList();
        for (final PhotoComment comment : comments) {
            if (!comment.getCommentAuthor().equals(user) && comment.getCreationTime().getTime() < beingCreatedCommentTime.getTime()) {
                commentsLeftBeforeBeingCreatedComment.add(comment);
            }
        }

        if (commentsLeftBeforeBeingCreatedComment.size() == 0) {
            return 0;
        }

        final int randomParentCommentIndex = services.getRandomUtilsService().getRandomInt(0, commentsLeftBeforeBeingCreatedComment.size() - 1);

        return commentsLeftBeforeBeingCreatedComment.get(randomParentCommentIndex).getId();
    }

    private boolean isGoingToBeAnswer() {
        return services.getRandomUtilsService().getRandomGenericArrayElement(commentTypesArray) == CommentType.ANSWER;
    }

    final CommentType[] commentTypesArray = {CommentType.COMMENT, CommentType.COMMENT, CommentType.COMMENT, CommentType.ANSWER};
}
