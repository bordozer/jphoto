package utils;

import common.AbstractTestCase;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UrlUtilsServiceTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void contextAndPrefixTest() {
		final UrlUtilsServiceImpl urlUtilsService = new UrlUtilsServiceImpl();
		urlUtilsService.setSystemVarsService( systemVarsServiceMock );

		assertEquals( "http://127.0.0.1:8085/worker", urlUtilsService.getBaseURL() );

		assertEquals( "http://127.0.0.1:8085/worker/adm", urlUtilsService.getBaseAdminURL() );

		assertEquals( "http://127.0.0.1:8085/worker", urlUtilsService.getPortalPageURL() );
	}

	@Test
	public void noContextTest() {

		final SystemVarsService systemVarsService = EasyMock.createMock( SystemVarsService.class );
		EasyMock.expect( systemVarsService.getProjectUrl() ).andReturn( "http://127.0.0.1:8085" ).anyTimes();
		EasyMock.expect( systemVarsService.getTomcatWorkerName() ).andReturn( "" ).anyTimes();
		EasyMock.expect( systemVarsService.getAdminPrefix() ).andReturn( "/adm" ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( systemVarsService );

		final UrlUtilsServiceImpl urlUtilsService = new UrlUtilsServiceImpl();
		urlUtilsService.setSystemVarsService( systemVarsService );

		assertEquals( "http://127.0.0.1:8085", urlUtilsService.getBaseURL() );

		assertEquals( "http://127.0.0.1:8085/adm", urlUtilsService.getBaseAdminURL() );

		assertEquals( "http://127.0.0.1:8085", urlUtilsService.getPortalPageURL() );
	}
}
