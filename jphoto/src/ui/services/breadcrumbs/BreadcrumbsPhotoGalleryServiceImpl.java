package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.PhotoGalleryBreadcrumb;
import ui.services.breadcrumbs.items.PortalPageBreadcrumb;
import ui.services.breadcrumbs.items.UserListBreadcrumbs;
import utils.StringUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

public class BreadcrumbsPhotoGalleryServiceImpl implements BreadcrumbsPhotoGalleryService {

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;
	
	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private Services services;

	@Override
	public BreadcrumbsBuilder getUserPhotosInGenreLinkBreadcrumbs( final User user, final Genre genre ) {

		return portalPage( services )
			.userListLink()
			.userCardLink( user )
			.photosByUser( user )
			.photosByUserAndGenre( user, genre )
			;
	}

	@Override
	public PageTitleData getPhotoGalleryBreadcrumbs() {

		final String title = BreadcrumbsBuilder.pageTitle( new PhotoGalleryBreadcrumb( services ), services ).build();
		final String header = BreadcrumbsBuilder.pageHeader( new PortalPageBreadcrumb( services ), services ).build();

		final String breadcrumbs = portalPage( services )
			.photoGallery()
			.build();

		return new PageTitleData( title, header, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosAllDataBest() {
		final PageTitleData titleData = getPhotoGalleryBreadcrumbs();

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoVotingData( final User user, final Photo photo, final Genre genre ) {
		return photoActionsDetails( user, photo, genre, translatorService.translate( "Votes", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData getPhotoTitleForHiddenAuthor( final Photo photo, final Genre genre, final String mode_t ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String anonymousName = configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME );
		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, anonymousName, photo.getName() );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ), anonymousName, translatorService.translate( mode_t, EnvironmentContext.getLanguage() ) );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	private PageTitleData photoActionsDetails( final User user, final Photo photo, final Genre genre, final String tran ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), photo.getName(), tran );
		final List<String> baseBreadcrumbs = getPhotoBaseBreadcrumbs( photo, user, genre, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( baseBreadcrumbs );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByGenreData( final Genre genre ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String genreName = translatorService.translateGenre( genre, getLanguage() );
		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, genreName );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), genreName );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByGenreDataBest( final Genre genre ) {
		final PageTitleData titleData = getPhotosByGenreData( genre );

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUser( final User user ) {
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
	public PageTitleData getPhotosByUserBest( final User user ) {
		final PageTitleData titleData = getPhotosByUser( user );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getUserCardLink( user, language ), entityLinkUtilsService.getPhotosByUserLink( user, language ), translatorService.translate( "The Best", language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserAndGenre( final User user, final Genre genre ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String genreName = translatorService.translateGenre( genre, getLanguage() );
		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), rootTranslated, genreName );
		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ), entityLinkUtilsService.getUserCardLink( user, language ), entityLinkUtilsService.getPhotosByUserLink( user, language ), genreName );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserAndGenreBest( final User user, final Genre genre ) {
		final PageTitleData titleData = getPhotosByUserAndGenre( user, genre );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ), entityLinkUtilsService.getUserCardLink( user, language ), entityLinkUtilsService.getPhotosByUserLink( user, language ), entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre, getLanguage() ), translatorService.translate( "The Best", language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosVotedByUser( final User user ) {
		final PageTitleData titleData = getPhotosByUser( user );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getUserCardLink( user, language ), translatorService.translate( "Appraised photos", language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByUserByVotingCategory( final User user, final PhotoVotingCategory votingCategory ) {
		final PageTitleData titleData = getPhotosByUser( user );

		final Language language = EnvironmentContext.getLanguage();
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language )
			, entityLinkUtilsService.getUserCardLink( user, language )
			, entityLinkUtilsService.getPhotosVotedByUserLinkUser( user.getId(), language )
			, translatorService.translatePhotoVotingCategory( votingCategory, language ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByPeriodData( final Date dateFrom, final Date dateTo ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		final String dateRange = dateFrom.getTime() != dateTo.getTime() ? String.format( "%s - %s", fDateFrom, fDateTo ) : fDateTo;

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, dateRange );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), dateRange );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByPeriodDataBest( final Date dateFrom, final Date dateTo ) {
		final PageTitleData titleData = getPhotosByPeriodData( dateFrom, dateTo  );

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), entityLinkUtilsService.getPhotosByPeriod( dateFrom, dateTo ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByMembershipType( final UserMembershipType membershipType ) {
		final String rootTranslated = getPhotoRootTranslated();
		String membershipTypeName = StringUtilities.toUpperCaseFirst( translatorService.translate( membershipType.getNamePlural(), EnvironmentContext.getLanguage() ) );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, membershipTypeName );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), membershipTypeName );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosByMembershipTypeBest( final UserMembershipType membershipType ) {
		final PageTitleData titleData = getPhotosByMembershipType( membershipType );

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() )
			, entityLinkUtilsService.getPhotosByMembershipLink( membershipType, EnvironmentContext.getLanguage() ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	@Deprecated
	public String getPhotoRootTranslated() {
		return translatorService.translate( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, EnvironmentContext.getLanguage() );
	}

	private List<String> getPhotoBaseBreadcrumbs( final Photo photo, final User user, final Genre genre, final String... strings ) {
		final Language language = EnvironmentContext.getLanguage();
		final ArrayList<String> list = newArrayList(
			entityLinkUtilsService.getPhotosRootLink( language )
			, entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() )
			, entityLinkUtilsService.getUserCardLink( user, language )
			, entityLinkUtilsService.getPhotosByUserLink( user, language )
			, entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre, getLanguage() )
		);

		if ( strings.length > 0 && StringUtils.isNotEmpty( strings[0] ) ) {
			list.add( entityLinkUtilsService.getPhotoCardLink( photo, language ) );
			list.addAll( Arrays.asList( strings ) );
		} else {
			list.add( StringUtilities.escapeHtml( photo.getName() ) );
		}

		return list;
	}

	@Override
	public PageTitleData getPhotoGroupOperationTitleData( final PhotoGroupOperationType groupOperationType ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String groupOperationText = translatorService.translate( "Group operations", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, groupOperationText, groupOperationType.getName() );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), groupOperationText, groupOperationType.getName() );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoGroupOperationErrorTitleData() {
		final String rootTranslated = getPhotoRootTranslated();
		final String groupOperationText = translatorService.translate( "Group operations", EnvironmentContext.getLanguage() );

		final String error = translatorService.translate( "Group operation error", EnvironmentContext.getLanguage() );
		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, groupOperationText, error );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), groupOperationText, error );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getFilteredPhotoListTitleData() {
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
