package photo.list.factory;

import common.AbstractTestCase;
import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.data.PhotoSort;
import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenu;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.entry.GroupOperationService;
import core.services.photo.PhotoListCriteriasService;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.system.ServicesImpl;
import core.services.translator.Language;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.controllers.photos.list.factory.AbstractPhotoListFactory;
import ui.controllers.photos.list.factory.PhotoListFactoryTopBest;
import ui.elements.PhotoList;

import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryTopBestTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void galleryTopBestTest() {

		final TestData testData = new TestData();
		testData.currentTime = dateUtilsService.parseDateTime( "2014-08-15 11:38:45" );
		testData.photoIds = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );

		final ServicesImpl services = getTestServices( testData );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( testData.accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, testData.currentTime );

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/from/2014-08-13/to/2014-08-15/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotosCriteriasDescription() );
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

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/members/112/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotosCriteriasDescription() );
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

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: in category Translated entry Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/genres/222/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: in category Translated entry Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotosCriteriasDescription() );
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

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/members/112/genre/222/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: of member <a class=\"member-link\" href=\"http://127.0.0.1:8085/worker/members/112/card/\" title=\"EntityLinkUtilsService: Just a user: user card link title\">Just a user</a> Top best photo list title: in category Translated entry Top best photo list title: appraised from to 2014-08-13 - 2014-08-15", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	private ServicesImpl getTestServices( final TestData testData ) {
		final ServicesImpl services = getServices();

		services.setPhotoListCriteriasService( getPhotoListCriteriasService( testData ) );
		services.setPhotoCriteriasSqlService( photoCriteriasSqlService );
		services.setPhotoService( getPhotoService( testData ) );
		services.setRestrictionService( getRestrictionService( testData ) );
		services.setUrlUtilsService( urlUtilsService );
		services.setGroupOperationService( getGroupOperationService() );

		return services;
	}

	private GroupOperationService getGroupOperationService() {
		final GroupOperationService groupOperationService = EasyMock.createMock( GroupOperationService.class );

		EasyMock.expect( groupOperationService.getNoPhotoGroupOperationMenuContainer() ).andReturn( new PhotoGroupOperationMenuContainer( Collections.<PhotoGroupOperationMenu>emptyList() ) ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( groupOperationService );

		return groupOperationService;
	}

	private RestrictionService getRestrictionService( final TestData testData ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		for ( final int photosId : testData.photoIds ) {
			EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( photosId, testData.currentTime ) ).andReturn( false ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	private PhotoService getPhotoService( final TestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		final SqlSelectIdsResult idsResult = new SqlSelectIdsResult();
		idsResult.setIds( testData.photoIds );
		idsResult.setRecordQty( 4 );

		EasyMock.expect( photoService.load( EasyMock.<SqlIdsSelectQuery>anyObject() ) ).andReturn( idsResult ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	private PhotoListCriteriasService getPhotoListCriteriasService( final TestData testData ) {
		final PhotoListCriteriasService photoListCriteriasService = EasyMock.createMock( PhotoListCriteriasService.class );

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setPhotoSort( PhotoSort.UPLOAD_TIME );
		criterias.setVotingTimeFrom( dateUtilsService.getDatesOffset( testData.currentTime, - 2 ) );
		criterias.setVotingTimeTo( testData.currentTime );

		if ( testData.user != null ) {
			criterias.setUser( testData.user );
		}

		if ( testData.genre != null ) {
			criterias.setGenre( testData.genre );
		}

		EasyMock.expect( photoListCriteriasService.getForAllPhotosTopBest( testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForGenreTopBest( testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserTopBest( testData.user, testData.accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getForUserAndGenreTopBest( testData.user, testData.genre, testData.accessor ) ).andReturn( criterias ).anyTimes();

		EasyMock.expect( photoListCriteriasService.getLinkToFullListText( criterias ) ).andReturn( "Link To Full List Text" ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListCriteriasService );

		return photoListCriteriasService;
	}
}
