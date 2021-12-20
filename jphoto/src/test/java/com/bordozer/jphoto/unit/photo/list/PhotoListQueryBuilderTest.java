package photo.list;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.enums.UserTeamMemberType;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.utils.sql.PhotoListQueryBuilder;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import mocks.GenreMock;
import mocks.PhotoVotingCategoryMock;
import mocks.UserMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class PhotoListQueryBuilderTest extends AbstractTestCase {

    public static final String WRONG_SQL = "Wrong SQL";

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void photoListQueryBuilderTest_1_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_2_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .sortByUploadTimeDesc();

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos ORDER BY photos.uploadTime DESC;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_3_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByAuthor(new UserMock(111));

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '111' );", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_4_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByGenre(new GenreMock(222));

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( photos.genreId = '222' );", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_5_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByAuthor(new UserMock(111))
                .filterByGenre(new GenreMock(222));

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( ( photos.userId = '111' ) AND photos.genreId = '222' );", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_6_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByAuthor(new UserMock(111))
                .filterByGenre(new GenreMock(222))
                .sortByUploadTimeDesc();

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( ( photos.userId = '111' ) AND photos.genreId = '222' ) ORDER BY photos.uploadTime DESC;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_7_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByAuthor(new UserMock(111))
                .forPage(3, 16);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( photos.userId = '111' ) LIMIT 16 OFFSET 32;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_8_Test() {
        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByMembershipType(UserMembershipType.MODEL)
                .forPage(3, 16);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN users ON ( photos.userId = users.id ) WHERE ( users.membershipType = '2' ) LIMIT 16 OFFSET 32;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_9_Test() {

        final Date timeFrom = dateUtilsService.parseDateTime("2014-08-20 11:12:13");
        final Date timeTo = dateUtilsService.parseDateTime("2014-08-23 20:02:53");

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByVotingTime(timeFrom, timeTo)
                .forPage(1, 16);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '2014-08-20 00:00:00' ) AND photoVoting.votingTime <= '2014-08-23 23:59:59' ) GROUP BY photos.id LIMIT 16;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_10_Test() {

        final Date timeFrom = dateUtilsService.parseDateTime("2014-08-20 11:12:13");
        final Date timeTo = dateUtilsService.parseDateTime("2014-08-23 20:02:53");

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByVotingTime(timeFrom, timeTo)
                .forPage(1, 16)
                .sortBySumMarksDesc();

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.votingTime >= '2014-08-20 00:00:00' ) AND photoVoting.votingTime <= '2014-08-23 23:59:59' ) GROUP BY photos.id ORDER BY SUM( photoVoting.mark ) DESC, photos.uploadTime DESC LIMIT 16;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_10_5_Test() {

        final Date time = dateUtilsService.parseDateTime("2014-08-20 11:12:13");

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .uploadedOn(time);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( ( photos.uploadTime >= '2014-08-20 00:00:00' ) AND photos.uploadTime <= '2014-08-20 23:59:59' );", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_11_Test() {

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .sortByUploadTimeAsc();

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos ORDER BY photos.uploadTime ASC;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_12_Test() {

        final Date timeFrom = dateUtilsService.parseDateTime("2014-08-20 11:12:13");
        final Date timeTo = dateUtilsService.parseDateTime("2014-08-23 20:02:53");

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByUploadTime(timeFrom, timeTo);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos WHERE ( ( photos.uploadTime >= '2014-08-20 00:00:00' ) AND photos.uploadTime <= '2014-08-23 23:59:59' );", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_13_Test() {

        final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
        userPhotoAlbum.setId(435);
        userPhotoAlbum.setUser(new UserMock(111));

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByUserAlbum(userPhotoAlbum);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos LEFT OUTER JOIN photoAlbums ON ( photos.id = photoAlbums.photoId ) WHERE ( photoAlbums.photoAlbumId = '435' );", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_14_Test() {

        final UserTeamMember teamMember = new UserTeamMember();
        teamMember.setId(955);
        teamMember.setUser(new UserMock(554));
        teamMember.setTeamMemberType(UserTeamMemberType.PHOTOGRAPH);

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByUserTeamMember(teamMember);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos LEFT OUTER JOIN photoTeam ON ( photos.id = photoTeam.photoId ) WHERE ( photoTeam.userTeamMemberId = '955' );", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_15_Test() {

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterOnlyPhotosAddedByUserToBookmark(new UserMock(435), FavoriteEntryType.BLACKLIST);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN favorites ON ( photos.id = favorites.favoriteEntryId ) WHERE ( favorites.userId = '435' AND favorites.entryType = '4' ) ORDER BY favorites.created DESC;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_16_Test() {

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByMinimalMarks(41);

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '41';", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_17_Test() {

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByMinimalMarks(41)
                .filterByVotingCategory(new PhotoVotingCategoryMock(98));

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photoVoting.votingCategoryId = '98' ) GROUP BY photos.id HAVING SUM( photoVoting.mark ) >= '41';", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_19_Test() {

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByVotedUser(new UserMock(113))
                .filterByVotingCategory(new PhotoVotingCategoryMock(98));

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( ( photoVoting.userId = '113' ) AND photoVoting.votingCategoryId = '98' ) GROUP BY photos.id;", queryBuilder.getQuery().build());
    }

    @Test
    public void photoListQueryBuilderTest_18_Test() {

        final PhotoListQueryBuilder queryBuilder = new PhotoListQueryBuilder(dateUtilsService)
                .filterByVotedUser(new UserMock(113))
                .sortByVotingTimeDesc();

        assertEquals(WRONG_SQL, "SELECT photos.id FROM photos AS photos INNER JOIN photoVoting ON ( photos.id = photoVoting.photoId ) WHERE ( photoVoting.userId = '113' ) GROUP BY photos.id ORDER BY photoVoting.votingTime DESC;", queryBuilder.getQuery().build());
    }
}
