package com.bordozer.jphoto.core.services.security;

import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.exceptions.AccessDeniedException;
import com.bordozer.jphoto.core.exceptions.NotLoggedUserException;
import com.bordozer.jphoto.core.exceptions.notFound.GenreNotFoundException;
import com.bordozer.jphoto.core.exceptions.notFound.PhotoNotFoundException;
import com.bordozer.jphoto.core.exceptions.notFound.UserNotFoundException;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.photo.UserRankInGenreVotingValidationResult;
import com.bordozer.jphoto.core.general.photo.ValidationResult;
import com.bordozer.jphoto.core.general.restriction.EntryRestriction;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.services.entry.AnonymousDaysService;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoCommentService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.dtos.AnonymousSettingsDTO;
import com.bordozer.jphoto.utils.NumberUtils;
import com.bordozer.jphoto.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private PhotoCommentService photoCommentService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private AnonymousDaysService anonymousDaysService;

    @Autowired
    private RestrictionService restrictionService;

    @Override
    public boolean userCanEditPhoto(final User user, final Photo photo) {
        return (configurationService.getBoolean(ConfigurationKey.ADMIN_CAN_EDIT_OTHER_PHOTOS) && isSuperAdminUser(user.getId())) || (UserUtils.isLoggedUser(user) && userOwnThePhoto(user, photo));
    }

    @Override
    public boolean userCanDeletePhoto(final User user, final Photo photo) {
        return (configurationService.getBoolean(ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS) && isSuperAdminUser(user.getId())) || (UserUtils.isLoggedUser(user) && userOwnThePhoto(user, photo));
    }

    @Override
    public boolean userCanVoteForPhoto(final User user, final Photo photo) {
        return UserUtils.isLoggedUser(user.getId()) && !userOwnThePhoto(user, photo);
    }

    @Override
    public boolean userCanSeeUserRankVoteHistory(final User user, final User userWhoIsSeeing) {
        return UserUtils.isLoggedUser(user) && (UserUtils.isUsersEqual(user, userWhoIsSeeing) || isSuperAdminUser(userWhoIsSeeing.getId()));
    }

    @Override
    public boolean userOwnThePhoto(final User user, final int photoId) {
        return UserUtils.isUserOwnThePhoto(user, photoService.load(photoId));
    }

    @Override
    public boolean userOwnThePhoto(final User user, final Photo photo) {
        return UserUtils.isUserOwnThePhoto(user, photo);
    }

    @Override
    public boolean userOwnThePhotoComment(final User user, final PhotoComment photoComment) {
        return UserUtils.isUsersEqual(photoComment.getCommentAuthor(), user);
    }

    @Override
    public boolean userCanEditPhotoComment(final User user, final PhotoComment photoComment) {

        if (photoComment.isCommentDeleted()) {
            return false;
        }

        if (!UserUtils.isLoggedUser(user)) {
            return false;
        }

        if (configurationService.getBoolean(ConfigurationKey.ADMIN_CAN_EDIT_PHOTO_COMMENTS) && isSuperAdminUser(user.getId())) {
            return true;
        }

        return userOwnThePhotoComment(user, photoComment);
    }

    @Override
    public boolean userCanDeletePhotoComment(final int userId, final int commentId) {

        // TODO: make test on this case -->
        if (!UserUtils.isLoggedUser(userId)) {
            return false;
        }
        // TODO: make test on this case <--

        final PhotoComment photoComment = photoCommentService.load(commentId);

        final User user = userService.load(userId);
        final User commentAuthor = photoComment.getCommentAuthor();
        final Photo photo = photoService.load(photoComment.getPhotoId());

        return isSuperAdminUser(userId) || UserUtils.isUserOwnThePhoto(user, photo) || UserUtils.isUsersEqual(userId, commentAuthor.getId());
    }

    @Override
    public boolean userCanDeletePhotoComment(final User user, final PhotoComment photoComment) {
        return userCanDeletePhotoComment(user.getId(), photoComment.getId());
    }

    @Override
    public boolean userCanEditUserData(final User user, final User dataOwnerUser) {
        return (configurationService.getBoolean(ConfigurationKey.ADMIN_CAN_EDIT_OTHER_USER_DATA) && isSuperAdminUser(user.getId())) || (UserUtils.isLoggedUser(dataOwnerUser) && UserUtils.isUsersEqual(user, dataOwnerUser));
    }

    @Override
    public void assertUserExists(final String _userId) {

        final int userId = NumberUtils.convertToInt(_userId);

        if (userId == 0) {
            throw new UserNotFoundException(_userId);
        }

        final User user = getUser(userId);

        if (user == null) {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public void assertPhotoExists(final String _photoId) {
        final int photoId = NumberUtils.convertToInt(_photoId);

        if (photoId == 0) {
            throw new PhotoNotFoundException(_photoId);
        }

        if (getPhoto(photoId) == null) {
            throw new PhotoNotFoundException(photoId);
        }
    }

    @Override
    public void assertPhotoFileExists(final Photo photo) {
        final File photoFile = photo.getPhotoImageFile();
        if (photoFile == null || !photoFile.isFile() || !photoFile.exists()) {
            throw new PhotoNotFoundException(photo.getId());
        }
    }

    @Override
    public void assertUserCanEditPhoto(final User user, final Photo photo) {
        final boolean userCanEditPhoto = userCanEditPhoto(user, photo);

        if (!userCanEditPhoto) {
            throw new AccessDeniedException();
        }
    }

    @Override
    public void assertUserEqualsToCurrentUser(final int userId) {
        final User user = getUser(userId);

        assertUserEqualsToCurrentUser(user);
    }

    @Override
    public void assertUserEqualsToCurrentUser(final User user) {
        if (!UserUtils.isTheUserThatWhoIsCurrentUser(user)) {
            throw new AccessDeniedException();
        }
    }

    @Override
    public void assertUserCanSeeUserRankVoteHistory(final User user, final User userWhoIsSeeing) {
        if (!userCanSeeUserRankVoteHistory(user, userWhoIsSeeing)) {
            throw new AccessDeniedException();
        }
    }

    @Override
    public void assertCurrentUserIsLogged(final String message) {
        if (!UserUtils.isCurrentUserLoggedUser()) {
            throw new NotLoggedUserException(message);
        }
    }

    @Override
    public void assertUserCanDeletePhoto(final User user, final Photo photo) {
        final boolean userCanDeletePhoto = userCanDeletePhoto(user, photo);

        if (!userCanDeletePhoto) {
            throw new AccessDeniedException();
        }
    }

    @Override
    public void assertUserCanDeletePhotoComment(final int userId, final int commentId) {
        if (!userCanDeletePhotoComment(userId, commentId)) {
            throw new AccessDeniedException();
        }
    }

    @Override
    public void assertUserCanEditUserData(final User user, final User dataOwnerUser) {
        if (!userCanEditUserData(user, dataOwnerUser)) {
            throw new AccessDeniedException();
        }
    }

    @Override
    public void assertGenreExists(final String _genreId) {
        final int genreId = NumberUtils.convertToInt(_genreId);

        if (genreId == 0) {
            throw new GenreNotFoundException(_genreId);
        }

        final Genre genre = genreService.load(genreId);

        if (genre == null) {
            throw new GenreNotFoundException(genreId);
        }
    }

    @Override
    public ValidationResult validateUserCanCommentPhoto(final User user, final Photo photo, final Date time, final Language language) {
        final ValidationResult allowanceValidationResult = new ValidationResult();

        if (photo.isArchived()) { // TODO: cover this by unit test
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: The photo had been uploaded more then $1 days ago and has been archived", language, configurationService.getString(ConfigurationKey.ARCHIVING_PHOTOS)));

            return allowanceValidationResult;
        }

        if (isSuperAdminUser(user.getId())) {
            return allowanceValidationResult;
        }

        if (!UserUtils.isLoggedUser(user)) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: You are not logged in", language));

            return allowanceValidationResult;
        }

        final boolean isUserInBlackListOfPhotoOwner = photo.getUserId() != user.getId() && favoritesService.isUserInBlackListOfUser(photo.getUserId(), user.getId());
        if (isUserInBlackListOfPhotoOwner) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: You are in the black list of photo's author", language));

            return allowanceValidationResult;
        }

        // TODO: -->
		/*final int photoQty = configurationService.getInt( ConfigurationKey.COMMENTS_USER_MUST_HAVE_N_APPROVED_PHOTOS_BEFORE_CAN_LEAVE_COMMENT );
		if ( photoQty > 0 ) {
			final int userApprovedPhotosQty = 5;
			if ( userApprovedPhotosQty < photoQty ) {
				allowanceValidation.failValidation( translatorService.translate( "ValidationResult: You do not have enough approved photos ( has to be $1 )", photoQty ) );

				return allowanceValidation;
			}
		}*/
        // TODO: <--

        if (user.getUserStatus() == UserStatus.CANDIDATE && !configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS)) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: Commenting is not allowed to candidates", language));

            return allowanceValidationResult;
        }

        final PhotoActionAllowance photoCommentsAllowance = photoService.getPhotoCommentAllowance(photo);

        if (photoCommentsAllowance == PhotoActionAllowance.ACTIONS_DENIED) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: The Author did not allow commenting", language));

            return allowanceValidationResult;
        }

        if (photoCommentsAllowance == PhotoActionAllowance.MEMBERS_ONLY && user.getUserStatus() == UserStatus.CANDIDATE) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: The author allowed commenting to members only", language));

            return allowanceValidationResult;
        }

        final EntryRestriction userCommentingRestrictionOn = restrictionService.getUserPhotoCommentingRestrictionOn(user.getId(), time);
        if (userCommentingRestrictionOn != null) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.failValidation(restrictionService.getUserRestrictionMessage(userCommentingRestrictionOn).build(language));

            return result;
        }

        final EntryRestriction photoCommentingRestrictionOn = restrictionService.getPhotoCommentingRestrictionOn(photo.getId(), time);
        if (photoCommentingRestrictionOn != null) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.failValidation(restrictionService.getPhotoRestrictionMessage(photoCommentingRestrictionOn).build(language));

            return result;
        }

        return allowanceValidationResult;
    }

    @Override
    public ValidationResult validateUserCanVoteForPhoto(final User user, final Photo photo, final Date time, final Language language) {
        final ValidationResult allowanceValidationResult = new ValidationResult();

        if (!UserUtils.isLoggedUser(user)) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: You are not logged in", language));

            return allowanceValidationResult;
        }

        final User photoAuthor = userService.load(photo.getUserId());

        if (UserUtils.isUsersEqual(user, photoAuthor)) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: This is your own photo", language));

            return allowanceValidationResult;
        }

        if (photo.isArchived()) { // TODO: cover this by unit test
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: The photo had been uploaded more then $1 days ago and has been archived", language, configurationService.getString(ConfigurationKey.ARCHIVING_PHOTOS)));

            return allowanceValidationResult;
        }

        final boolean isUserInBlackListOfPhotoOwner = favoritesService.isUserInBlackListOfUser(photoAuthor.getId(), user.getId());
        if (isUserInBlackListOfPhotoOwner) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: You are in the black list of photo's author", language));

            return allowanceValidationResult;
        }

        if (user.getUserStatus() == UserStatus.CANDIDATE && !configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS)) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: Voting is not allowed to candidates", language));

            return allowanceValidationResult;
        }

        final PhotoActionAllowance photoVotingAllowance = photoService.getPhotoVotingAllowance(photo);

        if (photoVotingAllowance == PhotoActionAllowance.ACTIONS_DENIED) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: The Author denied voting", language));

            return allowanceValidationResult;
        }

        if (photoVotingAllowance == PhotoActionAllowance.MEMBERS_ONLY && user.getUserStatus() == UserStatus.CANDIDATE) {
            allowanceValidationResult.failValidation(translatorService.translate("ValidationResult: The author allowed voting to members only", language));

            return allowanceValidationResult;
        }

        final int userRankInGenre = userRankService.getUserRankInGenre(user.getId(), photo.getGenreId());
        if (userRankInGenre < 0) {
            final Genre genre = genreService.load(photo.getGenreId());
            allowanceValidationResult.failValidation(
                    translatorService.translate("ValidationResult: You have negative rank ( $1 ) in category $2", language, String.valueOf(userRankInGenre)
                            , entityLinkUtilsService.getPhotosByGenreLink(genre, language)));

            return allowanceValidationResult;
        }

        final EntryRestriction restrictionOn = restrictionService.getUserPhotoAppraisalRestrictionOn(user.getId(), time);
        if (restrictionOn != null) {
            allowanceValidationResult.failValidation(restrictionService.getUserRestrictionMessage(restrictionOn).build(language));

            return allowanceValidationResult;
        }

        final EntryRestriction photoCommentingRestrictionOn = restrictionService.getPhotoAppraisalRestrictionOn(photo.getId(), time);
        if (photoCommentingRestrictionOn != null) {
            allowanceValidationResult.failValidation(restrictionService.getPhotoRestrictionMessage(photoCommentingRestrictionOn).build(language));

            return allowanceValidationResult;
        }

        return allowanceValidationResult;
    }

    @Override
    public UserRankInGenreVotingValidationResult getUserRankInGenreVotingValidationResult(final User user, final User voter, final Genre genre, final Date time, final Language language) {

        if (!UserUtils.isLoggedUser(voter)) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.setValidationPassed(false);
            result.setValidationMessage(translatorService.translate("ValidationResult: You are not logged in", language));
            result.setUiVotingIsInaccessible(true);

            return result;
        }

        if (UserUtils.isUsersEqual(user, voter)) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.setValidationPassed(false);
            result.setValidationMessage(translatorService.translate("ValidationResult: You can not vote for your own rank.", language));
            result.setUiVotingIsInaccessible(true);

            return result;
        }

        if (voter.getUserStatus() == UserStatus.CANDIDATE && !configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.setValidationPassed(false);
            result.setValidationMessage(translatorService.translate("ValidationResult: You are not the member yet. Voting for member ranks in photo categories is not allowed to candidates.", language));
            result.setUiVotingIsInaccessible(true);

            return result;
        }

        final int genreId = genre.getId();
        final int userId = user.getId();
        final int voterId = voter.getId();

        final int voterRankInGenre = userRankService.getUserRankInGenre(voterId, genreId);
        final String genreName = translatorService.translateGenre(genre, language);

        if (voterRankInGenre < 0) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.setValidationPassed(false);
            result.setValidationMessage(translatorService.translate("ValidationResult: You have an negative rank in category '$1'.", language, genreName));
            result.setUiVotingIsInaccessible(true);

            return result;
        }

        final int userRankInGenre = userRankService.getUserRankInGenre(userId, genreId);
        if (userRankService.isUserVotedLastTimeForThisRankInGenre(voterId, userId, genreId, userRankInGenre)) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.setValidationPassed(false);
            result.setValidationMessage(translatorService.translate("ValidationResult: You have already voted when member's rank is $1 in category '$2'", language, String.valueOf(userRankInGenre), genreName));
            result.setUiVotingIsInaccessible(false);

            return result;
        }

        final int minPhotosQtyForGenreRankVoting = configurationService.getInt(ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE);
        final int userPhotosInGenre = photoService.getPhotosCountByUserAndGenre(userId, genreId);
        if (userPhotosInGenre < minPhotosQtyForGenreRankVoting) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.setValidationPassed(false);
            result.setValidationMessage(translatorService.translate("ValidationResult: The member does not have enough photos in category $1 ( there are $2 photos but has to be at least $3 ones ).", language
                    , genreName, String.valueOf(userPhotosInGenre), String.valueOf(minPhotosQtyForGenreRankVoting)));
            result.setUiVotingIsInaccessible(true);

            return result;
        }

        if (favoritesService.isUserInBlackListOfUser(userId, voterId)) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.setValidationPassed(false);
            result.setValidationMessage(translatorService.translate("ValidationResult: You are in the black list of $1.", language, user.getNameEscaped()));
            result.setUiVotingIsInaccessible(true);

            return result;
        }

        final EntryRestriction restrictionOn = restrictionService.getUserRankVotingRestrictionOn(voter.getId(), time);
        if (restrictionOn != null) {
            final UserRankInGenreVotingValidationResult result = new UserRankInGenreVotingValidationResult();
            result.failValidation(restrictionService.getUserRestrictionMessage(restrictionOn).build(language));
            result.setUiVotingIsInaccessible(true);

            return result;
        }

        return new UserRankInGenreVotingValidationResult();
    }

    @Override
    public boolean isSuperAdminUser(final int userId) {

        final List<String> adminUserIds = getAdminUserIds();

        for (final String _adminUserId : adminUserIds) {
            final int adminUserId = NumberUtils.convertToInt(_adminUserId);
            if (adminUserId == 0) {
                continue;
            }

            if (adminUserId == userId) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isSuperAdminUser(final User user) {
        return isSuperAdminUser(user.getId());
    }

    @Override
    public List<User> getSuperAdminUsers() {
        final List<String> adminUserIds = getAdminUserIds();

        final List<User> result = newArrayList();
        for (final String _adminUserId : adminUserIds) {
            result.add(userService.load(NumberUtils.convertToInt(_adminUserId)));
        }
        return result;
    }

    @Override
    public void assertSuperAdminAccess(final User user) {
        if (!isSuperAdminUser(user.getId())) {
            throw new AccessDeniedException("This operation is allowed for com.bordozer.jphoto.admin only!");
        }
    }

    @Override
    public boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(final PhotoComment photoComment, final User accessor) {
        return isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(photoComment, accessor, dateUtilsService.getCurrentTime());
    }

    @Override
    public boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(final PhotoComment photoComment, final User accessor, final Date onTime) {
        final User commentAuthor = photoComment.getCommentAuthor();

        if (UserUtils.isUsersEqual(commentAuthor, accessor)) {
            return false;
        }

        final Photo photo = photoService.load(photoComment.getPhotoId());

        if (userOwnThePhoto(accessor, photo)) {
            return false;
        }

        return UserUtils.isUsersEqual(photo.getUserId(), commentAuthor.getId()) && isPhotoAuthorNameMustBeHidden(photo, accessor, onTime);
    }

    @Override
    public boolean isPhotoAuthorNameMustBeHidden(final Photo photo, final User accessor) {
        return isPhotoAuthorNameMustBeHidden(photo, accessor, dateUtilsService.getCurrentTime());
    }

    @Override
    public boolean isPhotoAuthorNameMustBeHidden(final Photo photo, final User accessor, final Date onTime) {

        if (isSuperAdminUser(accessor.getId())) {
            return false;
        }

        if (userOwnThePhoto(accessor, photo)) {
            return false;
        }

        return isPhotoWithingAnonymousPeriod(photo, onTime);
    }

    @Override
    public boolean isPhotoWithingAnonymousPeriod(final Photo photo) {
        return isPhotoWithingAnonymousPeriod(photo, dateUtilsService.getCurrentTime());
    }

    @Override
    public boolean isPhotoWithingAnonymousPeriod(final Photo photo, final Date onTime) {
        return photo.isAnonymousPosting() && onTime.getTime() < photoService.getPhotoAnonymousPeriodExpirationTime(photo).getTime();
    }

    @Override
    public boolean forceAnonymousPosting(final int userId, final int genreId, final Date time) {
        final int minRankToIgnoreAnonymousDay = configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_IGNORE_ANONYMOUS_GLOBAL_SETTINGS_IF_USER_RANK_IN_GENRE_MORE_THEN);
        final int userRankInGenre = userRankService.getUserRankInGenre(userId, genreId);

        final boolean isPostingDateAnonymousDay = anonymousDaysService.isDayAnonymous(time);
        return isPostingDateAnonymousDay && userRankInGenre < minRankToIgnoreAnonymousDay;
    }

    @Override
    public AnonymousSettingsDTO forceAnonymousPostingAjax(final int userId, final int genreId, final Language language) {
        final Date time = dateUtilsService.getCurrentTime();

        final boolean forceAnonymous = forceAnonymousPosting(userId, genreId, time);

        final boolean isDayAnonymous = anonymousDaysService.isDayAnonymous(time);
        final int minRankToIgnoreAnonymousDay = configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_IGNORE_ANONYMOUS_GLOBAL_SETTINGS_IF_USER_RANK_IN_GENRE_MORE_THEN);
        final int userRankInGenre = userRankService.getUserRankInGenre(userId, genreId);

        final Genre genre = genreService.load(genreId);

        final List<String> messages = newArrayList();

        if (isDayAnonymous) {
            //			messages.add( translatorService.translate( "Today is anonymous posting day" ) );
            final String genreName = translatorService.translateGenre(genre, language);
            if (userRankInGenre >= minRankToIgnoreAnonymousDay) {
                messages.add(translatorService.translate("But your rank in category '$1' is big enough to ignore global settings and decide if you want to upload a photo anonymously", language, genreName));
            } else {
                messages.add(translatorService.translate("Your photo will be posted anonymously", language));
                messages.add(translatorService.translate("You will be able to ignore anonymous days when your rank in category '$1' reached $2", language, genreName, String.valueOf(minRankToIgnoreAnonymousDay)));
            }
        } else {
            messages.add(translatorService.translate("Today is not anonymous posting day", language));
            messages.add(translatorService.translate("You decide if you want to upload a photo anonymously", language));
        }

        return new AnonymousSettingsDTO(forceAnonymous, messages);
    }

    private User getUser(final int userId) {
        return userService.load(userId);
    }

    private Photo getPhoto(final int photoId) {
        return photoService.load(photoId);
    }

    private List<String> getAdminUserIds() {
        return newArrayList("1"); // TODO: read superAdminUserIDs from property
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public void setPhotoService(final PhotoService photoService) {
        this.photoService = photoService;
    }

    public void setGenreService(final GenreService genreService) {
        this.genreService = genreService;
    }

    public void setUserRankService(final UserRankService userRankService) {
        this.userRankService = userRankService;
    }

    public void setFavoritesService(final FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setEntityLinkUtilsService(final EntityLinkUtilsService entityLinkUtilsService) {
        this.entityLinkUtilsService = entityLinkUtilsService;
    }

    public void setPhotoCommentService(final PhotoCommentService photoCommentService) {
        this.photoCommentService = photoCommentService;
    }

    public void setDateUtilsService(final DateUtilsService dateUtilsService) {
        this.dateUtilsService = dateUtilsService;
    }

    public void setTranslatorService(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public void setAnonymousDaysService(final AnonymousDaysService anonymousDaysService) {
        this.anonymousDaysService = anonymousDaysService;
    }

    public void setRestrictionService(final RestrictionService restrictionService) {
        this.restrictionService = restrictionService;
    }
}
