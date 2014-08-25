package admin.services.jobs;

import core.general.photo.Photo;

import java.util.Date;

public interface JobHelperService {

	Date getFirstPhotoUploadTime();

	boolean getAnonymousOption( final int userId, final int genreId, final Date uploadTime );

	void initPhotoNudeContentOption( final Photo photo );

	boolean doesUserPhotoExist( final int userId, final int importId );
}
