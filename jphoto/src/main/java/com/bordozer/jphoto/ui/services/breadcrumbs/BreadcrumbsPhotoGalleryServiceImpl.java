package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.general.data.TimeRange;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.StringBreadcrumb;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;
import com.bordozer.jphoto.ui.services.menu.main.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.bordozer.jphoto.ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

@Service("breadcrumbsPhotoGalleryService")
public class BreadcrumbsPhotoGalleryServiceImpl implements BreadcrumbsPhotoGalleryService {

    public static final String PHOTO_GALLERY_THE_BEST = "Breadcrumbs: The best";
    public static final String BREADCRUMBS_GROUP_OPERATIONS = "Breadcrumbs: Group operations";

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private PhotoVotingService photoVotingService;

    @Autowired
    private Services services;

    @Override
    public PageTitleData getPhotoGalleryBreadcrumbs() {

        final String breadcrumbs = portalPage(services)
                .add(new TranslatableStringBreadcrumb(BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services))
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getLastPopularPhotosBreadcrumbs() {
        final String breadcrumbs = photoGalleryLink()
                .translatableString(MenuService.MAIN_MENU_LATEST_POPULAR_PHOTOS)
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getAbsolutelyBestPhotosBreadcrumbs() {

        final String breadcrumbs = photoGalleryLink()
                .translatableString(MenuService.MAIN_MENU_ABSOLUTE_BEST)
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByGenreBreadcrumbs(final Genre genre) {
        final String breadcrumbs = photoGalleryLink()
                .string(getGenreNameTranslated(genre))
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByGenreBestBreadcrumbs(final Genre genre) {

        final String breadcrumbs = photoGalleryLink()
                .photosByGenre(genre)
                .string(getForPeriodBreadcrumb("Breadcrumbs: The best for period $1 - $2")) // TODO: show info that there are the best for period photos
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByUserBreadcrumbs(final User user) {

        final String breadcrumbs = userCardLink(user)
                .translatableString(EntityLinkUtilsService.ALL_USER_S_PHOTOS)
                .build();

        return new PageTitleData(pageTitle(user), pageHeader(user), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByUserBestBreadcrumbs(final User user) {
        final String breadcrumbs = userCardLink(user)
                .photosByUser(user)
                .translatableString(PHOTO_GALLERY_THE_BEST)
                .build();

        return new PageTitleData(pageTitle(user), pageHeader(user), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByUserAndGenreBreadcrumbs(final User user, final Genre genre) {

        final String breadcrumbs = photoGalleryLink()
                .photosByGenre(genre)
                .userCardLink(user)
                .photosByUser(user)
                .string(getGenreNameTranslated(genre))
                .build();

        return new PageTitleData(pageTitle(user), pageHeader(user), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByUserAndGenreBestBreadcrumbs(final User user, final Genre genre) {
        final String breadcrumbs = photoGalleryLink()
                .photosByGenre(genre)
                .userCardLink(user)
                .photosByUser(user)
                .photosByUserAndGenre(user, genre)
                .translatableString(PHOTO_GALLERY_THE_BEST)
                .build();

        return new PageTitleData(pageTitle(user), pageHeader(user), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosAppraisedByUserBreadcrumbs(final User user) {

        final String breadcrumbs = userCardLink(user)
                .translatableString(EntityLinkUtilsService.BREADCRUMBS_APPRAISED_PHOTOS)
                .build();

        return new PageTitleData(pageTitle(user), pageHeader(user), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByUserByVotingCategoryBreadcrumbs(final User user, final PhotoVotingCategory votingCategory) {

        final String breadcrumbs = userCardLink(user)
                .photoAppraisedPhotosLink(user)
                .string(translatorService.translatePhotoVotingCategory(votingCategory, getLanguage()))
                .build();

        return new PageTitleData(pageTitle(user), pageHeader(user), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByPeriodBreadcrumbs(final Date dateFrom, final Date dateTo) {

        final String breadcrumbs = photoGalleryLink()
                .string(String.format("%s - %s", dateUtilsService.formatDate(dateFrom), dateUtilsService.formatDate(dateTo))) // TODO: show info that there are uploaded in period photos
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByPeriodBestBreadcrumbs(final Date dateFrom, final Date dateTo) {

        final String breadcrumbs = photoGalleryLink()
                .string(entityLinkUtilsService.getPhotosByPeriod(dateFrom, dateTo))
                .string(getForPeriodBreadcrumb(PHOTO_GALLERY_THE_BEST))
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByMembershipTypeBreadcrumbs(final UserMembershipType membershipType) {
        final String breadcrumbs = photoGalleryLink()
                .string(entityLinkUtilsService.getMembershipPhotosLinkText(membershipType, getLanguage()))
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotosByMembershipTypeBestBreadcrumbs(final UserMembershipType membershipType) {
        final String breadcrumbs = photoGalleryLink()
                .string(entityLinkUtilsService.getPhotosByMembershipLink(membershipType, getLanguage()))
                .translatableString(PHOTO_GALLERY_THE_BEST)
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getPhotoGroupOperationBreadcrumbs(final PhotoGroupOperationType groupOperationType) {

        final String breadcrumbs = photoGalleryLink()
                .translatableString(groupOperationType.getName())
                .build();

        final TranslatableStringBreadcrumb breadcrumb = new TranslatableStringBreadcrumb(BREADCRUMBS_GROUP_OPERATIONS, services);
        final String title = BreadcrumbsBuilder.pageTitle(breadcrumb, services).build();
        final String header = BreadcrumbsBuilder.pageHeader(breadcrumb, services).build();

        return new PageTitleData(title, header, breadcrumbs);
    }

    @Override
    public PageTitleData getPhotoGroupOperationErrorBreadcrumbs() {

        final String breadcrumbs = photoGalleryLink()
                .translatableString(BREADCRUMBS_GROUP_OPERATIONS)
                .translatableString("Error")
                .build();

        final TranslatableStringBreadcrumb breadcrumb = new TranslatableStringBreadcrumb(BREADCRUMBS_GROUP_OPERATIONS, services);
        final String title = BreadcrumbsBuilder.pageTitle(breadcrumb, services).build();
        final String header = BreadcrumbsBuilder.pageHeader(breadcrumb, services).build();

        return new PageTitleData(title, header, breadcrumbs);
    }

    @Override
    public PageTitleData getFilteredPhotoListBreadcrumbs() {

        final TranslatableStringBreadcrumb breadcrumb = new TranslatableStringBreadcrumb("Photo search: Photo search result", services);

        final String title = BreadcrumbsBuilder.pageTitle(breadcrumb, services).build();
        final String header = BreadcrumbsBuilder.pageHeader(breadcrumb, services).build();

        final String breadcrumbs = photoGalleryLink()
                .add(breadcrumb)
                .build();

        return new PageTitleData(title, header, breadcrumbs);
    }

    private String pageTitle() {
        return BreadcrumbsBuilder.pageTitle(new TranslatableStringBreadcrumb(BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services), services).build();
    }

    private String pageHeader() {
        return BreadcrumbsBuilder.pageHeader(new TranslatableStringBreadcrumb(BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services), services).build();
    }

    private String pageTitle(final User user) {
        return BreadcrumbsBuilder.pageTitle(new StringBreadcrumb(user.getNameEscaped(), services), services).build();
    }

    private String pageHeader(final User user) {
        return BreadcrumbsBuilder.pageHeader(new StringBreadcrumb(user.getNameEscaped(), services), services).build();
    }

    private Language getLanguage() {
        return EnvironmentContext.getCurrentUser().getLanguage();
    }

    private BreadcrumbsBuilder photoGalleryLink() {
        return portalPage(services).photoGalleryLink();
    }

    private String getForPeriodBreadcrumb(final String nerd) {
        final TimeRange dateRange = photoVotingService.getTopBestDateRange();
        return translatorService.translate(nerd, getLanguage(), dateUtilsService.formatDate(dateRange.getTimeFrom()), dateUtilsService.formatDate(dateRange.getTimeTo()));
    }

    private BreadcrumbsBuilder userCardLink(final User user) {
        return photoGalleryLink()
                .userCardLink(user);
    }

    private String getGenreNameTranslated(final Genre genre) {
        return translatorService.translateGenre(genre, getLanguage());
    }
}
