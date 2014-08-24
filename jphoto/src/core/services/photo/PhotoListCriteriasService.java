package core.services.photo;

import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserMembershipType;

import java.util.Date;

public interface PhotoListCriteriasService {

	String getLinkToFullListText();

	PhotoListCriterias getForGenreTopBest( final Genre genre, final User user );

	PhotoListCriterias getForGenreBestForPeriod( final Genre genre, final User user );

	PhotoListCriterias getForUserTopBest( final User user, final User accessor );

	PhotoListCriterias getForUserAbsolutelyBest( final User user, final User accessor );

	PhotoListCriterias getForUserAndGenreTopBest( final User user, final Genre genre, final User accessor );

	PhotoListCriterias getForUserAndGenreAbsolutelyBest( final User user, final Genre genre, final User accessor );

	PhotoListCriterias getForAppraisedByUserPhotos( final User votedUser, final User accessor );

	PhotoListCriterias getForAppraisedByUserPhotos( final PhotoVotingCategory votingCategory, final User votedUser, final User accessor );

	PhotoListCriterias getForPeriod( final Date dateFrom, final Date dateTo, final User accessor );

	PhotoListCriterias getForPeriodBest( final Date dateFrom, final Date dateTo, final User accessor );

	PhotoListCriterias getForMembershipType( final UserMembershipType membershipType, final User accessor );

	PhotoListCriterias getForMembershipTypeTopBest( final UserMembershipType membershipType, final User accessor );

	PhotoListCriterias getForMembershipTypeBestForPeriod( final UserMembershipType membershipType, final User accessor );

	PhotoListCriterias getUserCardUserPhotosBest( final User cardOwner, final User accessor );

	PhotoListCriterias getUserCardUserPhotosLast( final User user, final User accessor );

	PhotoListCriterias getUserCardLastAppraisedPhotos( final User user, final User accessor );
}
