package photo.list.factory;

import core.enums.RestrictionType;
import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.ServicesImpl;
import core.services.translator.Language;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import ui.controllers.photos.list.factory.AbstractPhotoListFactory;
import ui.controllers.photos.list.factory.PhotoListFactoryTopBest;
import ui.elements.PhotoList;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryTopBestTest extends AbstractPhotoListFactoryTest_ {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void galleryTopBestTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );
		testData.votingTimeFrom = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.votingTimeTo = dateUtilsService.parseDateTime( "2014-08-10 01:11:12" );

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: appraised from to 2014-08-15 - 2014-08-10", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/from/2014-08-15/to/2014-08-10/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: appraised from to 2014-08-15 - 2014-08-10", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void galleryTopBestForUserTest() {

		final User user = new User();
		user.setId( 112 );
		user.setName( "Just a user" );

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );
		testData.user = user;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.user, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a>", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/members/112/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a>", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void galleryTopBestForGenresTest() {

		final Genre genre = new Genre();
		genre.setId( 222 );
		genre.setName( "Glamour" );

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );
		testData.genre = genre;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.genre, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: in category Translated entry", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/genres/222/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: in category Translated entry", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void galleryTopBestForUserAndGenresTest() {

		final User user = new User();
		user.setId( 112 );
		user.setName( "Just a user" );

		final Genre genre = new Genre();
		genre.setId( 222 );
		genre.setName( "Glamour" );

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );
		testData.user = user;
		testData.genre = genre;

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.user, testData.genre, testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/members/112/genre/222/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void photoIsNotShownIfItHasSuitableRestrictionTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-10 10:11:22" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );

		testData.votingTimeFrom = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.votingTimeTo = dateUtilsService.parseDateTime( "2014-08-10 01:11:12" );

		testData.restrictedPhotos = newArrayList();
		testData.restrictedPhotos.add( new Pair<>( 2001, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2003, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_APPRAISAL ) );

		final ServicesImpl services = getTestServices( testData );
		services.setRestrictionService( getRestrictionService( testData, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "2000,2002,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	@Test
	public void photoIsNotShownIfItHasSuitableRestriction_ForAdminToo_Test() {

		final TestData testData = new TestData( SUPER_ADMIN_1 );
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-10 10:11:22" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );

		testData.votingTimeFrom = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.votingTimeTo = dateUtilsService.parseDateTime( "2014-08-10 01:11:12" );

		testData.restrictedPhotos = newArrayList();
		testData.restrictedPhotos.add( new Pair<>( 2001, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2003, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );
		testData.restrictedPhotos.add( new Pair<>( 2005, RestrictionType.PHOTO_APPRAISAL ) );

		final ServicesImpl services = getTestServices( testData );
		services.setRestrictionService( getRestrictionService( testData, RestrictionType.PHOTO_SHOWING_IN_TOP_OF_GENRE ) );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "2000,2002,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}
}
