package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;

import java.util.Date;

public class PhotoActionGenerationPreviewsJob extends AbstractPhotoActionGenerationJob {

    public PhotoActionGenerationPreviewsJob(final JobRuntimeEnvironment jobEnvironment) {
        super(SavedJobType.ACTIONS_GENERATION_VIEWS, new LogHelper(), jobEnvironment);
    }

    @Override
    public boolean doPhotoAction(final Photo photo, final User user) {
        final Date actionTime = getPhotoActionTime(photo.getUploadTime());

        final TranslatableMessage translatableMessage = new TranslatableMessage("User $1 has seen photo $2 ( time: $3 )", services)
                .userCardLink(user)
                .addPhotoCardLinkParameter(photo)
                .dateTimeFormatted(actionTime);
        addJobRuntimeLogMessage(translatableMessage);

        return savePhotoPreview(photo, user, actionTime);
    }
}
