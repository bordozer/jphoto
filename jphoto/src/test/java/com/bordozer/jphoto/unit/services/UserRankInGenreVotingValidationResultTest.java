package services;

import com.bordozer.jphoto.core.enums.RestrictionType;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.UserRankInGenreVotingValidationResult;
import com.bordozer.jphoto.core.general.restriction.EntryRestriction;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.RestrictionService;
import com.bordozer.jphoto.core.services.security.SecurityServiceImpl;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class UserRankInGenreVotingValidationResultTest extends AbstractTestCase {

    public static final String VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE = "Validation is passed but should not be";
    public static final String VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Voting should not be accessible but it is";
    public static final String VALIDATION_MESSAGE_IS_WRONG = "Validation message is wrong";

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void notLoggedUser() {

        final User user = new User(11);
        final User voter = NOT_LOGGED_USER;

        final SecurityServiceImpl securityService = getSecurityService();
        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, getGenre(), dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertTrue(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(validationResult.getValidationMessage(), translated("ValidationResult: You are not logged in"));
    }

    @Test
    public void userCanNotVoteForHisOwnRank() {

        final User user = new User(11);
        final User voter = new User(11); // the same user ID

        final SecurityServiceImpl securityService = getSecurityService();
        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, getGenre(), dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertTrue(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(VALIDATION_MESSAGE_IS_WRONG, validationResult.getValidationMessage(), translated("ValidationResult: You can not vote for your own rank."));
    }

    @Test
    public void candidateIfVotingIsNotAllowedForOne() {

        final User user = new User(11);
        final User voter = new User(22);
        voter.setUserStatus(UserStatus.CANDIDATE);

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)).andReturn(false).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        final SecurityServiceImpl securityService = getSecurityService();
        securityService.setConfigurationService(configurationService);

        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, getGenre(), dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertTrue(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(VALIDATION_MESSAGE_IS_WRONG, validationResult.getValidationMessage(), translated("ValidationResult: You are not the member yet. Voting for member ranks in photo categories is not allowed to candidates."));
    }

    @Test
    public void userWithNegativeRankCanNotVote() {

        final User user = new User(11);
        final User voter = new User(22);
        voter.setUserStatus(UserStatus.CANDIDATE);

        final Genre genre = getGenre();

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)).andReturn(true).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        final UserRankService userRankService = EasyMock.createMock(UserRankService.class);
        EasyMock.expect(userRankService.getUserRankInGenre(voter.getId(), genre.getId())).andReturn(-1).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userRankService);

        final SecurityServiceImpl securityService = getSecurityService();
        securityService.setConfigurationService(configurationService);
        securityService.setUserRankService(userRankService);

        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, genre, dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertTrue(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(VALIDATION_MESSAGE_IS_WRONG, validationResult.getValidationMessage(), translated(String.format("ValidationResult: You have an negative rank in category '%s'.", translatorService.translateGenre(genre, Language.EN))));
    }

    @Test
    public void userWhoHasAlreadyVotedCanNotVote() {

        final User user = new User(11);
        final User voter = new User(22);
        voter.setUserStatus(UserStatus.CANDIDATE);

        final boolean isUserVotedLastTimeForThisRankInGenre = true;

        final Genre genre = getGenre();
        final int userRankInGenre = 1;

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)).andReturn(true).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        final UserRankService userRankService = EasyMock.createMock(UserRankService.class);
        EasyMock.expect(userRankService.getUserRankInGenre(voter.getId(), genre.getId())).andReturn(0).anyTimes();
        EasyMock.expect(userRankService.getUserRankInGenre(user.getId(), genre.getId())).andReturn(userRankInGenre).anyTimes();
        EasyMock.expect(userRankService.isUserVotedLastTimeForThisRankInGenre(voter.getId(), user.getId(), genre.getId(), userRankInGenre)).andReturn(isUserVotedLastTimeForThisRankInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userRankService);

        final SecurityServiceImpl securityService = getSecurityService();
        securityService.setConfigurationService(configurationService);
        securityService.setUserRankService(userRankService);

        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, genre, dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertFalse(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(VALIDATION_MESSAGE_IS_WRONG, validationResult.getValidationMessage()
                , translated(String.format("ValidationResult: You have already voted when member's rank is %s in category '%s'", String.valueOf(userRankInGenre), translatorService.translateGenre(genre, Language.EN))));
    }

    @Test
    public void itIsImpossibleToVoteIfUserDoesNotHaveEnoughPhotos() {

        final User user = new User(11);
        final User voter = new User(22);
        voter.setUserStatus(UserStatus.CANDIDATE);

        final boolean isUserVotedLastTimeForThisRankInGenre = false;
        final int userRankInGenre = 1;
        final int minPhotosQtyForGenreRankVoting = 3; // to vote must be at least 3 photos
        final int userPhotosInGenre = 2; // ... but user has 2 only

        final Genre genre = getGenre();

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)).andReturn(true).anyTimes();
        EasyMock.expect(configurationService.getInt(ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE)).andReturn(minPhotosQtyForGenreRankVoting).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        final UserRankService userRankService = EasyMock.createMock(UserRankService.class);
        EasyMock.expect(userRankService.getUserRankInGenre(voter.getId(), genre.getId())).andReturn(0).anyTimes();
        EasyMock.expect(userRankService.getUserRankInGenre(user.getId(), genre.getId())).andReturn(userRankInGenre).anyTimes();
        EasyMock.expect(userRankService.isUserVotedLastTimeForThisRankInGenre(voter.getId(), user.getId(), genre.getId(), userRankInGenre)).andReturn(isUserVotedLastTimeForThisRankInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userRankService);

        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.getPhotosCountByUserAndGenre(user.getId(), genre.getId())).andReturn(userPhotosInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        final SecurityServiceImpl securityService = getSecurityService();
        securityService.setConfigurationService(configurationService);
        securityService.setUserRankService(userRankService);
        securityService.setPhotoService(photoService);

        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, genre, dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertTrue(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(VALIDATION_MESSAGE_IS_WRONG, validationResult.getValidationMessage()
                , translated(String.format("ValidationResult: The member does not have enough photos in category %s ( there are %s photos but has to be at least %s ones )."
                        , translatorService.translateGenre(genre, Language.EN), String.valueOf(userPhotosInGenre), String.valueOf(minPhotosQtyForGenreRankVoting)))
        );
    }

    @Test
    public void userWhoIsInBlackListCanNotVote() {
        final User user = new User(11);
        final User voter = new User(22);
        voter.setUserStatus(UserStatus.CANDIDATE);

        final boolean isUserVotedLastTimeForThisRankInGenre = false;
        final int userRankInGenre = 1;
        final int minPhotosQtyForGenreRankVoting = 3;
        final int userPhotosInGenre = 5;
        final boolean isUserInBlackListOfUser = true; // Black list

        final Genre genre = getGenre();

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)).andReturn(true).anyTimes();
        EasyMock.expect(configurationService.getInt(ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE)).andReturn(minPhotosQtyForGenreRankVoting).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        final UserRankService userRankService = EasyMock.createMock(UserRankService.class);
        EasyMock.expect(userRankService.getUserRankInGenre(voter.getId(), genre.getId())).andReturn(0).anyTimes();
        EasyMock.expect(userRankService.getUserRankInGenre(user.getId(), genre.getId())).andReturn(userRankInGenre).anyTimes();
        EasyMock.expect(userRankService.isUserVotedLastTimeForThisRankInGenre(voter.getId(), user.getId(), genre.getId(), userRankInGenre)).andReturn(isUserVotedLastTimeForThisRankInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userRankService);

        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.getPhotosCountByUserAndGenre(user.getId(), genre.getId())).andReturn(userPhotosInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        final FavoritesService favoritesService = EasyMock.createMock(FavoritesService.class);
        EasyMock.expect(favoritesService.isUserInBlackListOfUser(user.getId(), voter.getId())).andReturn(isUserInBlackListOfUser).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(favoritesService);

        final SecurityServiceImpl securityService = getSecurityService();
        securityService.setConfigurationService(configurationService);
        securityService.setUserRankService(userRankService);
        securityService.setFavoritesService(favoritesService);
        securityService.setPhotoService(photoService);

        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, genre, dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertTrue(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(VALIDATION_MESSAGE_IS_WRONG, validationResult.getValidationMessage(), translated(String.format("ValidationResult: You are in the black list of %s.", user.getNameEscaped())));
    }

    @Test
    public void userVotingIsRestricted() {
        final User user = new User(11);
        final User voter = new User(22);
        voter.setUserStatus(UserStatus.CANDIDATE);

        final boolean isUserVotedLastTimeForThisRankInGenre = false;
        final int userRankInGenre = 1;
        final int minPhotosQtyForGenreRankVoting = 3;
        final int userPhotosInGenre = 5;
        final boolean isUserInBlackListOfUser = false;

        final Genre genre = getGenre();

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)).andReturn(true).anyTimes();
        EasyMock.expect(configurationService.getInt(ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE)).andReturn(minPhotosQtyForGenreRankVoting).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        final UserRankService userRankService = EasyMock.createMock(UserRankService.class);
        EasyMock.expect(userRankService.getUserRankInGenre(voter.getId(), genre.getId())).andReturn(0).anyTimes();
        EasyMock.expect(userRankService.getUserRankInGenre(user.getId(), genre.getId())).andReturn(userRankInGenre).anyTimes();
        EasyMock.expect(userRankService.isUserVotedLastTimeForThisRankInGenre(voter.getId(), user.getId(), genre.getId(), userRankInGenre)).andReturn(isUserVotedLastTimeForThisRankInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userRankService);

        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.getPhotosCountByUserAndGenre(user.getId(), genre.getId())).andReturn(userPhotosInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        final FavoritesService favoritesService = EasyMock.createMock(FavoritesService.class);
        EasyMock.expect(favoritesService.isUserInBlackListOfUser(user.getId(), voter.getId())).andReturn(isUserInBlackListOfUser).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(favoritesService);

        final RestrictionService restrictionService = EasyMock.createMock(RestrictionService.class);
        final EntryRestriction restriction = new EntryRestriction<User>(user, RestrictionType.USER_VOTING_FOR_RANK_IN_GENRE);
        EasyMock.expect(restrictionService.getUserRankVotingRestrictionOn(EasyMock.anyInt(), EasyMock.<Date>anyObject())).andReturn(restriction).anyTimes();
        EasyMock.expect(restrictionService.getUserRestrictionMessage(restriction)).andReturn(new TranslatableMessage("Restriction message", getServices())).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(restrictionService);


        final SecurityServiceImpl securityService = getSecurityService();
        securityService.setConfigurationService(configurationService);
        securityService.setUserRankService(userRankService);
        securityService.setFavoritesService(favoritesService);
        securityService.setPhotoService(photoService);
        securityService.setRestrictionService(restrictionService);

        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, genre, dateUtilsService.getCurrentTime(), Language.EN);

        assertFalse(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertTrue(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertEquals(VALIDATION_MESSAGE_IS_WRONG, validationResult.getValidationMessage(), "Restriction message");
    }

    @Test
    public void userCanVote() {
        final User user = new User(11);
        final User voter = new User(22);
        voter.setUserStatus(UserStatus.CANDIDATE);

        final boolean isUserVotedLastTimeForThisRankInGenre = false;
        final int userRankInGenre = 1;
        final int minPhotosQtyForGenreRankVoting = 3;
        final int userPhotosInGenre = 5;
        final boolean isUserInBlackListOfUser = false;

        final Genre genre = getGenre();

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_USER_RANKS_IN_GENRE)).andReturn(true).anyTimes();
        EasyMock.expect(configurationService.getInt(ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE)).andReturn(minPhotosQtyForGenreRankVoting).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        final UserRankService userRankService = EasyMock.createMock(UserRankService.class);
        EasyMock.expect(userRankService.getUserRankInGenre(voter.getId(), genre.getId())).andReturn(0).anyTimes();
        EasyMock.expect(userRankService.getUserRankInGenre(user.getId(), genre.getId())).andReturn(userRankInGenre).anyTimes();
        EasyMock.expect(userRankService.isUserVotedLastTimeForThisRankInGenre(voter.getId(), user.getId(), genre.getId(), userRankInGenre)).andReturn(isUserVotedLastTimeForThisRankInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userRankService);

        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.getPhotosCountByUserAndGenre(user.getId(), genre.getId())).andReturn(userPhotosInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        final FavoritesService favoritesService = EasyMock.createMock(FavoritesService.class);
        EasyMock.expect(favoritesService.isUserInBlackListOfUser(user.getId(), voter.getId())).andReturn(isUserInBlackListOfUser).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(favoritesService);

        final RestrictionService restrictionService = EasyMock.createMock(RestrictionService.class);
        EasyMock.expect(restrictionService.getUserRankVotingRestrictionOn(EasyMock.anyInt(), EasyMock.<Date>anyObject())).andReturn(null).anyTimes();
        //		final EntryRestriction restriction = new EntryRestriction<User>( user, RestrictionType.USER_VOTING_FOR_RANK_IN_GENRE );
        //		EasyMock.expect( restrictionService.getUserRankVotingRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( restriction ).anyTimes();
        //		EasyMock.expect( restrictionService.getUserRestrictionMessage( restriction ) ).andReturn( new TranslatableMessage( "A message", getServices() ) ).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(restrictionService);


        final SecurityServiceImpl securityService = getSecurityService();
        securityService.setConfigurationService(configurationService);
        securityService.setUserRankService(userRankService);
        securityService.setFavoritesService(favoritesService);
        securityService.setPhotoService(photoService);
        securityService.setRestrictionService(restrictionService);

        final UserRankInGenreVotingValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult(user, voter, genre, dateUtilsService.getCurrentTime(), Language.EN);

        assertTrue(VALIDATION_IS_PASSED_BUT_SHOULD_NOT_BE, validationResult.isValidationPassed());
        assertFalse(VOTING_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, validationResult.isUiVotingIsInaccessible());
        assertTrue(VALIDATION_MESSAGE_IS_WRONG, StringUtils.isEmpty(validationResult.getValidationMessage()));
    }

    private Genre getGenre() {
        final Genre genre = new Genre();
        genre.setId(4);
        genre.setName("Photo category name");

        return genre;
    }

    private SecurityServiceImpl getSecurityService() {
        final SecurityServiceImpl securityService = new SecurityServiceImpl();

        securityService.setTranslatorService(translatorService);

        return securityService;
    }
}
