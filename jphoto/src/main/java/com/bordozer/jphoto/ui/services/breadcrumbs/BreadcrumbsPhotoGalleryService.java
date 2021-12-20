package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.ui.elements.PageTitleData;

import java.util.Date;

public interface BreadcrumbsPhotoGalleryService {

    PageTitleData getPhotoGalleryBreadcrumbs();

    PageTitleData getLastPopularPhotosBreadcrumbs();

    PageTitleData getAbsolutelyBestPhotosBreadcrumbs();

    PageTitleData getPhotosByGenreBreadcrumbs(final Genre genre);

    PageTitleData getPhotosByGenreBestBreadcrumbs(final Genre genre);

    PageTitleData getPhotosByUserBreadcrumbs(final User user);

    PageTitleData getPhotosByUserBestBreadcrumbs(final User user);

    PageTitleData getPhotosByUserAndGenreBreadcrumbs(final User user, final Genre genre);

    PageTitleData getPhotosByUserAndGenreBestBreadcrumbs(final User user, final Genre genre);

    PageTitleData getPhotosAppraisedByUserBreadcrumbs(final User user);

    PageTitleData getPhotosByUserByVotingCategoryBreadcrumbs(final User user, final PhotoVotingCategory votingCategory);

    PageTitleData getPhotosByPeriodBreadcrumbs(final Date dateFrom, final Date dateTo);

    PageTitleData getPhotosByPeriodBestBreadcrumbs(final Date dateFrom, final Date dateTo);

    PageTitleData getPhotosByMembershipTypeBreadcrumbs(final UserMembershipType membershipType);

    PageTitleData getPhotosByMembershipTypeBestBreadcrumbs(final UserMembershipType membershipType);

    PageTitleData getPhotoGroupOperationBreadcrumbs(final PhotoGroupOperationType groupOperationType);

    PageTitleData getPhotoGroupOperationErrorBreadcrumbs();

    PageTitleData getFilteredPhotoListBreadcrumbs();
}
