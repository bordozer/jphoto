package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.system.ConfigurationService;
import core.services.system.MenuService;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.StringBreadcrumb;
import ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;
import utils.StringUtilities;

import java.util.Date;

import static ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

public class BreadcrumbsPhotoGalleryServiceImpl implements BreadcrumbsPhotoGalleryService {

	public static final String PHOTO_GALLERY_THE_BEST = "Breadcrumbs: The best";
	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;
	
	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private Services services;

	@Override
	public PageTitleData getPhotoGalleryBreadcrumbs() {

		final String breadcrumbs = portalPage( services )
			.add( new TranslatableStringBreadcrumb( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services ) )
			.build();

		return new PageTitleData( pageTitle(), pageHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getAbsolutelyBestPhotosBreadcrumbs() {

		final String breadcrumbs = photoGalleryLink()
			.translatableString( MenuService.MAIN_MENU_ABSOLUTE_BEST )
			.build();

		return new PageTitleData( pageTitle(), pageHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByGenreBreadcrumbs( final Genre genre ) {
		final String breadcrumbs = photoGalleryLink()
			.string( getGenreNameTranslated( genre ) )
			.build();

		return new PageTitleData( pageTitle(), pageHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByGenreBestBreadcrumbs( final Genre genre ) {

		final String breadcrumbs = photoGalleryLink()
			.photosByGenre( genre )
			.string( getTheBestForPeriodBreadcrumb() )
			.build();

		return new PageTitleData( pageTitle(), pageHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserBreadcrumbs( final User user ) {

		final String breadcrumbs = userCardLink( user )
			.translatableString( EntityLinkUtilsService.ALL_USER_S_PHOTOS )
			.build();

		return new PageTitleData( pageTitle( user ), pageHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserBestBreadcrumbs( final User user ) {
		final String breadcrumbs = userCardLink( user )
			.photosByUser( user )
			.translatableString( PHOTO_GALLERY_THE_BEST )
			.build();

		return new PageTitleData( pageTitle( user ), pageHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserAndGenreBreadcrumbs( final User user, final Genre genre ) {

		final String breadcrumbs = photoGalleryLink()
			.photosByGenre( genre )
			.userCardLink( user )
			.photosByUser( user )
			.string( getGenreNameTranslated( genre ) )
			.build();

		return new PageTitleData( pageTitle( user ), pageHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserAndGenreBestBreadcrumbs( final User user, final Genre genre ) {
		final String breadcrumbs = photoGalleryLink()
			.photosByGenre( genre )
			.userCardLink( user )
			.photosByUser( user )
			.photosByUserAndGenre( user, genre )
			.translatableString( PHOTO_GALLERY_THE_BEST )
			.build();

		return new PageTitleData( pageTitle( user ), pageHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosAppraisedByUserBreadcrumbs( final User user ) {

		final String breadcrumbs = userCardLink( user )
			.translatableString( EntityLinkUtilsService.BREADCRUMBS_APPRAISED_PHOTOS )
			.build();

		return new PageTitleData( pageTitle( user ), pageHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserByVotingCategoryBreadcrumbs( final User user, final PhotoVotingCategory votingCategory ) {

		final String breadcrumbs = userCardLink( user )
			.photoAppraisalPhotosLink( user )
			.string( translatorService.translatePhotoVotingCategory( votingCategory, getLanguage() ) )
			.build();

		return new PageTitleData( pageTitle( user ), pageHeader( user ), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByPeriodBreadcrumbs( final Date dateFrom, final Date dateTo ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		final String dateRange = dateFrom.getTime() != dateTo.getTime() ? String.format( "%s - %s", fDateFrom, fDateTo ) : fDateTo;

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, dateRange );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), dateRange );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByPeriodBestBreadcrumbs( final Date dateFrom, final Date dateTo ) {
		final PageTitleData titleData = getPhotosByPeriodBreadcrumbs( dateFrom, dateTo );

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getPhotosByPeriod( dateFrom, dateTo ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByMembershipTypeBreadcrumbs( final UserMembershipType membershipType ) {
		final String rootTranslated = getPhotoRootTranslated();
		String membershipTypeName = StringUtilities.toUpperCaseFirst( translatorService.translate( membershipType.getNamePlural(), EnvironmentContext.getLanguage() ) );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, membershipTypeName );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), membershipTypeName );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByMembershipTypeBestBreadcrumbs( final UserMembershipType membershipType ) {
		final PageTitleData titleData = getPhotosByMembershipTypeBreadcrumbs( membershipType );

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() )
			, entityLinkUtilsService.getPhotosByMembershipLink( membershipType, EnvironmentContext.getLanguage() ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	private String getPhotoRootTranslated() {
		return translatorService.translate( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, EnvironmentContext.getLanguage() );
	}

	@Override
	public PageTitleData getPhotoGroupOperationBreadcrumbs( final PhotoGroupOperationType groupOperationType ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String groupOperationText = translatorService.translate( "Group operations", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, groupOperationText, groupOperationType.getName() );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), groupOperationText, groupOperationType.getName() );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoGroupOperationErrorBreadcrumbs() {
		final String rootTranslated = getPhotoRootTranslated();
		final String groupOperationText = translatorService.translate( "Group operations", EnvironmentContext.getLanguage() );

		final String error = translatorService.translate( "Group operation error", EnvironmentContext.getLanguage() );
		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, groupOperationText, error );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), groupOperationText, error );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getFilteredPhotoListBreadcrumbs() {
		final String rootTranslated = getPhotoRootTranslated();
		final String text = translatorService.translate( "Photo search", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, text );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), text );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	private String pageTitle() {
		return BreadcrumbsBuilder.pageTitle( new TranslatableStringBreadcrumb( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services ), services ).build();
	}

	private String pageHeader() {
		return BreadcrumbsBuilder.pageHeader( new TranslatableStringBreadcrumb( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services ), services ).build();
	}

	private String pageTitle( final User user ) {
		return BreadcrumbsBuilder.pageTitle( new StringBreadcrumb( user.getNameEscaped(), services ), services ).build();
	}

	private String pageHeader( final User user ) {
		return BreadcrumbsBuilder.pageHeader( new StringBreadcrumb( user.getNameEscaped(), services ), services ).build();
	}

	private Language getLanguage() {
		return EnvironmentContext.getCurrentUser().getLanguage();
	}

	private BreadcrumbsBuilder photoGalleryLink() {
		return portalPage( services ).photoGalleryLink();
	}

	private String getTheBestForPeriodBreadcrumb() {
		final int days = configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
		final Date dateFrom = dateUtilsService.getDatesOffsetFromCurrentDate( -days );
		final Date dateTo = dateUtilsService.getCurrentDate();

		return translatorService.translate( "Breadcrumbs: The best for period $1 - $2", getLanguage(), dateUtilsService.formatDate( dateFrom ), dateUtilsService.formatDate( dateTo ) );
	}

	private BreadcrumbsBuilder userCardLink( final User user ) {
		return photoGalleryLink()
			.userCardLink( user );
	}

	private String getGenreNameTranslated( final Genre genre ) {
		return translatorService.translateGenre( genre, getLanguage() );
	}
}
