package photo.list.factory;

import common.AbstractTestCase;
import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.data.PhotoSort;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryTopBestTest extends AbstractTestCase {

	public static final ArrayList<Integer> PHOTOS_IDS = newArrayList( 2000, 2001, 2002, 2003, 2004, 2005 );

	private Date currentTime;

	@Before
	public void setup() {
		super.setup();

		currentTime = dateUtilsService.parseDateTime( "2014-08-17 11:38:45" );
	}

	@Test
	public void galleryTest() {

		final User accessor = new User( 111 );
		accessor.setName( "Accessor" );

		final ServicesImpl services = getTestServices( accessor, currentTime );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN, currentTime );

		assertEquals( "Assertion fails", "Top best photo list title Top best photo list title: appraised from to 2014-08-19 - 2014-08-17", photoList.getPhotoListTitle() );
		assertEquals( "Assertion fails", "", photoList.getBottomText() );
		assertEquals( "Assertion fails", "http://127.0.0.1:8085/worker/photos/from/2014-08-19/to/2014-08-17/best/", photoList.getLinkToFullList() );
		assertEquals( "Assertion fails", "Top best photo list description Top best photo list title: appraised from to 2014-08-19 - 2014-08-17", photoList.getPhotosCriteriasDescription() );
		assertEquals( "Assertion fails", "2000,2001,2002,2003,2004,2005", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	private ServicesImpl getTestServices( final User accessor, final Date time ) {
		final ServicesImpl services = getServices();

		services.setPhotoListCriteriasService( getPhotoListCriteriasService( accessor ) );
		services.setPhotoCriteriasSqlService( photoCriteriasSqlService );
		services.setPhotoService( getPhotoService() );
		services.setRestrictionService( getRestrictionService( time ) );
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

	private RestrictionService getRestrictionService( final Date time ) {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		for ( final int photosId : PHOTOS_IDS ) {
			EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( photosId, time ) ).andReturn( false ).anyTimes();
		}

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	private PhotoService getPhotoService() {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		final SqlSelectIdsResult idsResult = new SqlSelectIdsResult();
		idsResult.setIds( PHOTOS_IDS );
		idsResult.setRecordQty( 4 );

		EasyMock.expect( photoService.load( EasyMock.<SqlIdsSelectQuery>anyObject() ) ).andReturn( idsResult ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	private PhotoListCriteriasService getPhotoListCriteriasService( final User accessor ) {
		final PhotoListCriteriasService photoListCriteriasService = EasyMock.createMock( PhotoListCriteriasService.class );

		final PhotoListCriterias criterias = new PhotoListCriterias();
		criterias.setPhotoSort( PhotoSort.UPLOAD_TIME );
		criterias.setVotingTimeFrom( dateUtilsService.getDatesOffset( currentTime, 2 ) );
		criterias.setVotingTimeTo( currentTime );

		EasyMock.expect( photoListCriteriasService.getForAllPhotosTopBest( accessor ) ).andReturn( criterias ).anyTimes();
		EasyMock.expect( photoListCriteriasService.getLinkToFullListText( criterias ) ).andReturn( "Link To Full List Text" ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListCriteriasService );

		return photoListCriteriasService;
	}
}
