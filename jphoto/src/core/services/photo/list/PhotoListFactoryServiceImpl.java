package core.services.photo.list;

import core.general.base.PagingModel;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.list.factory.*;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.sql.PhotoCriteriasSqlService;
import core.services.utils.sql.PhotoSqlHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.SqlIdsSelectQuery;
import utils.UserUtils;

public class PhotoListFactoryServiceImpl implements PhotoListFactoryService {

	@Autowired
	private PhotoListCriteriasService photoListCriteriasService;

	@Autowired
	private PhotoListFilteringService photoListFilteringService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PhotoCriteriasSqlService photoCriteriasSqlService;

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
				return getQuery( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery", services );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
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
				return getQuery( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery top best for last $1 days", services ).addIntegerParameter( days() );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosBestInPeriodUrl( criterias.getVotingTimeFrom(), criterias.getVotingTimeTo() );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
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
				return getQuery( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery absolutely best", services );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return getQuery( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1", services ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
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
				return getQuery( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 top best for last $2 days", services ).addPhotosByGenreLinkParameter( genre ).addIntegerParameter( days() );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByGenreLinkBest( genre.getId() );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return getQuery( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 best for $2 days", services ).addPhotosByGenreLinkParameter( genre ).addIntegerParameter( days() );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
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
				return getQuery( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1", services ).userCardLink( user );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {

				if ( UserUtils.isUsersEqual( user, accessor ) ) {
					return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
				}

				return super.getGroupOperationMenuContainer();
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 top best", services ).userCardLink( user );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 best", services ).userCardLink( user );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {

				if ( UserUtils.isUsersEqual( user, accessor ) ) {
					return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
				}

				return super.getGroupOperationMenuContainer();
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2", services ).userCardLink( accessor ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 top best", services ).userCardLink( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserByGenreLinkBest( user.getId(), genre.getId() );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 best", services ).userCardLink( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
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
				return photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photos which the user $1 appraised", services ).userCardLink( user );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userTeamMemberLastPhotos( final User user, final UserTeamMember userTeamMember, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			protected SqlIdsSelectQuery getSelectIdsQuery() {
				return photoSqlHelperService.getUserTeamMemberLastPhotosQuery( user.getId(), userTeamMember.getId(), 1, configurationService.getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY ) );
			}

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1 top best photos of team member $2 ( $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {

				if ( UserUtils.isUsersEqual( user, accessor ) ) {
					return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
				}

				return super.getGroupOperationMenuContainer();
			}

			@Override
			protected TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "Photo list bottom text: ", services );
			}
		};
	}

	private SqlIdsSelectQuery getQuery( final PhotoListCriterias criterias, final PagingModel pagingModel ) {
		return photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, pagingModel );
	}

	private int days() {
		return configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
	}
}
