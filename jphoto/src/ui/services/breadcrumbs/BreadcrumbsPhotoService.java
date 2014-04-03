package ui.services.breadcrumbs;

import ui.controllers.photos.edit.PhotoEditWizardStep;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import elements.PageTitleData;

import java.util.Date;

public interface BreadcrumbsPhotoService {

	PageTitleData getPhotoGalleryBreadcrumbs();

	PageTitleData getPhotosAllDataBest();

	PageTitleData getPhotoNewData( User user, PhotoEditWizardStep wizardStep );

	PageTitleData getPhotoCardData( final Photo photo, final User user, Genre genre, final String title );

	PageTitleData getPhotoEditData( Photo photo, User user, Genre genre );

	PageTitleData getPhotoNotFoundData();

	PageTitleData getUserPhotoVotingData( User user, Photo photo, Genre genre );

	PageTitleData getUserPhotoPreviewsData( User user, Photo photo, Genre genre );

	PageTitleData getPhotoCardForHiddenAuthor( final Photo photo, final Genre genre, final String title );

	PageTitleData getPhotoTitleForHiddenAuthor( Photo photo, Genre genre, String mode_t );

	PageTitleData getPhotosByGenreData( Genre genre );

	PageTitleData getPhotosByGenreDataBest( Genre genre );

	PageTitleData getPhotosByUser( User user );

	PageTitleData getPhotosByUserBest( User user );

	PageTitleData getPhotosByUserAndGenre( User user, Genre genre );

	PageTitleData getPhotosByUserAndGenreBest( User user, Genre genre );

	PageTitleData getPhotosVotedByUser( User user );

	PageTitleData getPhotosByUserByVotingCategory ( User user, PhotoVotingCategory votingCategory );

	PageTitleData getPhotosByPeriodData( Date dateFrom, Date dateTo );

	PageTitleData getPhotosByPeriodDataBest( Date dateFrom, Date dateTo );

	PageTitleData getPhotosByMembershipType( UserMembershipType membershipType );

	PageTitleData getPhotosByMembershipTypeBest( UserMembershipType membershipType );

	String getPhotoRootTranslated();

	PageTitleData getPhotoGroupOperationTitleData( PhotoGroupOperationType groupOperationType );

	PageTitleData getPhotoGroupOperationErrorTitleData();

	PageTitleData getFilteredPhotoListTitleData();
}
