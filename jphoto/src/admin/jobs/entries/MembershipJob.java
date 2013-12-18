package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.jobs.enums.SavedJobType;
import core.enums.PrivateMessageType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.cache.CacheKey;
import core.general.user.User;
import core.general.user.UserStatus;
import core.general.configuration.ConfigurationKey;
import core.general.message.PrivateMessage;
import core.log.LogHelper;

import java.util.List;
import java.util.Map;

public class MembershipJob extends NoParametersAbstractJob {

	public MembershipJob() {
		super( new LogHelper( MembershipJob.class ) );
	}

	@Override
	protected void runJob() throws Throwable {
		final List<User> users = services.getUserService().loadAll();

		for ( final User user : users ) {

			if ( user.getUserStatus() == UserStatus.MEMBER ) {
				increment();
				continue;
			}

			final int photoQty = services.getPhotoService().getPhotoQtyByUser( user.getId() );
			if ( photoQty >= services.getConfigurationService().getInt( ConfigurationKey.CANDIDATES_PHOTOS_QTY_TO_BECOME_MEMBER ) ) {
				if ( services.getUserService().setUserMembership( user.getId(), UserStatus.MEMBER ) ) {

					sendSystemNotificationAboutGotMembershipToUser( user );

					getLog().info( String.format( "Member %s has got new status: MEMBER", user.getId() ) );
				} else {
					getLog().error( String.format( "Can not update member status. Id = # %s", user.getId() ) );
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
		getLog().info( "User cache has been cleared" );
	}

	private void sendSystemNotificationAboutGotMembershipToUser( final User user ) {
		final PrivateMessage message = new PrivateMessage();

		message.setToUser( user );
		message.setCreationTime( services.getDateUtilsService().getCurrentTime() );
		message.setPrivateMessageType( PrivateMessageType.SYSTEM_NOTIFICATIONS );
		message.setMessageText( String.format( "You has bees given a new club status: %s", UserStatus.MEMBER.getNameTranslated() ) );

		services.getPrivateMessageService().save( message );
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		super.initJobParameters( jobParameters );

		totalJopOperations = services.getUserService().getUserCount();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.USER_MEMBERSHIP;
	}
}
