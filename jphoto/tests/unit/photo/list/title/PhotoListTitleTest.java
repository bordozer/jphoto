package photo.list.title;

import common.AbstractTestCase;
import core.general.user.UserMembershipType;
import core.services.utils.DateUtilsService;
import mocks.GenreMock;
import core.general.data.PhotoListCriterias;
import core.services.system.ServicesImpl;
import mocks.PhotoVotingCategoryMock;
import mocks.UserMock;
import org.junit.Before;
import org.junit.Test;
import ui.controllers.photos.list.title.AbstractPhotoListTitle;
import ui.controllers.photos.list.title.PhotoListTitle;

import static junit.framework.Assert.assertEquals;

public class PhotoListTitleTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void noCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, getServices() );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void userCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setUser( new UserMock( 111 ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, getServices() );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Mock user #111: user card link title\">Mock user #111</a>", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Mock user #111: user card link title\">Mock user #111</a>", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void genreCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setGenre( new GenreMock( 333 ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, getServices() );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: in category Translated entry", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: in category Translated entry", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void userAndGenreCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setUser( new UserMock( 111 ) );
		criterias.setGenre( new GenreMock( 333 ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, getServices() );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Mock user #111: user card link title\">Mock user #111</a> Top best photo list title: in category Translated entry", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/111/card/\" title=\"EntityLinkUtilsService: Mock user #111: user card link title\">Mock user #111</a> Top best photo list title: in category Translated entry", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void membershipTypeCriteriasTest() {

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setMembershipType( UserMembershipType.MODEL );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, getServices() );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list description: with membership type UserMembershipType: model plural", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void uploadDateCriteriasTest() {

		final ServicesImpl services = getServices();
		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setUploadDateFrom( dateUtilsService.parseDate( "2014-03-01" ) );
		criterias.setUploadDateTo( dateUtilsService.parseDate( "2014-03-05" ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, services );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: uploaded from to 2014-03-01 - 2014-03-05", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: uploaded from to 2014-03-01 - 2014-03-05", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void appraisalDateCriteriasTest() {

		final ServicesImpl services = getServices();
		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setVotingTimeFrom( dateUtilsService.parseDate( "2014-04-03" ) );
		criterias.setVotingTimeTo( dateUtilsService.parseDate( "2014-04-07" ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, services );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: appraised from to 2014-04-03 - 2014-04-07", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: appraised from to 2014-04-03 - 2014-04-07", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void votedUserCriteriasTest() {

		final ServicesImpl services = getServices();

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setVotedUser( new UserMock( 222 ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, services );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: appraised by member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/222/card/\" title=\"EntityLinkUtilsService: Mock user #222: user card link title\">Mock user #222</a>", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: appraised by member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/222/card/\" title=\"EntityLinkUtilsService: Mock user #222: user card link title\">Mock user #222</a>", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}

	@Test
	public void votedUserAndCotingCategoryCriteriasTest() {

		final ServicesImpl services = getServices();

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setVotedUser( new UserMock( 222 ) );
		criterias.setVotingCategory( new PhotoVotingCategoryMock( 3335 ) );

		final AbstractPhotoListTitle galleryTitle = new PhotoListTitle( criterias, services );

		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_TITLES_ARE_NOT_EQUAL, "Photo list title Top best photo list title: appraised by member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/222/card/\" title=\"EntityLinkUtilsService: Mock user #222: user card link title\">Mock user #222</a>", galleryTitle.getPhotoListTitle().build( PhotoListTitleGalleryTest.LANGUAGE ) );
		assertEquals( PhotoListTitleGalleryTest.EXPECTED_AND_ACTUAL_DESCRIPTIONS_ARE_NOT_EQUAL, "Photo list description Top best photo list title: appraised by member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/222/card/\" title=\"EntityLinkUtilsService: Mock user #222: user card link title\">Mock user #222</a> Top best photo list description: has appraised as Translated entry", galleryTitle.getPhotoListDescription().build( PhotoListTitleGalleryTest.LANGUAGE ) );
	}
}
