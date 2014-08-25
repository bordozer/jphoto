package core.services.photo;

import core.general.photo.Photo;
import core.general.user.User;

import java.util.List;

public interface PhotoUploadService {

	List<Integer> getUploadedTodayPhotosIds( final User user );

	List<Integer> getUploadedThisWeekPhotosIds( final User user );

	List<Photo> getUploadedThisWeekPhotos( final User user );

	long getUploadedTodayPhotosSummarySize( final User user );

	long getUploadedThisWeekPhotosSummarySize( final User user );
}
