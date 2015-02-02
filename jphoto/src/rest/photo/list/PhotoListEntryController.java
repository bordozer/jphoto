package rest.photo.list;

import core.enums.FavoriteEntryType;
import core.general.configuration.ConfigurationKey;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.log.LogHelper;
import core.services.entry.FavoritesService;
import core.services.entry.GenreService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoPreviewService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.security.RestrictionService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;
import ui.controllers.photos.groupoperations.PhotoGroupOperationModel;
import ui.services.security.SecurityUIService;
import ui.userRankIcons.AbstractUserRankIcon;
import ui.userRankIcons.UserRankIconContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoListEntryController extends AbstractPhotoListEntryController {

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private PhotoPreviewService photoPreviewService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	private final LogHelper log = new LogHelper( PhotoListEntryController.class );

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoEntryDTO photoListEntry( final @PathVariable( "photoId" ) int photoId, final HttpServletRequest request ) {

		final Photo photo = photoService.load( photoId );
		final User currentUser = EnvironmentContext.getCurrentUser();

		final boolean doesPreviewHasToBeHidden = request.getHeader( "referer" ).startsWith( urlUtilsService.getAllUsersLink() ) && securityService.isPhotoAuthorNameMustBeHidden( photo, currentUser );

		return photoListEntry( photo, currentUser, doesPreviewHasToBeHidden, getLanguage() );
	}

	public PhotoEntryDTO photoListEntry( final Photo photo, final User accessor, final boolean doesPreviewHasToBeHidden, final Language language ) {

		final boolean isSuperAdminUser = securityService.isSuperAdminUser( accessor );
		final boolean showAdminFlag_nude = isSuperAdminUser && photo.isContainsNudeContent();

		final Genre genre = genreService.load( photo.getGenreId() );
		final String genreName = translatorService.translateGenre( genre, language );
		final boolean canContainNudeContent = genre.isCanContainNudeContent();

		final PhotoEntryDTO dto = new PhotoEntryDTO( accessor.getId(), photo.getId() );

		dto.setUserSuperAdmin( isSuperAdminUser );

		dto.setGroupOperationCheckbox( getGroupOperationCheckbox( photo ) );
		dto.setPhotoUploadDate( getPhotoUploadDate( photo, language ) );
		dto.setPhotoCategory( getPhotoCategory( photo.getGenreId(), language ) );

		dto.setPhotoCategoryCanContainNudeContent( canContainNudeContent );
		dto.setPhotoCategoryContainsNudeContent( genre.isContainsNudeContent() );

		dto.setPhotoImage( getPhotoPreview( photo, accessor, doesPreviewHasToBeHidden, language, userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) ) );
		dto.setPhotoCardLink( urlUtilsService.getPhotoCardLink( photo.getId() ) );
		dto.setShowPhotoListPreviewFooter( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_PREVIEW_FOOTER ) );

		dto.setIconTitlePhotoIsInAlbum( translatorService.translate( "Photo preview: The photo is in album admin icon", language ) );
		dto.setIconTitleNudeContent( showAdminFlag_nude && !canContainNudeContent ? translatorService.translate( "Photo preview: Category $1 can not contain NUDE but it does!!!", language, genreName ) : translatorService.translate( "Photo preview: The photo has nude content admin icon", language ) );

		dto.setTextConfirmSettingNudeContent( translatorService.translate( "Photo preview: Set nude content property for photo?", language ) );
		dto.setTextConfirmRemovingNudeContent( translatorService.translate( "Photo preview: Remove nude content property for photo?", language ) );

		dto.setTextCategoryCanNotContainNudeContent( translatorService.translate( "Photo preview: Category $1 can not contains nude content", language, genreName ) );
		dto.setTextCategoryContainsNudeContent( translatorService.translate( "Photo preview: Category $1 contains nude content", language, genreName ) );

		setNudeContent( photo, accessor, dto, language );

		dto.setShowPhotoContextMenu( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_PHOTO_MENU ) );

		if ( doesPreviewHasToBeHidden ) {
			dto.setPhotoName( translatorService.translate( "Photo preview: Photo's name is hidden", language ) );
			dto.setPhotoLink( "" );
		} else {
			dto.setPhotoName( photo.getName() ); // TODO: escaping!
			dto.setPhotoLink( entityLinkUtilsService.getPhotoCardLink( photo, language ) );
		}
		dto.setPhotoAuthorLink( getPhotoAuthorLink( photo, accessor, language ) );

		setPhotoStatistics( photo, dto, doesPreviewHasToBeHidden, language );

		setUserRank( photo, accessor, dto );

		setPhotoAnonymousPeriodExpiration( photo, accessor, dto, language );

		final boolean userOwnThePhoto = securityService.userOwnThePhoto( accessor, photo );

		dto.setShowAdminFlag_Anonymous( securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) );

		dto.setShowAdminFlag_Nude( showAdminFlag_nude );

		if ( securityService.isSuperAdminUser( accessor ) || securityService.userOwnThePhoto( accessor, photo ) ) {
			setSpecialRestrictedIcons( photo, dto );
		}

		dto.setUserOwnThePhoto( userOwnThePhoto );

		final List<PhotoBookmarkIcon> photoBookmarkIcons = newArrayList();
		for ( final FavoriteEntryType favoriteEntryType : FavoriteEntryType.RELATED_TO_PHOTO ) {

			final int favoriteEntryTypeId = favoriteEntryType.getId();

			if ( favoritesService.isEntryInFavorites( accessor.getId(), photo.getId(), favoriteEntryTypeId ) ) {
				photoBookmarkIcons.add( new PhotoBookmarkIcon( favoriteEntryTypeId ) );
			}
		}
		dto.setPhotoBookmarkIcons( photoBookmarkIcons );

		final boolean hideAuthorName = securityService.isPhotoAuthorNameMustBeHidden( photo, accessor );
		if ( ! hideAuthorName ) {
			final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadPhotoAlbums( photo.getId() );
			final boolean isPhotoInAlbum = ! userPhotoAlbums.isEmpty();
			dto.setMemberOfAlbum( isPhotoInAlbum );
			if ( isPhotoInAlbum ) {
				dto.setPhotoAlbumLink( urlUtilsService.getUserPhotoAlbumPhotosLink( photo.getUserId(), userPhotoAlbums.get( 0 ).getId() ) );
			}
		}

		log.debug( String.format( "Rendering photo list entry #%d: %s", photo.getId(), photo.getName() ) );

		return dto;
	}

	private void setNudeContent( final Photo photo, final User accessor, final PhotoEntryDTO dto, final Language language ) {
		final boolean hidePreviewBecauseOfNudeContent = securityUIService.isPhotoHasToBeHiddenBecauseOfNudeContent( photo, accessor );

		dto.setHidePreviewBecauseOfNudeContent( hidePreviewBecauseOfNudeContent );

		dto.setNudeContentWarning0( translatorService.translate( "Photo preview nude content: Photo contains", language ) );
		dto.setNudeContentWarning1( translatorService.translate( "Photo preview nude content: NUDE CONTENT", language ) );
		dto.setNudeContentWarning2( translatorService.translate( "Photo preview nude content: You must be at least", language ) );
		dto.setNudeContentWarning3( translatorService.translate( "Photo preview nude content: 18 years old", language ) );
		dto.setNudeContentWarning4( translatorService.translate( "Photo preview nude content: to see this", language ) );
	}

	private void setSpecialRestrictedIcons( final Photo photo, final PhotoEntryDTO dto ) {
		final Date currentTime = dateUtilsService.getCurrentTime();

		final Map<String, SpecialIconDTO> result = newHashMap();
		final List<EntryRestriction> restrictionsOn = restrictionService.getPhotoAllRestrictionsOn( photo.getId(), currentTime );
		for ( final EntryRestriction restriction : restrictionsOn ) {
			final SpecialIconDTO iconDTO = new SpecialIconDTO();
			iconDTO.setIcon( restriction.getRestrictionType().getIcon() );
			iconDTO.setRestrictionTypeName( translatorService.translate( "List preview special restriction icon: Restriction $1", getLanguage(), translatorService.translate( restriction.getRestrictionType().getName(), getLanguage() ) ) );
			iconDTO.setRestrictionMessage( restrictionService.getPhotoRestrictionMessage( restriction ).build( getLanguage() ) );

			result.put( String.valueOf( restriction.getRestrictionType().getId() ), iconDTO );
		}

		dto.setSpecialRestrictionIcons( result );
	}

	private void setPhotoStatistics( final Photo photo, final PhotoEntryDTO photoEntry, final boolean doesPreviewHasToBeHidden, final Language language ) {

		if ( photo.isArchived() ) {
			return;
		}

		final boolean showPhotoStatistics = configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_STATISTIC );
		photoEntry.setShowStatistics( showPhotoStatistics );

		if ( !showPhotoStatistics ) {
			return;
		}

		photoEntry.setTodayMarks( getTodayMarks( photo ) );
		photoEntry.setTodayMarksTitle( translatorService.translate( "Photo preview: The photo's today's marks", language ) );

		final TimeRange dateRange = photoVotingService.getTopBestDateRange();
		photoEntry.setPeriodMarks( photoVotingService.getPhotoMarksForPeriod( photo.getId(), dateRange.getTimeFrom(), dateRange.getTimeTo() ) );

		photoEntry.setPeriodMarksTitle( translatorService.translate( "Photo preview: The photo's marks for period from $1 to $2"
				, language
				, dateUtilsService.formatDate( dateRange.getTimeFrom() )
				, dateUtilsService.formatDate( dateRange.getTimeTo() )
		) );

		final String totalMarksTitle = translatorService.translate( "Photo preview: The photo's total marks", language );
		final int totalMarks = getTotalMarks( photo );
		if ( doesPreviewHasToBeHidden ) {
			photoEntry.setTotalMarks( String.format( "<span title='%s'>%d</span>", totalMarksTitle, totalMarks ) );
		} else {
			photoEntry.setTotalMarks( String.format( "<a href='%s' title='%s'>%d</a>", urlUtilsService.getPhotoMarksListLink( photo.getId() ), totalMarksTitle, totalMarks ) );
		}


		// Previews
		final String previewsCount = String.valueOf( photoPreviewService.getPreviewCount( photo.getId() ) );
		photoEntry.setPreviewsIcon( String.format( "<img src='%s/photo_preview_views_icon.png' height='8' title='%s'>"
						, urlUtilsService.getSiteImagesPath()
						, translatorService.translate( "Photo preview: Previews count: $1", language, previewsCount )
				)
		);
		if ( doesPreviewHasToBeHidden ) {
			photoEntry.setPreviewsCount( String.format( "<span title='%s'>%s</span>"
							, translatorService.translate( "Photo preview: Previews count: $1", language, previewsCount )
							, previewsCount
					)
			);
		} else {
			photoEntry.setPreviewsCount( String.format( "<a href='%s' title='%s'>%s</a>"
					, urlUtilsService.getPhotoPreviewsListLink( photo.getId() )
					, translatorService.translate( "Photo preview: Show preview history", language )
					, previewsCount
			) );
		}

		// Comments
		final String commentsCount = String.valueOf( photoCommentService.getPhotoCommentsCount( photo.getId() ) );
		photoEntry.setCommentsIcon( String.format( "<img src='%s/photo_preview_comments_icon.png' height='8' title='%s'>"
						, urlUtilsService.getSiteImagesPath()
						, translatorService.translate( "Photo preview: Comments count: $1", language, commentsCount )
				)
		);
		photoEntry.setCommentsCount( String.format( "<span title='%s'>%s</span>"
						, translatorService.translate( "Photo preview: Comments count: $1", language, commentsCount )
						, commentsCount
				)
		);

	}

	private int getTodayMarks( final Photo photo ) {
		return photoVotingService.getPhotoMarksForPeriod( photo.getId(), dateUtilsService.getFirstSecondOfToday(), dateUtilsService.getLastSecondOfToday() );
	}

	private int getTotalMarks( final Photo photo ) {
		return photoVotingService.getSummaryPhotoMark( photo );
	}

	private String getGroupOperationCheckbox( final Photo photo ) {
		final String id = PhotoGroupOperationModel.FORM_CONTROL_SELECTED_PHOTO_IDS;
		return String.format( "<input type='checkbox' id='%s' name='%s' class='%s' value='%s' />", id, id, id, photo.getId() );
	}

	private String getPhotoAuthorLink( final Photo photo, final User accessor, final Language language ) {
		final boolean hideAuthorName = securityService.isPhotoAuthorNameMustBeHidden( photo, accessor );

		if ( hideAuthorName ) {
			return userService.getAnonymousUserName( language );
		}

		final User photoAuthor = userService.load( photo.getUserId() );
		return entityLinkUtilsService.getUserCardLink( photoAuthor, language );
	}

	private void setUserRank( final Photo photo, final User accessor, final PhotoEntryDTO photoEntry ) {
		final boolean showUserRank = configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_USER_RANK_IN_GENRE );

		if ( !showUserRank ) {
			return;
		}

		if ( ! securityService.isPhotoAuthorNameMustBeHidden( photo, accessor ) ) {
			return;
		}

		photoEntry.setShowUserRank( true );

		final User photoAuthor = userService.load( photo.getUserId() );
		final UserRankIconContainer iconContainer = userRankService.getUserRankIconContainer( photoAuthor, photo );
		final StringBuilder builder = new StringBuilder();

		for ( final AbstractUserRankIcon rankIcon : iconContainer.getRankIcons() ) {
			builder.append( String.format( "<img src='%s/%s' height='8px' title='%s'>"
				, urlUtilsService.getSiteImagesPath()
				, rankIcon.getIcon()
				, rankIcon.getTitle()
			));
		}

		photoEntry.setPhotoAuthorRank( builder.toString() );
	}

	private void setPhotoAnonymousPeriodExpiration( final Photo photo, User accessor, final PhotoEntryDTO photoEntry, final Language language ) {
		if ( securityService.isPhotoWithingAnonymousPeriod( photo ) ) {
			final String expirationInfo = translatorService.translate( "Photo preview: Anonymous posting till $1", language, dateUtilsService.formatDateTimeShort( photoService.getPhotoAnonymousPeriodExpirationTime( photo ) ) );
			photoEntry.setPhotoAnonymousPeriodExpirationInfo( expirationInfo );
		}
	}

	private User getCurrentUser() {
		return EnvironmentContext.getCurrentUser();
	}

	public void setPhotoService( final PhotoService photoService ) {
		this.photoService = photoService;
	}

	public void setPhotoCommentService( final PhotoCommentService photoCommentService ) {
		this.photoCommentService = photoCommentService;
	}

	public void setPhotoPreviewService( final PhotoPreviewService photoPreviewService ) {
		this.photoPreviewService = photoPreviewService;
	}

	public void setUserService( final UserService userService ) {
		this.userService = userService;
	}

	public void setGenreService( final GenreService genreService ) {
		this.genreService = genreService;
	}

	public void setUserPhotoFilePathUtilsService( final UserPhotoFilePathUtilsService userPhotoFilePathUtilsService ) {
		this.userPhotoFilePathUtilsService = userPhotoFilePathUtilsService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setEntityLinkUtilsService( final EntityLinkUtilsService entityLinkUtilsService ) {
		this.entityLinkUtilsService = entityLinkUtilsService;
	}

	public void setConfigurationService( final ConfigurationService configurationService ) {
		this.configurationService = configurationService;
	}

	public void setUrlUtilsService( final UrlUtilsService urlUtilsService ) {
		this.urlUtilsService = urlUtilsService;
	}

	public void setPhotoVotingService( final PhotoVotingService photoVotingService ) {
		this.photoVotingService = photoVotingService;
	}

	public void setTranslatorService( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
	}

	public void setUserRankService( final UserRankService userRankService ) {
		this.userRankService = userRankService;
	}

	public void setSecurityService( final SecurityService securityService ) {
		this.securityService = securityService;
	}

	public void setSecurityUIService( final SecurityUIService securityUIService ) {
		this.securityUIService = securityUIService;
	}

	public void setFavoritesService( final FavoritesService favoritesService ) {
		this.favoritesService = favoritesService;
	}

	public void setRestrictionService( final RestrictionService restrictionService ) {
		this.restrictionService = restrictionService;
	}

	public void setUserPhotoAlbumService( final UserPhotoAlbumService userPhotoAlbumService ) {
		this.userPhotoAlbumService = userPhotoAlbumService;
	}
}
