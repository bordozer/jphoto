package ui.services;

import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSiteUrlHelper;
import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoRating;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoImportData;
import core.general.photo.PhotoInfo;
import core.general.photo.PhotoPreviewWrapper;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.*;
import core.services.security.SecurityService;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import core.services.utils.DateUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.controllers.users.card.MarksByCategoryInfo;
import ui.services.security.SecurityUIService;

import java.util.Date;
import java.util.List;

public class PhotoUIServiceImpl implements PhotoUIService {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SecurityUIService securityUIService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private CacheService<PhotoInfo> cacheServicePhotoInfo;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private PhotoCommentArchService photoCommentArchService;

	@Autowired
	private PhotoPreviewService photoPreviewService;

	@Autowired
	private PhotoAwardService photoAwardService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoRatingService photoRatingService;

	@Override
	public PhotoInfo getPhotoInfo( final Photo photo, final User accessor ) {
		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeToday();

		return getPhotoInfo( photo, timeRangeToday.getTimeFrom(), timeRangeToday.getTimeTo(), accessor );
	}

	private PhotoInfo getPhotoInfo( final Photo photo, final Date timeFrom, final Date timeTo, final User accessor ) {
		final PhotoInfo photoInfo = cacheServicePhotoInfo.getEntry( CacheKey.PHOTO_INFO, photo.getId(), new CacheEntryFactory<PhotoInfo>() {
			@Override
			public PhotoInfo createEntry() {
				return loadCachablePhotoInfoPart( photo );
			}
		} );

		// TODO: marksByCategoryInfos can be loaded in cacheable part if update PhotoVotingCategory to prevent caching
		final List<MarksByCategoryInfo> marksByCategoryUser = photoVotingService.getPhotoSummaryVoicesByPhotoCategories( photo );
		photoInfo.setMarksByCategoryInfos( marksByCategoryUser );

		updateNotCachedInPhotoInfoEntries( photo, photoInfo, timeFrom, timeTo, accessor );

		photoInfo.setPhotoTeam( userTeamService.getPhotoTeam( photo.getId() ) );

		photoInfo.setUserPhotoAlbums( userPhotoAlbumService.loadPhotoAlbums( photo.getId() ) );

		final int photoAuthorId = photo.getUserId();
		final int genreId = photo.getGenreId();
		photoInfo.setPhotoAuthorRankInGenre( userRankService.getUserRankInGenre( photoAuthorId, genreId ) );

		// TODO: duplicates UserRankServiceImpl.getVotingModel() -->
		final int minPhotosQtyForGenreRankVoting = configurationService.getInt( ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE );
		final int userPhotosInGenre = photoService.getPhotosCountByUserAndGenre( photoAuthorId, genreId );
		photoInfo.setPhotoAuthorHasEnoughPhotosInGenre( userPhotosInGenre >= minPhotosQtyForGenreRankVoting  );
		// TODO: duplicates UserRankServiceImpl.getVotingModel() <--

		photoInfo.setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( securityUIService.isPhotoHasToBeHiddenBecauseOfNudeContent( photo, accessor ) );

		photoInfo.setPhotoPreviewMustBeHidden( false );

		photoInfo.setUserCanEditPhoto( securityService.userCanEditPhoto( accessor, photo ) );
		photoInfo.setUserCanDeletePhoto( securityService.userCanDeletePhoto( accessor, photo ) );

		photoInfo.setSuperAdminUser( securityService.isSuperAdminUser( accessor.getId() ) );
		photoInfo.setCommentsCount( photo.isArchived() ? photoCommentArchService.getPhotoCommentsCount( photo.getId() ) : photoCommentService.getPhotoCommentsCount( photo.getId() ) );

		photoInfo.setShowStatisticInPhotoList( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_STATISTIC ) );
		photoInfo.setShowUserRankInGenreInPhotoList( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_USER_RANK_IN_GENRE ) );

		final PhotoImportData photoImportData = photo.getPhotoImportData();
		if ( photoImportData != null ) {
			final AbstractRemotePhotoSiteUrlHelper remotePhotoSiteUrlHelper = AbstractRemotePhotoSiteUrlHelper.getInstance( photoImportData.getPhotosImportSource() );

			photoInfo.setRemoteSourceLink( String.format( "<a href='http://%1$s' target='_blank'>%1$s</a>", remotePhotoSiteUrlHelper.getRemotePhotoSiteHost() ) );
			photoInfo.setRemoteUserLink( remotePhotoSiteUrlHelper.getRemoteUserCardLink( photoImportData.getRemoteUserId(), photoImportData.getRemoteUserName() ) );
			photoInfo.setRemotePhotoLink( String.format( "<a href='%s' target='_blank'>%s</a>"
				, remotePhotoSiteUrlHelper.getPhotoCardUrl( photoImportData.getRemoteUserId(), photoImportData.getRemotePhotoId() )
				, photo.getNameEscaped()
				)
			);
		}

		return photoInfo;
	}

	@Override
	public PhotoPreviewWrapper getPhotoPreviewWrapper( final Photo photo, final User user ) {
		final Genre genre = genreService.load( photo.getId() );
		final String photoPreviewUrl = userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo );
		final PhotoPreviewWrapper photoPreviewWrapper = new PhotoPreviewWrapper( photo, genre, photoPreviewUrl );
		photoPreviewWrapper.setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( securityUIService.isPhotoHasToBeHiddenBecauseOfNudeContent( photo, user ) );

		return photoPreviewWrapper;
	}

	private PhotoInfo loadCachablePhotoInfoPart( final Photo photo ) {
		final int photoId = photo.getId();
		final PhotoInfo photoInfo = new PhotoInfo( photo );

		final Date lastSecondOfToday = dateUtilsService.getLastSecondOfToday();
		final TimeRange dateRange = photoVotingService.getTopBestDateRange();
		photoInfo.setTopBestMarks( photoVotingService.getPhotoMarksForPeriod( photoId, dateRange.getTimeFrom(), dateRange.getTimeTo() ) ); // TODO: reset this at midnight

		photoInfo.setTodayMarks( photoVotingService.getPhotoMarksForPeriod( photoId, dateUtilsService.getFirstSecondOfToday(), lastSecondOfToday ) ); // TODO: reset this at midnight

		photoInfo.setPreviewCount( photoPreviewService.getPreviewCount( photoId ) );

		photoInfo.setPhotoAwards( photoAwardService.getPhotoAwards( photoId ) );

		photoInfo.setPhotoImgUrl( userPhotoFilePathUtilsService.getPhotoImageUrl( photo ) );
		photoInfo.setPhotoPreviewImgUrl( userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );

		return photoInfo;
	}

	private void updateNotCachedInPhotoInfoEntries( final Photo photo, final PhotoInfo photoInfo, final Date timeFrom, final Date timeTo, final User accessor ) {
		photoInfo.setPhoto( photo );

		final User user = userService.load( photo.getUserId() );
		photoInfo.setUser( user );

		final boolean isPhotoAuthorNameMustBeHidden = securityService.isPhotoAuthorNameMustBeHidden( photo, accessor );
		photoInfo.setPhotoAuthorNameMustBeHidden( isPhotoAuthorNameMustBeHidden );
		if ( isPhotoAuthorNameMustBeHidden ) {
			photoInfo.setPhotoAuthorAnonymousName( configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME ) );
			photoInfo.setPhotoAnonymousPeriodExpirationTime( photoService.getPhotoAnonymousPeriodExpirationTime( photo ) );
		}

		final Genre genre = genreService.load( photo.getGenreId() );

		photoInfo.setGenre( genre );

		if ( dateUtilsService.isNotEmptyTime( timeFrom ) && dateUtilsService.isNotEmptyTime( timeTo ) ) {
			final PhotoRating photoRatingPosition = photoRatingService.getPhotoRatingForPeriod( photo.getId(), timeFrom, timeTo );
			final boolean showPhotoRatingPosition = photoRatingPosition != null;
			photoInfo.setShowPhotoRatingPosition( showPhotoRatingPosition );
			if ( showPhotoRatingPosition ) {
				photoInfo.setPhotoRatingPosition( photoRatingPosition.getRatingPosition() );

				final String dateFrom = dateUtilsService.formatDate( timeFrom );
				final String dateTo = dateUtilsService.formatDate( timeTo );

				final boolean areDatesEquals = dateFrom.equals( dateTo );
				String dateInterval;
				final String currentDate = dateUtilsService.formatDate( dateUtilsService.getCurrentDate() );
				if ( areDatesEquals && dateFrom.equals( currentDate ) ) {
					dateInterval = translatorService.translate( "today", accessor.getLanguage() );
				} else {
					dateInterval = areDatesEquals ? dateFrom : String.format( "%s - %s", dateFrom, dateTo );
				}

				final String photoRatingPositionDescription = translatorService.translate( "Photo's rating position on $1", accessor.getLanguage(), dateInterval );
				photoInfo.setPhotoRatingPositionDescription( photoRatingPositionDescription );
			}
		}

		photoInfo.setUserRankWhenPhotoWasUploadedIconContainer( userRankService.getUserRankIconContainer( user, photo ) );
		photoInfo.setUserRankIconContainer( userRankService.getUserRankIconContainer( user, genre ) );
	}

	private int getSummaryPhotoMark( final List<MarksByCategoryInfo> marksByCategories ) {
		int sumMark = 0;

		for ( final MarksByCategoryInfo marksByCategory : marksByCategories ) {
			sumMark += marksByCategory.getSumMark();
		}

		return sumMark;
	}
}
