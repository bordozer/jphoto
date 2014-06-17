package core.services.photo;

import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.translator.Language;

import java.util.Date;

public interface PhotoListCriteriasService {

	String getLinkToFullListText( final PhotoListCriterias topBestPhotoListCriterias, final Language language );

	PhotoListCriterias getForAllPhotos( final User user );

	PhotoListCriterias getForAllPhotosTopBest( final User user );

	PhotoListCriterias getForAbsolutelyBest( final User user );

	PhotoListCriterias getForGenre( final Genre genre, final User user );

	PhotoListCriterias getForGenreTopBest( final Genre genre, final User user );

	PhotoListCriterias getForGenreBestForPeriod( final Genre genre, final User user );

	PhotoListCriterias getForGenreAbsolutelyBest( final Genre genre, final User user );

	PhotoListCriterias getForUser( final User user, final User accessor );

	PhotoListCriterias getForUserTopBest( final User user, final User accessor );

	PhotoListCriterias getForUserBestForPeriod( final User user, final User accessor );

	PhotoListCriterias getForUserAbsolutelyBest( final User user, final User accessor );

	PhotoListCriterias getForUserAndGenre( final User user, final Genre genre, final User accessor );

	PhotoListCriterias getForUserAndGenreTopBest( final User user, final Genre genre, final User accessor );

	PhotoListCriterias getForUserAndGenreBestForPeriod( final User user, final Genre genre, final User accessor );

	PhotoListCriterias getForUserAndGenreAbsolutelyBest( final User user, final Genre genre, final User accessor );

	PhotoListCriterias getForVotedPhotos( final PhotoVotingCategory votingCategory, final User accessor );

	PhotoListCriterias getForVotedPhotos( final User votedUser, final User accessor );

	PhotoListCriterias getForVotedPhotos( final PhotoVotingCategory votingCategory, final User votedUser, final User accessor );

	PhotoListCriterias getForVotingCategoryTopBest( final PhotoVotingCategory votingCategory, final User accessor );

	PhotoListCriterias getForVotingCategoryBestForPeriod( final PhotoVotingCategory votingCategory, final User accessor );

	PhotoListCriterias getForVotingCategoryAbsolutelyBest( final PhotoVotingCategory votingCategory, final User accessor );

	PhotoListCriterias getForPeriod( final Date dateFrom, final Date dateTo, final User accessor );

	PhotoListCriterias getForPeriodTopBest( final Date dateFrom, final Date dateTo, final User accessor );

	PhotoListCriterias getForPeriodBest( final Date dateFrom, final Date dateTo, final User accessor );

	PhotoListCriterias getForMembershipType( final UserMembershipType membershipType, final User accessor );

	PhotoListCriterias getForMembershipTypeTopBest( final UserMembershipType membershipType, final User accessor );

	PhotoListCriterias getForMembershipTypeBestForPeriod( final UserMembershipType membershipType, final User accessor );

	PhotoListCriterias getForMembershipTypeAbsolutelyBest( final UserMembershipType membershipType, final User accessor );

	PhotoListCriterias getUserCardUserPhotosBest( final User cardOwner, final User accessor );

	PhotoListCriterias getUserCardUserPhotosLast( final User user, final User accessor );

	PhotoListCriterias getUserCardLastVotedPhotos( final User user, final User accessor );
}
