package services;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.dao.UserRankDao;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.user.UserRankServiceImpl;
import com.bordozer.jphoto.ui.userRankIcons.AbstractUserRankIcon;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertTrue;

public class UserRankIconTest extends AbstractTestCase {

    public static final int MIN_PHOTOS_IN_GENRE_FOR_VOTING = 3;
    public static final int RANK_VOTING_RANK_QTY_TO_COLLAPSE = 10;

    private static final String WRONG_ICONS_QTY = "Wrong icons qty";
    private static final String WRONG_ICON = "Wrong icon";

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void notEnoughPhotosAndZeroRankTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = 0;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING - 1; //less then then MIN_PHOTOS_IN_GENRE_FOR_VOTING;

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 1);
        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NOT_ENOUGH_PHOTOS));
    }

    @Test
    public void notEnoughPhotosAndRankIsOneTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = 1;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING - 1; //less then then MIN_PHOTOS_IN_GENRE_FOR_VOTING

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 1);
        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
    }

    @Test
    public void notEnoughPhotosAndRankMoreThenOneAndLessThenCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = 7;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING - 1; //less then then MIN_PHOTOS_IN_GENRE_FOR_VOTING

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 7);
        for (final AbstractUserRankIcon icon : icons) {
            assertTrue(WRONG_ICON, icon.getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        }
    }

    @Test
    public void notEnoughPhotosAndRankMoreThenCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = 13;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING - 1; //less then then MIN_PHOTOS_IN_GENRE_FOR_VOTING

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 4);

        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(1).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(2).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(3).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
    }

    @Test
    public void enoughPhotosAndZeroRankTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = 0;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING; // equals or more then MIN_PHOTOS_IN_GENRE_FOR_VOTING;

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 1);
        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_ZERO));
    }

    @Test
    public void enoughPhotosAndRankMoreThenOneAndLessThenCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = 5;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING; // equals or more then MIN_PHOTOS_IN_GENRE_FOR_VOTING;

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 5);
        for (final AbstractUserRankIcon icon : icons) {
            assertTrue(WRONG_ICON, icon.getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        }
    }

    @Test
    public void enoughPhotosAndRankMoreThenCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = 15;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING + 1; //less then then MIN_PHOTOS_IN_GENRE_FOR_VOTING

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 6);

        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(1).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(2).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(3).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(4).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(5).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
    }

    @Test
    public void enoughPhotosAndRankMoreThenDoubledCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = RANK_VOTING_RANK_QTY_TO_COLLAPSE * 2 + 3;
        final int userPhotosQtyInGenre = MIN_PHOTOS_IN_GENRE_FOR_VOTING + 1; //less then then MIN_PHOTOS_IN_GENRE_FOR_VOTING

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 5);

        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(1).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(2).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(3).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
        assertTrue(WRONG_ICON, icons.get(4).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON));
    }

    @Test
    public void negativeFirstRankTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = -1;
        final int userPhotosQtyInGenre = 0; //does not matter

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 1);

        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE));
    }

    @Test
    public void negativeRankMoreThenOneButLessThenCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = -3;
        final int userPhotosQtyInGenre = 0; //does not matter

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 3);

        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE));
        assertTrue(WRONG_ICON, icons.get(1).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE));
        assertTrue(WRONG_ICON, icons.get(2).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE));
    }

    @Test
    public void negativeRankMoreThenCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = -12;
        final int userPhotosQtyInGenre = 0; //does not matter

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 3);

        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(1).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE));
        assertTrue(WRONG_ICON, icons.get(2).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE));
    }

    @Test
    public void negativeRankMoreThenTrippleCollapsedValueTest() {

        final User user = new User(33);

        final Genre genre = new Genre();
        genre.setId(777);

        final int rankInGenre = -1 * RANK_VOTING_RANK_QTY_TO_COLLAPSE * 3 - 1;
        final int userPhotosQtyInGenre = 0; //does not matter

        final UserRankServiceImpl userRankService = getUserRankService(user, genre, rankInGenre, userPhotosQtyInGenre);

        final List<AbstractUserRankIcon> icons = userRankService.getUserRankIconContainer(user, genre).getRankIcons();

        assertTrue(WRONG_ICONS_QTY, icons.size() == 4);

        assertTrue(WRONG_ICON, icons.get(0).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(1).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(2).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE_COLLAPSED));
        assertTrue(WRONG_ICON, icons.get(3).getIcon().equals(AbstractUserRankIcon.USER_RANK_ICON_NEGATIVE));
    }

    private UserRankServiceImpl getUserRankService(final User user, final Genre genre, final int currentRankInGenre, final int userPhotosQtyInGenre) {
        final UserRankServiceImpl userRankService = new UserRankServiceImpl();

        userRankService.setTranslatorService(translatorService);
        userRankService.setUserRankDao(getUserRankDao(user, genre, currentRankInGenre));
        userRankService.setConfigurationService(getConfigurationService());
        userRankService.setPhotoService(getPhotoService(genre, user, userPhotosQtyInGenre));

        return userRankService;
    }

    private UserRankDao getUserRankDao(final User user, final Genre genre, final int currentRankInGenre) {
        final UserRankDao userRankDao = EasyMock.createMock(UserRankDao.class);
        EasyMock.expect(userRankDao.getUserRankInGenre(user.getId(), genre.getId())).andReturn(currentRankInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userRankDao);

        return userRankDao;
    }

    private ConfigurationService getConfigurationService() {
        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);
        EasyMock.expect(configurationService.getInt(ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE)).andReturn(MIN_PHOTOS_IN_GENRE_FOR_VOTING).anyTimes();
        EasyMock.expect(configurationService.getInt(ConfigurationKey.RANK_VOTING_RANK_QTY_TO_COLLAPSE)).andReturn(RANK_VOTING_RANK_QTY_TO_COLLAPSE).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        return configurationService;
    }

    private PhotoService getPhotoService(final Genre genre, final User user, final int userPhotosQtyInGenre) {
        final PhotoService photoService = EasyMock.createMock(PhotoService.class);
        EasyMock.expect(photoService.getPhotosCountByUserAndGenre(user.getId(), genre.getId())).andReturn(userPhotosQtyInGenre).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        return photoService;
    }
}
