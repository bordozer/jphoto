package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.PrivateMessageType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.cache.CacheKey;
import core.general.configuration.ConfigurationKey;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.general.user.UserStatus;
import core.log.LogHelper;
import core.services.entry.ActivityStreamService;
import core.services.translator.message.TranslatableMessage;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;

import java.util.List;
import java.util.Map;

public class UserStatusRecalculationJob extends NoParametersAbstractJob {

	public UserStatusRecalculationJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( UserStatusRecalculationJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {
		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final UserService userService = services.getUserService();
		final ActivityStreamService activityStreamService = services.getActivityStreamService();

		final List<User> users = userService.loadAll();

		for ( final User user : users ) {

			if ( user.getUserStatus() == UserStatus.MEMBER ) {
				increment();
				continue;
			}

			final int photoQty = services.getPhotoService().getPhotoQtyByUser( user.getId() );
			if ( photoQty >= services.getConfigurationService().getInt( ConfigurationKey.CANDIDATES_PHOTOS_QTY_TO_BECOME_MEMBER ) ) {
				if ( userService.setUserStatus( user.getId(), UserStatus.MEMBER ) ) {

					sendSystemNotificationAboutGotMembershipToUser( user );

					getLog().info( String.format( "Member %s has got new status: %s", user.getId(), UserStatus.MEMBER.getName() ) );

					/*final String message = services.getTranslatorService().translate( "Member $1 has got new status: $2"
						, getLanguage()
						, String.valueOf( user.getId() )
						, UserStatus.MEMBER.getName()
					);*/
					final TranslatableMessage translatableMessage = new TranslatableMessage( "Member $1 has got new status: $2", services )
						.addLinkToUserCardUnit( user )
						.addStringTranslatableUnit( UserStatus.MEMBER.getName() );
					addJobRuntimeLogMessage( translatableMessage );

					activityStreamService.saveUserStatusChange( user, UserStatus.CANDIDATE, UserStatus.MEMBER, dateUtilsService.getCurrentTime(), services );
				} else {
					getLog().error( String.format( "Can not update member status. Id = # %s", user.getId() ) );

					/*final String message = services.getTranslatorService().translate( "Can not update member status. Id = #$1"
						, getLanguage()
						, String.valueOf( user.getId() )
					);*/
					final TranslatableMessage translatableMessage = new TranslatableMessage( "Member $1 has got new status: $2", services ).addLinkToUserCardUnit( user );
					addJobRuntimeLogMessage( translatableMessage );
				}
			}

			increment();

			if ( isFinished() ) {
				break;
			}

			if ( hasJobFinishedWithAnyResult() ) {
				break;
			}
		}

		services.getCacheService().expire( CacheKey.USER );
		getLog().debug( "User cache has been cleared" );
	}

	private void sendSystemNotificationAboutGotMembershipToUser( final User user ) {
		final PrivateMessage message = new PrivateMessage();

		message.setToUser( user );
		message.setCreationTime( services.getDateUtilsService().getCurrentTime() );
		message.setPrivateMessageType( PrivateMessageType.SYSTEM_NOTIFICATIONS );
		message.setMessageText( String.format( "You have bees given a new club status: %s", UserStatus.MEMBER.getNameTranslated() ) );

		services.getPrivateMessageService().save( message );
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		super.initJobParameters( jobParameters );

		totalJopOperations = services.getUserService().getUserCount();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.USER_STATUS;
	}
}
