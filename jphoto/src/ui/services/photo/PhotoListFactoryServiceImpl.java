package ui.services.photo;

import core.general.base.PagingModel;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoListFilteringService;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import ui.controllers.photos.list.factory.*;
import utils.UserUtils;

import java.util.Date;

public class PhotoListFactoryServiceImpl implements PhotoListFactoryService {

	@Autowired
	private PhotoListCriteriasService photoListCriteriasService;

	@Autowired
	private PhotoListFilteringService photoListFilteringService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private Services services;

	@Override
	public AbstractPhotoListFactory gallery( final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForAllPhotos( getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( getCurrentUser() );

		return new PhotoListFactoryGallery( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery", services );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryTopBest( final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForPhotoGalleryTopBest( getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.topBestFilteringStrategy();

		return new PhotoListFactoryTopBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery top best", services );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosBestInPeriodUrl( criterias.getVotingTimeFrom(), criterias.getVotingTimeTo() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryBest( final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForAbsolutelyBest( getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( getCurrentUser() );

		return new PhotoListFactoryBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery absolutely best", services );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenre( final Genre genre, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForGenre( genre, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( getCurrentUser() );

		return new PhotoListFactoryGallery( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1", services ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getPhotoListBottomText() {
				return genre.getDescription(); // TODO: translate
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenreTopBest( final Genre genre, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForGenreTopBest( genre, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.topBestFilteringStrategy();

		return new PhotoListFactoryTopBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 top best", services ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByGenreLinkBest( genre.getId() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForGenreBest( final Genre genre, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForGenreBestForPeriod( genre, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.bestFilteringStrategy( getCurrentUser() );

		return new PhotoListFactoryBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 best for $2 days ", services ).addPhotosByGenreLinkParameter( genre ).addIntegerParameter( days() );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getPhotoListBottomText() {
				return genre.getDescription(); // TODO: translate
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUser( final User user, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUser( user, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, getCurrentUser() );

		return new PhotoListFactoryGallery( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1", services ).addUserCardLinkParameter( user );
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
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserTopBest( final User user, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserTopBest( user, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, getCurrentUser() );

		return new PhotoListFactoryTopBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by genre $1 top best", services ).addUserCardLinkParameter( user );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserLinkBest( user.getId() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserBest( final User user, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAbsolutelyBest( user, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, getCurrentUser() );

		return new PhotoListFactoryBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 best", services ).addUserCardLinkParameter( user );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenre( final User user, final Genre genre, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenre( user, genre, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, getCurrentUser() );

		return new PhotoListFactoryGallery( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2", services ).addUserCardLinkParameter( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenreTopBest( final User user, final Genre genre, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenreTopBest( user, genre, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, getCurrentUser() );

		return new PhotoListFactoryTopBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 top best", services ).addUserCardLinkParameter( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}

			@Override
			protected String getLinkToFullList() {
				return services.getUrlUtilsService().getPhotosByUserByGenreLinkBest( user.getId(), genre.getId() );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory galleryForUserAndGenreBest( final User user, final Genre genre, final PagingModel pagingModel ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getForUserAndGenreAbsolutelyBest( user, genre, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.userCardFilteringStrategy( user, getCurrentUser() );

		return new PhotoListFactoryBest( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photo gallery by user $1 and genre $2 best", services ).addUserCardLinkParameter( user ).addPhotosByGenreLinkParameter( genre );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}
		};
	}

	@Override
	public AbstractPhotoListFactory appraisedByUserPhotos( final User user, final PagingModel pagingModel ) {

		final PhotoListCriterias criterias = photoListCriteriasService.getForAppraisedByUserPhotos( user, getCurrentUser() );
		final AbstractPhotoFilteringStrategy filteringStrategy = photoListFilteringService.galleryFilteringStrategy( getCurrentUser() );

		return new PhotoListFactoryGallery( criterias, filteringStrategy, getCurrentUser(), services ) {

			@Override
			protected TranslatableMessage getTitle() {
				return new TranslatableMessage( "Photo list title: Photos which the user $1 appraised", services ).addUserCardLinkParameter( user );
			}

			@Override
			protected TranslatableMessage getDescription() {
				return new TranslatableMessage( "", services );
			}
		};
	}

	private User getCurrentUser() {
		return EnvironmentContext.getCurrentUser();
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	private Date getCurrentTime() {
		return dateUtilsService.getCurrentTime();
	}

	private int days() {
		return configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
	}
}
