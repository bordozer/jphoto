package utils;

import common.AbstractTestCase;
import core.general.configuration.SystemConfiguration;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EntityLinkUtilsServiceTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void test() {
		
		final String projectUrl = systemVarsServiceMock.getProjectUrlClosed();
		final String workerName = systemVarsServiceMock.getTomcatWorkerName();
		final String appPrefix = systemVarsServiceMock.getApplicationPrefix();
		final String adminPrefix = systemVarsServiceMock.getAdminPrefix();

		final SystemConfiguration systemConfiguration = new SystemConfiguration();
		systemConfiguration.setId( 333 );
		systemConfiguration.setName( "System Configuration Name" );

		final User user = new User();
		user.setId( 111 );
		user.setName( "User name" );

		final Photo photo = new Photo();
		photo.setId( 444 );
		photo.setName( "Photo name" );

		final Genre genre = new Genre();
		genre.setId( 555 );
		genre.setName( "Genre name" );

		assertEquals( String.format( "<a href=\"%s%s/%s/\" title=\"Main page\">Test jPhoto</a>", projectUrl, workerName, appPrefix ), entityLinkUtilsService.getPortalPageLink() );
		assertEquals( String.format( "<a class=\"member-link\" href=\"%s%s/%s/members/111/card/\" title=\"User name: card\">User name</a>", projectUrl, workerName, appPrefix ), entityLinkUtilsService.getUserCardLink( user ) );
//		assertEquals( String.format( "<a href=\"%s%s/%s/photos/members/111/\" title=\"User name &sup3;: all photos\">%s</a>", projectUrl, workerName, appPrefix, TranslatorUtils.translate( "Photos" ) ), entityLinkUtilsService.getPhotosByUserLink( user ) );
		assertEquals( String.format( "<a href=\"%s%s/%s/photos/genres/555/\" title=\"All photos in genre '%s'\">%s</a>", projectUrl, workerName, appPrefix, TRANSLATED_ENTRY, TRANSLATED_ENTRY ), entityLinkUtilsService.getPhotosByGenreLink( genre ) );
		assertEquals( String.format( "<a class=\"photos-by-user-by-genre-link\" href=\"%s%s/%s/photos/members/111/genre/555/\" title=\"User name: all photos in category '%s'\">%s</a>", projectUrl, workerName, appPrefix, TRANSLATED_ENTRY, TRANSLATED_ENTRY )
			, entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre ) );
//		assertEquals( String.format( "<a href=\"%s%s/%s/photos/type/2/\">%s</a>", projectUrl, workerName, appPrefix, TranslatorUtils.translate( "Models" ) ), entityLinkUtilsService.getPhotosByMembershipLink( UserMembershipType.MODEL )  );

		final Date dateFrom = new Date( 1360101600000L ); // the first second of 2013-02-06
		final Date dateTo = new Date( 1360187999999L );   // the last second of 2013-02-06
		assertEquals( String.format( "<a href=\"%s%s/%s/photos/from/2013-02-06/to/2013-02-06/uploaded/\">2013-02-06 - 2013-02-06</a>", projectUrl, workerName, appPrefix ), entityLinkUtilsService.getPhotosByPeriod( dateFrom, dateTo ) );

//		assertEquals( String.format( "<a href=\"%s%s/%s/photos/444/card/\">%s</a>", projectUrl, workerName, appPrefix, TranslatorUtils.translate( "Photo name" ) ), entityLinkUtilsService.getPhotoCardLink( photo ) );
//		assertEquals( String.format( "<a href=\"%s%s/%s/members/\">Users</a>", projectUrl, workerName, appPrefix ), entityLinkUtilsService.getUsersRootLink() );
//		assertEquals( String.format( "<a href=\"%s%s/%s/photos/\" title=\"All photos\">Photos</a>", projectUrl, workerName, appPrefix ), entityLinkUtilsService.getPhotosRootLink() );
//		assertEquals( String.format( "<a href=\"%s%s/%s/members/111/comments/to/\" title=\"Comments of User name &sup3;\">Comments</a>", projectUrl, workerName, appPrefix ), entityLinkUtilsService.getPhotoCommentsToUserLink( user ) );

//		assertEquals( String.format( "<a href=\"%s%s/%s/configuration/1/tabs/members/info/\">Members</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminConfigurationLink( 1, ConfigurationTab.MEMBERS ) );
//		assertEquals( String.format( "<a href=\"%s%s/%s/genres/\">Genres</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminGenresRootLink() );
//		assertEquals( String.format( "<a href=\"%s%s/%s/configuration/333/tabs/all/info/\">System Configuration Name</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminConfigurationTabsLink( systemConfiguration ) );
//		assertEquals( String.format( "<a href=\"%s%s/%s/configuration/\">System configuration</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminSystemConfigurationListLink() );
//		assertEquals( String.format( "<a href=\"%s%s/%s/configuration/333/tabs/photoComments/info/\">Photo comments</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminConfigurationLink( systemConfiguration.getId(), ConfigurationTab.COMMENTS )  );
//		assertEquals( String.format( "<a href=\"%s%s/%s/jobs/\">Jobs</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminJobsRootLink() );
//		assertEquals( String.format( "<a href=\"%s%s/%s/scheduler/tasks/\">Scheduler tasks</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminSchedulerTaskListLink() );
//		assertEquals( String.format( "<a href=\"%s%s/%s/votingcategories/\">Voting category</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminVotingCategoriesRootLink() );
//		assertEquals( String.format( "<a href=\"%s%s/%s/upgrade/\">Upgrade tasks</a>", projectUrl, workerName, adminPrefix ), entityLinkUtilsService.getAdminUpgradeLink() );
	}
}
