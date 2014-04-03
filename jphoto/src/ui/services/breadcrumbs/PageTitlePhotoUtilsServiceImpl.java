package ui.services.breadcrumbs;

import ui.controllers.photos.edit.PhotoEditWizardStep;
import core.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import utils.StringUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PageTitlePhotoUtilsServiceImpl implements PageTitlePhotoUtilsService {

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

	@Override
	public PageTitleData getPhotosAllData() {
		final String rootTranslated = getPhotoRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( rootTranslated );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotosAllDataBest() {
		final PageTitleData titleData = getPhotosAllData();

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), translatorService.translate( "The Best", EnvironmentContext.getLanguage() ) );

		return new PageTitleData( titleData.getTitle(), titleData.getHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoNewData( final User user, final PhotoEditWizardStep wizardStep ) {
		final String rootTranslated = getPhotoRootTranslated();
		final Language language = EnvironmentContext.getLanguage();
		final String tran = translatorService.translate( "Breadcrumbs: Upload new photo", language );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( language ), entityLinkUtilsService.getUserCardLink( user, language ), entityLinkUtilsService.getPhotosByUserLink( user, language ), tran, wizardStep.getStepDescription() );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoCardData( final Photo photo, final User user, final Genre genre, final String title ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String fullTitle = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), photo.getName(), title );

		final List<String> baseBreadcrumbs = getPhotoBaseBreadcrumbs( photo, user, genre, title );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( baseBreadcrumbs );

		return new PageTitleData( fullTitle, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoEditData( final Photo photo, final User user, final Genre genre ) {
		final String rootTranslated = getPhotoRootTranslated();
		final String tran = translatorService.translate( "Breadcrumbs: Edit photo", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, user.getName(), photo.getName(), tran );

		final List<String> baseBreadcrumbs = getPhotoBaseBreadcrumbs( photo, user, genre, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( baseBreadcrumbs );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getPhotoNotFoundData() {
		final String rootTranslated = getPhotoRootTranslated();
		final String tran = translatorService.translate( "Photo not found", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getUserPhotoVotingData( final User user, final Photo photo, final Genre genre ) {
		return photoActionsDetails( user, photo, genre, translatorService.translate( "Votes", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData getUserPhotoPreviewsData( final User user, final Photo photo, final Genre genre ) {
		return photoActionsDetails( user, photo, genre, translatorService.translate( "Previews", EnvironmentContext.getLanguage() ) );
	}

	@Override
	public PageTitleData getPhotoCardForHiddenAuthor( final Photo photo, final Genre genre, final String title ) {
		final String rootTranslated = getPhotoRootTranslated();

		final String userAnonymousName = configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME );
		final String fullTitle = pageTitleUtilsService.getTitleDataString( rootTranslated, userAnonymousName, photo.getName(), title );

		final List<String> breadcrumbList = newArrayList();
		breadcrumbList.add( entityLinkUtilsService.getPhotosRootLink( EnvironmentContext.getLanguage() ) );
		breadcrumbList.add( entityLinkUtilsService.getPhotosByGenreLink( genre, getLanguage() ) );
		breadcrumbList.add( userAnonymousName );
		breadcrumbList.add( StringUtils.isNotEmpty( title ) ? entityLinkUtilsService.getPhotoCardLink( photo, EnvironmentContext.getLanguage() ) : photo.getNameEscaped() );
		if ( StringUtils.isNotEmpty( title ) ) {
			breadcrumbList.add( title );
		}

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( breadcrumbList );

		return new PageTitleData( fullTitle, rootTranslated, breadcrumbs );
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
	public String getPhotoRootTranslated() {
		return translatorService.translate( "Photo gallery", EnvironmentContext.getLanguage() );
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
