package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;
import utils.StringUtilities;

import java.util.Date;

import static ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

public class BreadcrumbsPhotoGalleryServiceImpl implements BreadcrumbsPhotoGalleryService {

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;
	
	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private Services services;

	@Override
	public PageTitleData getPhotoGalleryBreadcrumbs() {

		final TranslatableStringBreadcrumb breadcrumb = new TranslatableStringBreadcrumb( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, services );

		final String title = BreadcrumbsBuilder.pageTitle( breadcrumb, services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( breadcrumb, services ).build();

		final String breadcrumbs = portalPage( services )
			.add( breadcrumb )
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getAbsolutelyBestPhotosBreadcrumbs() {
		final PageTitleData titleData = getPhotoGalleryBreadcrumbs();

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(
			entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() )
			, translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByGenreBreadcrumbs( final Genre genre ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String genreName = translatorService.translateGenre( genre, getLanguage() );
		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, genreName );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), genreName );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByGenreBestBreadcrumbs( final Genre genre ) {
		final PageTitleData titleData = getPhotosByGenreBreadcrumbs( genre );

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserBreadcrumbs( final User user ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), rootTranslated );
		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString(
			entityLinkUtilsService.getPhotosRootLink( language )
			, entityLinkUtilsService.getUserCardLink( user, language )
			, translatorService.translate( "Breadcrumbs: User's photos", language )
		);

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserBestBreadcrumbs( final User user ) {
		final PageTitleData titleData = getPhotosByUserBreadcrumbs( user );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getUserCardLink( user, language ), entityLinkUtilsService.getPhotosByUserLink( user, language ), translatorService.translate( "The Best", language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserAndGenreBreadcrumbs( final User user, final Genre genre ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String genreName = translatorService.translateGenre( genre, getLanguage() );
		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), rootTranslated, genreName );
		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ), entityLinkUtilsService.getUserCardLink( user, language ), entityLinkUtilsService.getPhotosByUserLink( user, language ), genreName );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserAndGenreBestBreadcrumbs( final User user, final Genre genre ) {
		final PageTitleData titleData = getPhotosByUserAndGenreBreadcrumbs( user, genre );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ), entityLinkUtilsService.getUserCardLink( user, language ), entityLinkUtilsService.getPhotosByUserLink( user, language ), entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre, getLanguage() ), translatorService.translate( "The Best", language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosAppraisedByUserBreadcrumbs( final User user ) {
		final PageTitleData titleData = getPhotosByUserBreadcrumbs( user );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getUserCardLink( user, language ), translatorService.translate( "Appraised photos", language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserByVotingCategoryBreadcrumbs( final User user, final PhotoVotingCategory votingCategory ) {
		final PageTitleData titleData = getPhotosByUserBreadcrumbs( user );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language )
			, entityLinkUtilsService.getUserCardLink( user, language )
			, entityLinkUtilsService.getPhotosVotedByUserLinkUser( user.getId(), language )
			, translatorService.translatePhotoVotingCategory( votingCategory, language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
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

	private Language getLanguage() {
		return EnvironmentContext.getCurrentUser().getLanguage();
	}
}
