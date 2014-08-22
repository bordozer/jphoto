package core.services.photo.list;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.list.factory.*;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.sql.PhotoSqlHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.SqlIdsSelectQuery;
import ui.services.UtilsService;
import utils.UserUtils;

public class PhotoListFactoryServiceImpl implements PhotoListFactoryService {

	@Autowired
	private PhotoListCriteriasService photoListCriteriasService;

	@Autowired
	private PhotoListFilteringService photoListFilteringService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PhotoSqlHelperService photoSqlHelperService;

	@Autowired
	private Services services;

	@Override
	public AbstractPhotoListFactory gallery( final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForAllPhotos( accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery", services );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: All photos", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryTopBest( final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForPhotoGalleryTopBest( accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.topBestFilteringStrategy();

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery top best for last $1 days", services ).addIntegerParameter( days() );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosBestInPeriodUrl( criterias.getVotingTimeFrom(), criterias.getVotingTimeTo() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Top best photos for last $1 days", services ).addIntegerParameter( days() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryBest( final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForAbsolutelyBest( accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery absolutely best", services );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery absolutely best", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenre( final Genre genre, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForGenre( genre, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1", services ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "$1", services ).string( genre.getDescription() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenreTopBest( final Genre genre, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForGenreTopBest( genre, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.topBestFilteringStrategy();

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 top best for last $2 days", services ).addPhotosByGenreLinkParameter( genre ).addIntegerParameter( days() );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByGenreLinkBest( genre.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by genre $1 top best for last $2 days", services ).addPhotosByGenreLinkParameter( genre ).addIntegerParameter( days() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenreBest( final Genre genre, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForGenreBestForPeriod( genre, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 best for $2 days", services ).addPhotosByGenreLinkParameter( genre ).addIntegerParameter( days() );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "$1", services ).string( genre.getDescription() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUser( final User user, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUser( user, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1", services ).userCardLink( user );
			}

			@Override
			protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {

				if ( UserUtils.isUsersEqual( user, accessor ) ) {
					return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
				}

				return super.getGroupOperationMenuContainer();
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: All photos of $1", services ).userCardLink( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserTopBest( final User user, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserTopBest( user, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 top best", services ).userCardLink( user );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 top best", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserBest( final User user, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAbsolutelyBest( user, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 best", services ).userCardLink( user );
			}

			@Override
			protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {

				if ( UserUtils.isUsersEqual( user, accessor ) ) {
					return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
				}

				return super.getGroupOperationMenuContainer();
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 best", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenre( final Genre genre, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenre( accessor, genre, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( accessor, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2", services ).userCardLink( accessor ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 and genre $2", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenreTopBest( final User user, final Genre genre, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenreTopBest( user, genre, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 top best", services ).userCardLink( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserByGenreLinkBest( user.getId(), genre.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo list title: Photo gallery by user $1 and genre $2 top best", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenreBest( final User user, final Genre genre, final PagingModel pagingModel, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenreAbsolutelyBest( user, genre, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 best", services ).userCardLink( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 and genre $2 best", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory appraisedByUserPhotos( final User user, final PagingModel pagingModel, final User accessor ) {

		final PhotoListCriterias criterias = photoListCriteriasService.getForAppraisedByUserPhotos( user, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photos which the user $1 appraised", services ).userCardLink( user );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photos which the user $1 appraised", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userTeamMemberPhotosLast( final User user, final UserTeamMember userTeamMember, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getUserTeamMemberPhotosQuery( user, userTeamMember, 1, getTopPhotoListPhotosCount() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: last photos with team member $2 ( $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getUserTeamMemberCardLink( user.getId(), userTeamMember.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: the latest team member $2 photos ( member type is $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userTeamMemberPhotos( final User user, final UserTeamMember userTeamMember, final int page, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getUserTeamMemberPhotosQuery( user, userTeamMember, page, getAccessorPhotosOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: all photos of $2 ( $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: all team member $2 photos ( member type is $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userAlbumPhotosLast( final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getUserPhotoAlbumPhotosQuery( user, userPhotoAlbum, 1, getTopPhotoListPhotosCount() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: the latest photos from album $2", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getUserPhotoAlbumPhotosLink( user.getId(), userPhotoAlbum.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: the latest photos from album $2 photos", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userAlbumPhotos( final User user, final UserPhotoAlbum userPhotoAlbum, final int page, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getUserPhotoAlbumPhotosQuery( user, userPhotoAlbum, page, getAccessorPhotosOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: all photos from album $2", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: all photos from album $2", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userCardPhotosBest( final User user, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosBest( user, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, 1, getTopPhotoListPhotosCount() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User card $1: the best photos", services )
					.userCardLink( user )
					;
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom tex: User card $1: the best photos", services )
					.userCardLink( user )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userCardPhotosLast( final User user, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosLast( user, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, 1, getTopPhotoListPhotosCount() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User card $1: the latest photos", services )
					.userCardLink( user )
					;
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLink( user.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom tex: User card $1: the latest photos", services )
					.userCardLink( user )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userCardPhotosLastAppraised( final User user, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardLastAppraisedPhotos( user, accessor );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, 1, getTopPhotoListPhotosCount() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User card $1: last appraised photos", services )
					.userCardLink( user )
					;
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosAppraisedByUserLink( user.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom tex: User card $1: last appraised photos", services )
					.userCardLink( user )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userBookmarkedPhotos( final User user, final FavoriteEntryType favoriteEntryType, final int page, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getFavoritesPhotosSQL( user.getId(), favoriteEntryType, page, getAccessorPhotosOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: bookmarked photos $2", services )
					.userCardLink( user )
					.translatableString( favoriteEntryType.getName() )
					;
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: bookmarked photos $2", services )
					.userCardLink( user )
					.translatableString( favoriteEntryType.getName() )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory photosOfFavoriteAuthorsOfUser( final User user, final int page, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getPhotosOfUserFavoritesMembersSQL( user, page, getAccessorPhotosOnPage() );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: photos of favorite authors", services )
					.userCardLink( user )
					;
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: photos of favorite authors", services )
					.userCardLink( user )
					;
			}
		};
	}

	private SqlIdsSelectQuery getQuery( final PhotoListCriterias criterias, final int page, final int itemsOnPage ) {
		return photoSqlHelperService.getForCriteriasPagedIdsSQL( criterias, page, itemsOnPage );
	}

	private int days() {
		return configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
	}

	private int getTopPhotoListPhotosCount() {
		return configurationService.getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY );
	}
}

	/*@Override
	public AbstractPhotoListFactory userCardPhotosOfLastVisitors( final User user, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return getQuery( user );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User card $1: last appraised photos", services )
					.userCardLink( user )
					;
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
			}

			@Override
			protected TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom tex: User card $1: last appraised photos", services )
					.userCardLink( user )
					;
			}

			private SqlIdsSelectQuery getQuery( final User user ) {
				final List<Integer> userIds = photoPreviewService.getLastUsersWhoViewedUserPhotos( user.getId(), photosQty );
				final List<Integer> photosIds = newArrayList();
				for ( final int userId : userIds ) {
					final int lastUserPhotoId = getLastUserPhotoId( userId );
					if ( lastUserPhotoId > 0 ) {
						photosIds.add( lastUserPhotoId );
					}
				}

				return photosIds;
			}
		};
	}*/