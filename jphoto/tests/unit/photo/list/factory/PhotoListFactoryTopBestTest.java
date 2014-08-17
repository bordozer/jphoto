package photo.list.factory;

import common.AbstractTestCase;
import core.general.base.PagingModel;
import core.general.data.PhotoListCriterias;
import core.general.data.PhotoSort;
import core.general.user.User;
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

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;

public class PhotoListFactoryTopBestTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void galleryTest() {

		final User accessor = new User( 111 );
		accessor.setName( "Accessor" );

		final ServicesImpl services = getTestServices( accessor );

		final AbstractPhotoListFactory photoListFactoryTopBest = new PhotoListFactoryTopBest( accessor, services );
		final PagingModel pagingModel = new PagingModel( services );
		final PhotoList photoList = photoListFactoryTopBest.getPhotoList( 0, pagingModel, Language.EN );

		assertEquals( "", "Photo gallery root", photoList.getPhotoListTitle() );
		assertEquals( "", "Photo gallery root", photoList.getBottomText() );
		assertEquals( "", "Photo gallery root", photoList.getLinkToFullList() );
		assertEquals( "", "Photo gallery root", photoList.getPhotosCriteriasDescription() );
		assertEquals( "", "2000, 2001, 2002, 2003", StringUtils.join( photoList.getPhotoIds(), "," ) );
	}

	private ServicesImpl getTestServices( final User accessor ) {
		final ServicesImpl services = getServices();

		services.setPhotoListCriteriasService( getPhotoListCriteriasService( accessor ) );
		services.setPhotoCriteriasSqlService( photoCriteriasSqlService );
		services.setPhotoService( getPhotoService() );
		services.setRestrictionService( getRestrictionService() );

		return services;
	}

	private RestrictionService getRestrictionService() {
		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );

		EasyMock.expect( restrictionService.isPhotoShowingInTopBestRestrictedOn( 2001,  ) ).andReturn( idsResult ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		return restrictionService;
	}

	private PhotoService getPhotoService() {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		final SqlSelectIdsResult idsResult = new SqlSelectIdsResult();
		idsResult.setIds( newArrayList( 2000, 2001, 2002, 2003 ) );
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

		EasyMock.expect( photoListCriteriasService.getForAllPhotosTopBest( accessor ) ).andReturn( criterias ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoListCriteriasService );

		return photoListCriteriasService;
	}
}
