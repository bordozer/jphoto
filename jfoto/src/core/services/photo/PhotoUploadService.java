package core.services.photo;

import core.general.photo.Photo;

import java.util.List;

public interface PhotoUploadService {

	List<Integer> getUploadedTodayPhotosIds( final int userId );

	List<Integer> getUploadedThisWeekPhotosIds( final int userId );

	List<Photo> getUploadedThisWeekPhotos( final int userId );

	long getUploadedTodayPhotosSummarySize( final int userId );

	long getUploadedThisWeekPhotosSummarySize( final int userId );
}
