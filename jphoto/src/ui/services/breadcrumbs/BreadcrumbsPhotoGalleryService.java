package ui.services.breadcrumbs;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import elements.PageTitleData;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;

import java.util.Date;

public interface BreadcrumbsPhotoGalleryService {

	PageTitleData getPhotoGalleryBreadcrumbs();

	PageTitleData getPhotosAllDataBest();

	PageTitleData getUserPhotoVotingData( User user, Photo photo, Genre genre );

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
