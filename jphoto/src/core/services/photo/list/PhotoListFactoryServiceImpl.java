package core.services.photo.list;

import core.enums.FavoriteEntryType;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.photo.list.factory.*;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.PhotoListQueryBuilder;
import core.services.utils.sql.PhotoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;

public class PhotoListFactoryServiceImpl implements PhotoListFactoryService {

	private static final int USER_CARD_BEST_MIN_MARKS = 1;

	private static final String SORTING_BY_UPLOAD_TIME_DESC = "Photo list bottom text: Sorted by upload time DESC";
	private static final String SORTING_BY_SUM_MARKS_DESC = "Photo list bottom text: Sorted by sum marks DESC";

	@Autowired
	private PhotoListFilteringService photoListFilteringService;

	@Autowired
	private PhotoQueryService photoQueryService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private Services services;

	@Override
	public AbstractPhotoListFactory gallery( final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery", services );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: All photos.", services )
					.lineBreakHtml()
					.translatableString( SORTING_BY_UPLOAD_TIME_DESC )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryTopBest( final User accessor ) {

		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.topBestFilteringStrategy();

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery().getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery top best for last $1 days", services ).addIntegerParameter( days );
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosBestInPeriodUrl( timeRange.getTimeFrom(), timeRange.getTimeTo() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Top best photos that got at least $1 marks in period $2 - $3", services )
					.addIntegerParameter( minMarks )
					.dateFormatted( timeRange.getTimeFrom() )
					.dateFormatted( timeRange.getTimeTo() )
					.lineBreakHtml()
					.translatableString( SORTING_BY_SUM_MARKS_DESC )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryAbsolutelyBest( final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder().filterByMinimalMarks( minMarks ).sortBySumMarksDesc().forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery absolutely best", services );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery absolutely best which got at least $1 marks.", services )
					.addIntegerParameter( minMarks )
					.lineBreakHtml()
					.translatableString( "Photo list bottom text: Sorted by total marks." )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenre( final Genre genre, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery( page, itemsOnPage ).filterByGenre( genre ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1", services ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1", services )
					.addGenreNameParameter( genre )
					.lineBreakHtml()
					.translatableString( SORTING_BY_UPLOAD_TIME_DESC )
					;
			}

			@Override
			public TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "$1", services ).string( genre.getDescription() ); // TODO: translations!
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenreTopBest( final Genre genre, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.topBestFilteringStrategy();

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery()
					.filterByGenre( genre )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 top best for last $2 days", services )
					.addGenreNameParameter( genre )
					.addIntegerParameter( days );
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByGenreLinkBest( genre.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Top best photos by genre $1 that got at least $2 marks in period $3 - $4", services )
					.addGenreNameParameter( genre )
					.addIntegerParameter( minMarks )
					.dateFormatted( timeRange.getTimeFrom() )
					.dateFormatted( timeRange.getTimeTo() )
					.lineBreakHtml()
					.translatableString( SORTING_BY_SUM_MARKS_DESC )
					;

			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenreBest( final Genre genre, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery().filterByGenre( genre ).forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 best for $2 days", services )
					.addPhotosByGenreLinkParameter( genre )
					.addIntegerParameter( days )
					;
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: The best photos from category $1 which got at least $2 marks in period $3 - $4", services )
					.addGenreNameParameter( genre )
					.addIntegerParameter( minMarks )
					.dateFormatted( timeRange.getTimeFrom() )
					.dateFormatted( timeRange.getTimeTo() )
					.lineBreakHtml()
					.translatableString( SORTING_BY_SUM_MARKS_DESC )
					;
			}

			@Override
			public TranslatableMessage getPhotoListBottomText() {
				return new TranslatableMessage( "$1", services ).string( genre.getDescription() ); // TODO: the description is untranslated!
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryUploadedInDateRange( final Date timeFrom, final Date timeTo, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery( page, itemsOnPage ).filterByUploadTime( timeFrom, timeTo ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photos uploaded between $1 and $2", services ).dateFormatted( timeFrom ).dateFormatted( timeTo );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photos uploaded between $1 and $2", services )
					.dateFormatted( timeFrom )
					.dateFormatted( timeTo )
					.lineBreakHtml()
					.translatableString( SORTING_BY_UPLOAD_TIME_DESC );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryUploadedInDateRangeBest( final Date timeFrom, final Date timeTo, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery().forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: The best photos for period $1 - $2", services )
					.dateFormatted( timeFrom )
					.dateFormatted( timeTo )
					;
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: The best photos which got at least $1 marks in period $2 - $3", services )
					.addIntegerParameter( minMarks )
					.dateFormatted( timeFrom )
					.dateFormatted( timeTo )
					.lineBreakHtml()
					.translatableString( SORTING_BY_SUM_MARKS_DESC )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryByUserMembershipType( final UserMembershipType userMembershipType, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery( page, itemsOnPage ).filterByMembershipType( userMembershipType ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Main menu: photos: " + userMembershipType.getName(), services );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photos of users with membership type $1. $2.", services ).translatableString( userMembershipType.getNamePlural() ).translatableString( SORTING_BY_UPLOAD_TIME_DESC );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryByUserMembershipTypeTopBest( final UserMembershipType userMembershipType, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.topBestFilteringStrategy();

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery()
					.filterByMembershipType( userMembershipType )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Main menu: The best photos: " + userMembershipType.getName(), services );
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByMembershipBest( userMembershipType, "photos" );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: The top best photos of users with membership type $1 which got at least $2 masks in period $3 - $4.", services )
					.translatableString( userMembershipType.getNamePlural() )
					.addIntegerParameter( minMarks )
					.dateFormatted( timeRange.getTimeFrom() )
					.dateFormatted( timeRange.getTimeTo() )
					.lineBreakHtml()
					.translatableString( SORTING_BY_SUM_MARKS_DESC )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryByUserMembershipTypeBest( final UserMembershipType userMembershipType, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery().filterByMembershipType( userMembershipType ).forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Main menu: The best photos: " + userMembershipType.getName(), services );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: The best photos of users with membership type $1. $2.", services )
					.translatableString( userMembershipType.getNamePlural() )
					.translatableString( SORTING_BY_SUM_MARKS_DESC )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUser( final User user, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery( page, itemsOnPage ).filterByAuthor( user ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1", services ).userCardLink( user );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: All photos of $1", services ).userCardLink( user );
			}

			@Override
			public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
				return getPhotoGroupOperationMenuContainerForUserCard( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserTopBest( final User user, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder()
					.filterByAuthor( user )
					.filterByMinimalMarks( USER_CARD_BEST_MIN_MARKS )
					.sortBySumMarksDesc()
					.forPage( 1, getTopListPhotosCount() )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 top best", services ).userCardLink( user );
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 top best", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserBest( final User user, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder().filterByAuthor( user ).filterByMinimalMarks( USER_CARD_BEST_MIN_MARKS ).sortBySumMarksDesc().forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 best", services ).userCardLink( user );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 best", services );
			}

			@Override
			public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
				return getPhotoGroupOperationMenuContainerForUserCard( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenre( final User user, final Genre genre, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return getBaseQuery( page, itemsOnPage ).filterByAuthor( user ).filterByGenre( genre ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2", services ).userCardLink( accessor ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 and genre $2", services ).userCardLink( accessor ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
				return getPhotoGroupOperationMenuContainerForUserCard( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenreTopBest( final User user, final Genre genre, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder()
					.filterByAuthor( user )
					.filterByGenre( genre )
					.filterByMinimalMarks( USER_CARD_BEST_MIN_MARKS )
					.sortBySumMarksDesc()
					.forPage( 1, getTopListPhotosCount() )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 top best", services ).userCardLink( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserByGenreLinkBest( user.getId(), genre.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo list title: Photo gallery by user $1 and genre $2 top best", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenreBest( final User user, final Genre genre, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder().filterByAuthor( user ).filterByGenre( genre ).filterByMinimalMarks( USER_CARD_BEST_MIN_MARKS ).sortBySumMarksDesc().forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 best", services ).userCardLink( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photo gallery by user $1 and genre $2 best", services ).userCardLink( accessor ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
				return getPhotoGroupOperationMenuContainerForUserCard( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory appraisedByUserPhotos( final User user, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder().filterByVotedUser( user ).sortByVotingTimeDesc().forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photos which the user $1 appraised", services ).userCardLink( user );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photos which the user $1 appraised", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory appraisedByUserPhotos( final User user, final PhotoVotingCategory photoVotingCategory, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder().filterByVotedUser( user ).filterByVotingCategory( photoVotingCategory ).sortByVotingTimeDesc().forPage( page, itemsOnPage ).getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photos which the user $1 appraised", services ).userCardLink( user );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: Photos which the user $1 appraised", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userTeamMemberPhotosLast( final User user, final UserTeamMember userTeamMember, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder()
					.filterByAuthor( user )
					.filterByUserTeamMember( userTeamMember )
					.forPage( 1, getTopListPhotosCount() )
					.sortByUploadTimeDesc()
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: last photos with team member $2 ( $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getUserTeamMemberCardLink( user.getId(), userTeamMember.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: the latest team member $2 photos ( member type is $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userTeamMemberPhotos( final User user, final UserTeamMember userTeamMember, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder()
					.filterByAuthor( user )
					.filterByUserTeamMember( userTeamMember )
					.forPage( page, itemsOnPage )
					.sortByUploadTimeDesc()
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: all photos of $2 ( $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: all team member $2 photos ( member type is $3 )", services )
					.userCardLink( user )
					.userTeamMemberCardLink( userTeamMember )
					.translatableString( userTeamMember.getTeamMemberType().getName() )
					;
			}

			@Override
			public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
				return getPhotoGroupOperationMenuContainerForUserCard( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userAlbumPhotosLast( final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return new PhotoListQueryBuilder( dateUtilsService )
					.filterByAuthor( user )
					.filterByUserAlbum( userPhotoAlbum )
					.forPage( 1, getTopListPhotosCount() )
					.sortByUploadTimeDesc()
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: the latest photos from album $2", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getUserPhotoAlbumPhotosLink( user.getId(), userPhotoAlbum.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: the latest photos from album $2 photos", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userAlbumPhotos( final User user, final UserPhotoAlbum userPhotoAlbum, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return new PhotoListQueryBuilder( dateUtilsService )
					.filterByAuthor( user )
					.filterByUserAlbum( userPhotoAlbum )
					.forPage( page, itemsOnPage )
					.sortByUploadTimeDesc()
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: all photos from album $2", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: all photos from album $2", services )
					.userCardLink( user )
					.userAlbumLink( userPhotoAlbum )
					;
			}

			@Override
			public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
				return getPhotoGroupOperationMenuContainerForUserCard( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userCardPhotosBest( final User user, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder().filterByAuthor( user )
					.filterByMinimalMarks( USER_CARD_BEST_MIN_MARKS )
					.sortBySumMarksDesc()
					.forPage( 1, getTopListPhotosCount() )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User card $1: the best photos", services )
					.userCardLink( user )
					;
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom tex: User card $1: the best photos", services )
					.userCardLink( user )
					;
			}

			@Override
			public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
				return getPhotoGroupOperationMenuContainerForUserCard( user );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userCardPhotosLast( final User user, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder()
					.filterByAuthor( user )
					.sortByUploadTimeDesc()
					.forPage( 1, getTopListPhotosCount() )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User card $1: the latest photos", services )
					.userCardLink( user )
					;
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLink( user.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom tex: User card $1: the latest photos", services )
					.userCardLink( user )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userCardPhotosLastAppraised( final User user, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryTopBest( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder()
					.filterByVotedUser( user )
					.sortByVotingTimeDesc()
					.forPage( 1, getTopListPhotosCount() )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User card $1: last appraised photos", services )
					.userCardLink( user )
					;
			}

			@Override
			public String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosAppraisedByUserLink( user.getId() );
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom tex: User card $1: last appraised photos", services )
					.userCardLink( user )
					;
			}
		};
	}

	@Override
	public AbstractPhotoListFactory userBookmarkedPhotos( final User user, final FavoriteEntryType favoriteEntryType, final int page, final int itemsOnPage, final User accessor ) {
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( accessor );

		return new PhotoListFactoryGallery( filteringStrategy, accessor, services ) {

			@Override
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return builder()
					.filterOnlyPhotosAddedByUserToBookmark( user, favoriteEntryType )
					.forPage( page, itemsOnPage )
					.getQuery();
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: bookmarked photos $2", services )
					.userCardLink( user )
					.translatableString( favoriteEntryType.getName() )
					;
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
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
			public SqlIdsSelectQuery getSelectIdsQuery() {
				return photoQueryService.getPhotosOfUserFavoritesMembersSQL( user, page, getAccessorPhotosOnPage() );
			}

			@Override
			public TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: User $1: photos of favorite authors", services )
					.userCardLink( user )
					;
			}

			@Override
			public TranslatableMessage getCriteriaDescription() {
				return new TranslatableMessage( "Photo list bottom text: User $1: photos of favorite authors", services )
					.userCardLink( user )
					;
			}
		};
	}

	@Override
	public TimeRange getTimeRange( final int days ) {
		return new TimeRange( dateUtilsService.getDatesOffsetFromCurrentDate( -days + 1 ), dateUtilsService.getCurrentTime() );
	}

	private PhotoListQueryBuilder builder() {
		return new PhotoListQueryBuilder( dateUtilsService );
	}

	public void setPhotoListFilteringService( final PhotoListFilteringService photoListFilteringService ) {
		this.photoListFilteringService = photoListFilteringService;
	}

	public void setPhotoQueryService( final PhotoQueryService photoQueryService ) {
		this.photoQueryService = photoQueryService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setServices( final Services services ) {
		this.services = services;
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
				return urlUtilsService.getPhotosByUserLinkBest( user.getId() );
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