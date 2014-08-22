package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.general.photo.Photo;
import core.general.user.User;
import core.log.LogHelper;
import core.services.translator.message.TranslatableMessage;

import java.util.Date;

public class PhotoActionGenerationPreviewsJob extends AbstractPhotoActionGenerationJob {

	public PhotoActionGenerationPreviewsJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( SavedJobType.ACTIONS_GENERATION_VIEWS, new LogHelper( PhotoActionGenerationPreviewsJob.class ), jobEnvironment );
	}

	@Override
	public boolean doPhotoAction( final Photo photo, final User user ) {
		final Date actionTime = getPhotoActionTime( photo.getUploadTime() );

		final TranslatableMessage translatableMessage = new TranslatableMessage( "User $1 has seen photo $2 ( time: $3 )", services )
			.userCardLink( user )
			.addPhotoCardLinkParameter( photo )
			.dateTimeFormatted( actionTime )
			;
		addJobRuntimeLogMessage( translatableMessage );

		return savePhotoPreview( photo, user, actionTime );
	}
}
