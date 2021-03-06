package photo.list.service;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.enums.UserTeamMemberType;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.data.TimeRange;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenu;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenuContainer;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.general.photoTeam.PhotoTeam;
import com.bordozer.jphoto.core.general.photoTeam.PhotoTeamMember;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.entry.GroupOperationService;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.photo.list.PhotoListFactoryServiceImpl;
import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoListFactory;
import com.bordozer.jphoto.core.services.photo.list.filtering.BestFilteringStrategy;
import com.bordozer.jphoto.core.services.photo.list.filtering.GalleryFilteringStrategy;
import com.bordozer.jphoto.core.services.photo.list.filtering.HideAnonymousPhotosFilteringStrategy;
import com.bordozer.jphoto.core.services.photo.list.filtering.TopBestFilteringStrategy;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import mocks.PhotoVotingCategoryMock;
import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class PhotoListFactoryServiceTest extends AbstractTestCase {

    private static final int DAYS = 2;
    private static final int MIN_MARKS_FOR_VERY_BEST = 40;

    private static final String A_GROUP_MENU_CAN_NOT_BE_FOUND = "A group operation menu is not provided by photo list";

    private static final ArrayList<PhotoGroupOperationMenu> GROUP_OPERATION_MENUS_USER = newArrayList(
            PhotoGroupOperationMenu.ARRANGE_PHOTO_ALBUMS
            , PhotoGroupOperationMenu.ARRANGE_TEAM_MEMBERS
            , PhotoGroupOperationMenu.SEPARATOR_MENU
            , PhotoGroupOperationMenu.ARRANGE_NUDE_CONTENT_MENU
            , PhotoGroupOperationMenu.MOVE_TO_GENRE_MENU
            , PhotoGroupOperationMenu.SEPARATOR_MENU
            , PhotoGroupOperationMenu.DELETE_PHOTOS_MENU
    );

    private static final ArrayList<PhotoGroupOperationMenu> GROUP_OPERATION_MENUS_EMPTY = newArrayList();   // is returned if list should not have group menus
    private static final ArrayList<PhotoGroupOperationMenu> GROUP_OPERATION_MENUS_DEFAULT = newArrayList(); // is returned if list' group menus are defined by GroupOperationService.getPhotoListPhotoGroupOperationMenuContainer()

    TestData testData;

    @Before
    public void setup() {
        super.setup();

        testData = new TestData();
    }

    @Test
    public void galleryTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).gallery(5, 24, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos ORDER BY photos.uploadTime DESC LIMIT 24 OFFSET 96;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: All photos.<br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());


        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForGenreTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForGenre(testData.genre, 5, 16, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos WHERE ( photos.genreId = '222' ) ORDER BY photos.uploadTime DESC LIMIT 16 OFFSET 64;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a>", factory.getTitle().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForUserTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForUser(testData.user, 3, 36, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '112' ) ORDER BY photos.uploadTime DESC LIMIT 36 OFFSET 72;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: All photos of <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a><br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());


        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForUserOwnGroupMenusTest() {
        assertGroupOperationMenusForUser(getPhotoListFactoryService(testData).galleryForUser(testData.accessor, 3, 36, testData.accessor));
    }

    @Test
    public void galleryForUserAndGenreTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForUserAndGenre(testData.user, testData.genre, 3, 36, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos WHERE ( ( photos.userId = '112' ) AND photos.genreId = '222' ) ORDER BY photos.uploadTime DESC LIMIT 36 OFFSET 72;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Accessor: user card link title\">Accessor</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a>", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Photos by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Accessor: user card link title\">Accessor</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a><br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForUserAndGenreOwnGroupMenusTest() {
        assertGroupOperationMenusForUser(getPhotoListFactoryService(testData).galleryForUserAndGenre(testData.accessor, testData.genre, 3, 36, testData.accessor));
    }

    @Test
    public void userCardPhotosLastTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).userCardPhotosLast(testData.user, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '112' ) ORDER BY photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User card User card owner: the latest photos", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom tex: User card User card owner: the latest photos", factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/photos/members/112/", factory.getLinkToFullList());


        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void userBookmarkedPhotosTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).userBookmarkedPhotos(testData.user, FavoriteEntryType.FAVORITE_PHOTOS, 1, 20, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN favorites ON ( photos.id = favorites.favoriteEntryId ) WHERE ( favorites.userId = '112' AND favorites.entryType = '2' ) ORDER BY favorites.created DESC LIMIT 20;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: bookmarked photos FavoriteEntryType: Favorite photos", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: bookmarked photos FavoriteEntryType: Favorite photos<br />Photo list bottom text: Sorted by adding to bookmark time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());


        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void userTeamMemberPhotosTest() {
        final UserTeamMember teamMember = new UserTeamMember();
        teamMember.setId(987);
        teamMember.setTeamMemberType(UserTeamMemberType.MODEL);
        teamMember.setName("Team model");
        teamMember.setUser(testData.user);

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).userTeamMemberPhotos(testData.user, teamMember, 2, 28, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos LEFT OUTER JOIN photoTeam ON ( photos.id = photoTeam.photoId ) WHERE ( ( photos.userId = '112' ) AND photoTeam.userTeamMemberId = '987' ) ORDER BY photos.uploadTime DESC LIMIT 28 OFFSET 28;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: all photos of <a href=\"http://127.0.0.1:8085/worker/members/112/team/987/\" title=\"EntityLinkUtilsService: User Team member card link title: Team model ( UserTeamMemberType: Model )\">Team model</a> ( UserTeamMemberType: Model )", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: all team member <a href=\"http://127.0.0.1:8085/worker/members/112/team/987/\" title=\"EntityLinkUtilsService: User Team member card link title: Team model ( UserTeamMemberType: Model )\">Team model</a> photos ( member type is UserTeamMemberType: Model )<br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());


        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void userTeamMemberPhotosOwnGroupMenusTest() {
        final UserTeamMember teamMember = new UserTeamMember();
        teamMember.setId(987);
        teamMember.setTeamMemberType(UserTeamMemberType.MODEL);
        teamMember.setName("Team model");
        teamMember.setUser(testData.user);

        assertGroupOperationMenusForUser(getPhotoListFactoryService(testData).userTeamMemberPhotos(testData.accessor, teamMember, 3, 36, testData.accessor));
    }

    @Test
    public void userTeamMemberPhotosLastTest() {
        final UserTeamMember teamMember = new UserTeamMember();
        teamMember.setId(987);
        teamMember.setTeamMemberType(UserTeamMemberType.MODEL);
        teamMember.setName("Team model");
        teamMember.setUser(testData.user);

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).userTeamMemberPhotosLast(testData.user, teamMember, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos LEFT OUTER JOIN photoTeam ON ( photos.id = photoTeam.photoId ) WHERE ( ( photos.userId = '112' ) AND photoTeam.userTeamMemberId = '987' ) ORDER BY photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: last photos with team member <a href=\"http://127.0.0.1:8085/worker/members/112/team/987/\" title=\"EntityLinkUtilsService: User Team member card link title: Team model ( UserTeamMemberType: Model )\">Team model</a> ( UserTeamMemberType: Model )", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: the latest team member <a href=\"http://127.0.0.1:8085/worker/members/112/team/987/\" title=\"EntityLinkUtilsService: User Team member card link title: Team model ( UserTeamMemberType: Model )\">Team model</a> photos ( member type is UserTeamMemberType: Model )", factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/members/112/team/987/", factory.getLinkToFullList());


        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void galleryTopBestTest() {
        final DateData dateData = new DateData();

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryTopBest(testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof TopBestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '40' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 4;", dateData.from1, dateData.to1), factory.getSelectIdsQuery().build());
        assertEquals(String.format("Photo list title: Photo gallery top best for last %s days", DAYS), factory.getTitle().build(Language.EN));
        assertEquals(String.format("Photo list bottom text: Top best photos that got at least %s marks in period %s - %s<br />Photo list bottom text: Sorted by sum marks DESC", MIN_MARKS_FOR_VERY_BEST, dateData.from2, dateData.to2), factory.getCriteriaDescription().build(Language.EN));
        assertEquals(String.format("http://127.0.0.1:8085/worker/photos/from/%s/to/%s/best/", dateData.from2, dateData.to2), factory.getLinkToFullList());


        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void galleryLastPopularTest() {
        final DateData dateData = new DateData();

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryLastPopular(2, 12, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof BestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoPreview ON ( photos.id = photoPreview.photoId ) WHERE ( ( photoPreview.previewTime >= '%s' ) AND photoPreview.previewTime <= '%s' ) GROUP BY photos.id ORDER BY COUNT( photoPreview.id ) DESC LIMIT 12 OFFSET 12;", dateData.from1, dateData.to1), factory.getSelectIdsQuery().build());
        assertEquals(String.format("Photo list title: Most popular fot last %d days photos", DAYS), factory.getTitle().build(Language.EN));
        assertEquals(String.format("Photo list bottom text: The most popular photos for period %s - %s.<br />Photo list bottom text: Sorted by previews count DESC", dateData.from2, dateData.to2), factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());


        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForGenreTopBestTest() {
        final DateData dateData = new DateData();

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForGenreTopBest(testData.genre, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof TopBestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) AND photos.genreId = '222' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 4;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST), factory.getSelectIdsQuery().build());
        assertEquals(String.format("Photo list title: Photo gallery by genre Translated entry top best for last %d days", DAYS), factory.getTitle().build(Language.EN));
        assertEquals(String.format("Photo list bottom text: Top best photos by genre Translated entry that got at least %d marks in period %s - %s<br />Photo list bottom text: Sorted by sum marks DESC", MIN_MARKS_FOR_VERY_BEST, dateData.from2, dateData.to2), factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/photos/genres/222/best/", factory.getLinkToFullList());

        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void galleryAbsolutelyBestTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryAbsolutelyBest(2, 12, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof BestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 12 OFFSET 12;", MIN_MARKS_FOR_VERY_BEST), factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery absolutely best", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Photo gallery absolutely best which got at least 40 marks.<br />Photo list bottom text: Sorted by total marks.", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForGenreBestTest() {
        final DateData dateData = new DateData();

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForGenreBest(testData.genre, 2, 12, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof BestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) AND photos.genreId = '222' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 12 OFFSET 12;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST), factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> best for 2 days", factory.getTitle().build(Language.EN));
        assertEquals(String.format("Photo list bottom text: The best photos from category Translated entry which got at least %d marks in period %s - %s<br />Photo list bottom text: Sorted by sum marks DESC", MIN_MARKS_FOR_VERY_BEST, dateData.from2, dateData.to2), factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForUserTopBestTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForUserTopBest(testData.user, 5, 20, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photos.userId = '112' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> top best", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Top best photos by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> which got at least 1 marks<br />Photo list bottom text: Sorted by total marks.", factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/photos/members/112/best/", factory.getLinkToFullList());

        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void userCardPhotosBestTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).userCardPhotosBest(testData.user, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photos.userId = '112' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User card User card owner: the best photos", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom tex: User card User card owner: the best photos", factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/photos/members/112/best/", factory.getLinkToFullList());

        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void userCardPhotosBestOwnGroupMenusTest() {
        assertGroupOperationMenusEmpty(getPhotoListFactoryService(testData).userCardPhotosBest(testData.accessor, testData.accessor));
    }

    @Test
    public void galleryForUserBestTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForUserBest(testData.user, 1, 36, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photos.userId = '112' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 36;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> best", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: The best photos of user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> best which got at least 1 marks<br />Photo list bottom text: Sorted by total marks.", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForUserBestOwnGroupMenusTest() {
        assertGroupOperationMenusForUser(getPhotoListFactoryService(testData).galleryForUserBest(testData.accessor, 1, 36, testData.accessor));
    }

    @Test
    public void galleryForUserAndGenreTopBestTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForUserAndGenreTopBest(testData.user, testData.genre, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photos.userId = '112' ) AND photos.genreId = '222' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> top best", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Top best photos by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Accessor: user card link title\">Accessor</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> which got at least 1 marks<br />Photo list bottom text: Sorted by total marks.", factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/photos/members/112/genre/222/best/", factory.getLinkToFullList());

        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void galleryForUserAndGenreBestTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryForUserAndGenreBest(testData.user, testData.genre, 5, 20, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photos.userId = '112' ) AND photos.genreId = '222' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '1' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 20 OFFSET 80;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photo gallery by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> best", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: The best photos by user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Accessor: user card link title\">Accessor</a> and genre <a class='photo-category-link' href=\"http://127.0.0.1:8085/worker/photos/genres/222/\" title=\"Breadcrumbs: All photos in category 'Translated entry'\">Translated entry</a> which got at least 1 marks<br />Photo list bottom text: Sorted by total marks.", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryForUserAndGenreBestOwnGroupMenusTest() {
        assertGroupOperationMenusForUser(getPhotoListFactoryService(testData).galleryForUserAndGenreBest(testData.accessor, testData.genre, 5, 20, testData.accessor));
    }

    @Test
    public void userCardPhotosLastAppraisedTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).userCardPhotosLastAppraised(testData.user, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photoVoting.userId = '112' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC LIMIT 4;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User card User card owner: last appraised photos", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom tex: User card User card owner: last appraised photos", factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/photos/members/112/category/", factory.getLinkToFullList());

        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void appraisedByUserPhotosTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).appraisedByUserPhotos(testData.user, 10, 24, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photoVoting.userId = '112' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photos which the user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> appraised", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Photos which the user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> appraised<br />Photo list bottom text: Sorted by voting time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void appraisedByUserPhotosByVotingCategoryTest() {
        final PhotoVotingCategory votingCategory = new PhotoVotingCategoryMock(543);
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).appraisedByUserPhotos(testData.user, votingCategory, 10, 24, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.userId = '112' ) AND photoVoting.votingCategoryId = '543' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photos which the user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> appraised as Translated entry", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Photos which the user <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a> appraised as Translated entry<br />Photo list bottom text: Sorted by voting time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryUploadedInDateRangeTest() {
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryUploadedInDateRange(dateUtilsService.parseDateTime("2014-08-10 12:15:48"), dateUtilsService.parseDateTime("2014-08-14 03:42:15"), 10, 24, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos WHERE ( ( photos.uploadTime >= '2014-08-10 00:00:00' ) AND photos.uploadTime <= '2014-08-14 23:59:59' ) ORDER BY photos.uploadTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: Photos uploaded between 2014-08-10 and 2014-08-14", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Photos uploaded between 2014-08-10 and 2014-08-14<br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryUploadedInDateRangeBestTest() {
        final DateData dateData = new DateData();

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryUploadedInDateRangeBest(dateData.timeFrom, dateData.timeTo, 10, 24, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof BestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 24 OFFSET 216;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST), factory.getSelectIdsQuery().build());
        assertEquals(String.format("Photo list title: The best photos for period %s - %s", dateData.from2, dateData.to2), factory.getTitle().build(Language.EN));
        assertEquals(String.format("Photo list bottom text: The best photos which got at least %d marks in period %s - %s<br />Photo list bottom text: Sorted by sum marks DESC", MIN_MARKS_FOR_VERY_BEST, dateData.from2, dateData.to2), factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryByUserMembershipTypeTest() {

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryByUserMembershipType(UserMembershipType.MODEL, 10, 24, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof GalleryFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) WHERE ( users.membershipType = '2' ) ORDER BY photos.uploadTime DESC LIMIT 24 OFFSET 216;", factory.getSelectIdsQuery().build());
        assertEquals("Main menu: photos: UserMembershipType: model", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: Photos of users with membership type UserMembershipType: model plural<br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void galleryByUserMembershipTypeTopBestTest() {
        final DateData dateData = new DateData();
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryByUserMembershipTypeTopBest(UserMembershipType.MAKEUP_MASTER, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof TopBestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) INNER JOIN users ON ( photos.userId = users.id ) WHERE ( ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) AND users.membershipType = '3' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 4;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST), factory.getSelectIdsQuery().build());
        assertEquals("Main menu: The best photos: UserMembershipType: makeup master", factory.getTitle().build(Language.EN));
        assertEquals(String.format("Photo list bottom text: The top best photos of users with membership type UserMembershipType: makeup master plural which got at least %d masks in period %s - %s.<br />Photo list bottom text: Sorted by sum marks DESC", MIN_MARKS_FOR_VERY_BEST, dateData.from2, dateData.to2), factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/photos/type/3/best/", factory.getLinkToFullList());

        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void galleryByUserMembershipTypeBestTest() {
        final DateData dateData = new DateData();
        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).galleryByUserMembershipTypeBest(UserMembershipType.MAKEUP_MASTER, 5, 16, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof BestFilteringStrategy);
        assertEquals(String.format("SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) INNER JOIN users ON ( photos.userId = users.id ) WHERE ( ( ( photoVoting.votingTime >= '%s' ) AND photoVoting.votingTime <= '%s' ) AND users.membershipType = '3' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '%d' ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC, photos.uploadTime DESC LIMIT 16 OFFSET 64;", dateData.from1, dateData.to1, MIN_MARKS_FOR_VERY_BEST), factory.getSelectIdsQuery().build());
        assertEquals("Main menu: The best photos: UserMembershipType: makeup master", factory.getTitle().build(Language.EN));
        assertEquals(String.format("Photo list bottom text: The best photos of users with membership type UserMembershipType: makeup master plural which got at least %d marks in period %s - %s<br />Photo list bottom text: Sorted by sum marks DESC", MIN_MARKS_FOR_VERY_BEST, dateData.from2, dateData.to2), factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void userAlbumPhotosTest() {

        final int photoAlbum = 3455;

        final UserPhotoAlbumService userPhotoAlbumService = EasyMock.createMock(UserPhotoAlbumService.class);
        EasyMock.expect(userPhotoAlbumService.loadAlbumPhotoIds(photoAlbum)).andReturn(newArrayList()).anyTimes();
        EasyMock.replay(userPhotoAlbumService);

        final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
        userPhotoAlbum.setId(photoAlbum);
        userPhotoAlbum.setUser(testData.user);

        final PhotoListFactoryServiceImpl photoListFactoryService = getPhotoListFactoryService(testData);
        photoListFactoryService.setUserPhotoAlbumService(userPhotoAlbumService);

        final AbstractPhotoListFactory factory = photoListFactoryService.userAlbumPhotos(testData.user, userPhotoAlbum, 5, 16, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos LEFT OUTER JOIN photoAlbums ON ( photos.id = photoAlbums.photoId ) WHERE ( ( photos.userId = '112' ) AND photoAlbums.photoAlbumId = '3455' ) ORDER BY photos.uploadTime DESC LIMIT 16 OFFSET 64;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: album <a href=\"http://127.0.0.1:8085/worker/members/112/albums/3455/\" title\"EntityLinkUtilsService: User photo album link title: null\">null</a>", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: all photos from album <a href=\"http://127.0.0.1:8085/worker/members/112/albums/3455/\" title\"EntityLinkUtilsService: User photo album link title: null\">null</a><br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    @Test
    public void userAlbumPhotosWithMembersTest() {
        final int photoAlbumId = 3455;
        final int photoInAlbumId_1 = 1;
        final int photoInAlbumId_2 = 2;

        final UserTeamMember userTeamMember = new UserTeamMember();
        userTeamMember.setId(123);

        final PhotoTeamMember photoTeamMember = new PhotoTeamMember();
        photoTeamMember.setUserTeamMember(userTeamMember);

        final EntityLinkUtilsService entityLinkUtilsService = EasyMock.createMock(EntityLinkUtilsService.class);
        EasyMock.expect(entityLinkUtilsService.getUserTeamMemberCardLink(userTeamMember, Language.EN)).andReturn("UserTeamMember link").anyTimes();
        EasyMock.replay(entityLinkUtilsService);

        final UserPhotoAlbumService userPhotoAlbumService = EasyMock.createMock(UserPhotoAlbumService.class);
        EasyMock.expect(userPhotoAlbumService.loadAlbumPhotoIds(photoAlbumId)).andReturn(newArrayList(photoInAlbumId_1, photoInAlbumId_2)).anyTimes();
        EasyMock.replay(userPhotoAlbumService);

        final UserTeamService userTeamService = EasyMock.createMock(UserTeamService.class);
        EasyMock.expect(userTeamService.getPhotoTeam(photoInAlbumId_1)).andReturn(new PhotoTeam(new Photo(), newArrayList(photoTeamMember))).anyTimes();
        EasyMock.expect(userTeamService.getPhotoTeam(photoInAlbumId_2)).andReturn(new PhotoTeam(new Photo(), newArrayList(photoTeamMember))).anyTimes();
        EasyMock.replay(userTeamService);

        final PhotoListFactoryServiceImpl photoListFactoryService = getPhotoListFactoryService(testData);
        photoListFactoryService.setUserTeamService(userTeamService);
        photoListFactoryService.setUserPhotoAlbumService(userPhotoAlbumService);
        photoListFactoryService.setEntityLinkUtilsService(entityLinkUtilsService);

        final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
        userPhotoAlbum.setId(photoAlbumId);
        userPhotoAlbum.setName("The album");
        userPhotoAlbum.setUser(testData.user);

        final AbstractPhotoListFactory factory = photoListFactoryService.userAlbumPhotos(testData.user, userPhotoAlbum, 5, 16, testData.accessor);

        assertEquals("Photo list title: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: album <a href=\"http://127.0.0.1:8085/worker/members/112/albums/3455/\" title\"EntityLinkUtilsService: User photo album link title: The album\">The album</a> ( Team: UserTeamMember link )", factory.getTitle().build(Language.EN));
    }

    @Test
    public void userAlbumPhotosOwnGroupMenusTest() {
        final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
        userPhotoAlbum.setId(3455);
        userPhotoAlbum.setUser(testData.user);

        assertGroupOperationMenusForUser(getPhotoListFactoryService(testData).userAlbumPhotos(testData.accessor, userPhotoAlbum, 5, 16, testData.accessor));
    }

    @Test
    public void userAlbumPhotosLastTest() {

        final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
        userPhotoAlbum.setId(3455);
        userPhotoAlbum.setUser(testData.user);

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).userAlbumPhotosLast(testData.user, userPhotoAlbum, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos LEFT OUTER JOIN photoAlbums ON ( photos.id = photoAlbums.photoId ) WHERE ( ( photos.userId = '112' ) AND photoAlbums.photoAlbumId = '3455' ) ORDER BY photos.uploadTime DESC LIMIT 4;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: album <a href=\"http://127.0.0.1:8085/worker/members/112/albums/3455/\" title\"EntityLinkUtilsService: User photo album link title: null\">null</a>", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: the latest photos from album <a href=\"http://127.0.0.1:8085/worker/members/112/albums/3455/\" title\"EntityLinkUtilsService: User photo album link title: null\">null</a> photos", factory.getCriteriaDescription().build(Language.EN));
        assertEquals("http://127.0.0.1:8085/worker/members/112/albums/3455/", factory.getLinkToFullList());

        assertGroupOperationMenusEmpty(factory);
    }

    @Test
    public void photosOfFavoriteAuthorsOfUserTest() {

        final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
        userPhotoAlbum.setId(3455);
        userPhotoAlbum.setUser(testData.user);

        final AbstractPhotoListFactory factory = getPhotoListFactoryService(testData).photosOfFavoriteAuthorsOfUser(testData.user, 1, 25, testData.accessor);

        assertTrue(factory.getPhotoFilteringStrategy() instanceof HideAnonymousPhotosFilteringStrategy);
        assertEquals("SELECT photos.id FROM photos AS photos INNER JOIN favorites ON ( photos.userId = favorites.favoriteEntryId ) WHERE ( favorites.userId = '112' AND favorites.entryType = '1' ) ORDER BY photos.uploadTime DESC LIMIT 25;", factory.getSelectIdsQuery().build());
        assertEquals("Photo list title: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: photos of favorite authors", factory.getTitle().build(Language.EN));
        assertEquals("Photo list bottom text: User <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: User card owner: user card link title\">User card owner</a>: photos of favorite authors<br />Photo list bottom text: Sorted by upload time DESC", factory.getCriteriaDescription().build(Language.EN));
        assertEquals(emptyLink(), factory.getLinkToFullList());

        assertGroupOperationMenusForUserThenAdmin(factory);
    }

    private PhotoListFactoryServiceImpl getPhotoListFactoryService(final TestData testData) {
        final ConfigurationService configurationService = getConfigurationService(testData);

        final PhotoListFactoryServiceImpl photoListFactoryService = new PhotoListFactoryServiceImpl();

        final ServicesImpl services = getServices();
        services.setConfigurationService(configurationService);
        services.setPhotoVotingService(getPhotoVotingService());
        services.setUrlUtilsService(urlUtilsService);
        services.setGroupOperationService(getGroupOperationService(testData));
        services.setUserService(getUserService(testData));

        photoListFactoryService.setServices(services);
        //		photoListFactoryService.setConfigurationService( configurationService );
        photoListFactoryService.setDateUtilsService(dateUtilsService);

        return photoListFactoryService;
    }

    private UserService getUserService(final TestData testData) {
        final UserService userService = EasyMock.createMock(UserService.class);

        EasyMock.expect(userService.load(testData.user.getId())).andReturn(testData.user).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(userService);

        return userService;
    }

    private GroupOperationService getGroupOperationService(final TestData testData) {
        final GroupOperationService groupOperationService = EasyMock.createMock(GroupOperationService.class);

        EasyMock.expect(groupOperationService.getNoPhotoGroupOperationMenuContainer()).andReturn(new PhotoGroupOperationMenuContainer(GROUP_OPERATION_MENUS_EMPTY)).anyTimes();
        EasyMock.expect(groupOperationService.getUserOwnPhotosGroupOperationMenus()).andReturn(GROUP_OPERATION_MENUS_USER).anyTimes();
        EasyMock.expect(groupOperationService.getPhotoListPhotoGroupOperationMenuContainer(testData.user)).andReturn(new PhotoGroupOperationMenuContainer(GROUP_OPERATION_MENUS_DEFAULT)).anyTimes();
        EasyMock.expect(groupOperationService.getPhotoListPhotoGroupOperationMenuContainer(testData.accessor)).andReturn(new PhotoGroupOperationMenuContainer(GROUP_OPERATION_MENUS_DEFAULT)).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(groupOperationService);

        return groupOperationService;
    }

    private PhotoVotingService getPhotoVotingService() {
        final TimeRange timeRange = new TimeRange(dateUtilsService.getFirstSecondOfDay(dateUtilsService.getDatesOffsetFromCurrentDate(-DAYS + 1)), dateUtilsService.getLastSecondOfToday());

        final PhotoVotingService photoVotingService = EasyMock.createMock(PhotoVotingService.class);

        EasyMock.expect(photoVotingService.getTopBestDateRange()).andReturn(timeRange).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(photoVotingService);

        return photoVotingService;
    }

    private ConfigurationService getConfigurationService(final TestData testData) {

        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);

        EasyMock.expect(configurationService.getInt(ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY)).andReturn(4).anyTimes();
        EasyMock.expect(configurationService.getInt(ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS)).andReturn(DAYS).anyTimes();
        EasyMock.expect(configurationService.getInt(ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO)).andReturn(MIN_MARKS_FOR_VERY_BEST).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);

        return configurationService;
    }

    private void assertGroupOperationMenusEmpty(final AbstractPhotoListFactory factory) {
        assertTrue(factory.getGroupOperationMenuContainer().getGroupOperationMenus() == GROUP_OPERATION_MENUS_EMPTY);
    }

    private void assertGroupOperationMenusForUserThenAdmin(final AbstractPhotoListFactory factory) {
        assertTrue(factory.getGroupOperationMenuContainer().getGroupOperationMenus() == GROUP_OPERATION_MENUS_DEFAULT);
    }

    private void assertGroupOperationMenusForUser(final AbstractPhotoListFactory factory) {
        final PhotoGroupOperationMenuContainer groupOperationMenuContainer = factory.getGroupOperationMenuContainer();
        final List<PhotoGroupOperationMenu> operationMenus = groupOperationMenuContainer.getGroupOperationMenus();

        assertTrue(A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu(operationMenus, PhotoGroupOperationType.ARRANGE_PHOTO_ALBUMS));
        assertTrue(A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu(operationMenus, PhotoGroupOperationType.ARRANGE_TEAM_MEMBERS));
        assertTrue(A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu(operationMenus, PhotoGroupOperationType.ARRANGE_NUDE_CONTENT));
        assertTrue(A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu(operationMenus, PhotoGroupOperationType.MOVE_TO_GENRE));
        assertTrue(A_GROUP_MENU_CAN_NOT_BE_FOUND, containsMenu(operationMenus, PhotoGroupOperationType.DELETE_PHOTOS));
    }

    private class DateData {

        final Date timeFrom = dateUtilsService.getFirstSecondOfDay(dateUtilsService.getDatesOffsetFromCurrentDate(-DAYS + 1));
        final Date timeTo = dateUtilsService.getLastSecondOfDay(dateUtilsService.getCurrentTime());

        final String to1 = dateUtilsService.formatDateTime(timeTo);
        final String from1 = dateUtilsService.formatDateTime(timeFrom);

        final String to2 = dateUtilsService.formatDate(timeTo);
        final String from2 = dateUtilsService.formatDate(timeFrom);
    }

    private static String emptyLink() {
        return StringUtils.EMPTY;
    }

    private boolean containsMenu(final List<PhotoGroupOperationMenu> operationMenus, final PhotoGroupOperationType operationType) {

        for (final PhotoGroupOperationMenu photoGroupOperationMenu : operationMenus) {
            if (photoGroupOperationMenu.getPhotoGroupOperation() == operationType) {
                return true;
            }
        }

        return false;
    }
}
