package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.jobs.entries.AbstractJob;
import core.log.LogHelper;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import org.dom4j.DocumentException;

import java.io.File;

public class PhotosImportLogger {

	private AbstractJob job;
	private AbstractRemotePhotoSiteUrlHelper remoteContentHelper;
	private final Services services;

	public PhotosImportLogger( final AbstractJob job, final AbstractRemotePhotoSiteUrlHelper remoteContentHelper, final Services services ) {
		this.job = job;
		this.remoteContentHelper = remoteContentHelper;
		this.services = services;
	}

	public void logUserImportImportStart( final RemoteUser remoteUser ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "TODO", services );
			}
		}.log();
	}

	public void logUserCanNotBeCreated( final RemoteUser remoteUser ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "TODO", services );
			}
		}.log();
	}

	public void logInitRemoteUserCacheFileStructure( final RemoteUser remoteUser ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "TODO", services );
			}
		}.log();
	}

	public void logErrorReadingUserDataFile( final RemoteUser remoteUser, final File userDataFile, final DocumentException e ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "Error reading user info file: $1<br />$2", services )
					.string( userDataFile.getAbsolutePath() )
					.string( e.getMessage() )
					;
			}
		}.log();
	}

	public void logUserPageCountError( final RemoteUser remoteUser ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "TODO", services );
			}
		}.log();
	}

	public void logUserPageCountGotSuccessfully( final RemoteUser remoteUser, final int totalPagesQty ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "TODO", services );
			}
		}.log();
	}

	public void logErrorGettingUserPagesCount( final String remotePhotoSiteUserId ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "ERROR getting remote photo site user #$1 pages qty. Photos import of the user will be skipped.", services )
					.string( remotePhotoSiteUserId )
					;
			}
		}.log();
	}

	public void logGettingUserPagesCount( final String remotePhotoSiteUserId, final int userPagesCount, final int totalPages ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "Getting remote photo site user #$1 pages qty: $2 ( total: $3 )", services )
					.string( remotePhotoSiteUserId )
					.addIntegerParameter( userPagesCount )
					.addIntegerParameter( totalPages )
					;
			}
		}.log();
	}

	public void logNoPhotosFoundOnPage( final RemoteUser remoteUser, final int page ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "User #$2: no photo have been found on page $1", services )
					.string( remoteUser.getId() )
					.addIntegerParameter( page )
					;
			}
		}.log();
	}

	public void logSkippingTheRestPhotosBecauseAlreadyImportedPhotoFound( final String remotePhotoSiteUserPageLink, final int remotePhotoId ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "Photo $1 of $2 has already been imported", services )
					.addIntegerParameter( remotePhotoId )
					.string( remotePhotoSiteUserPageLink )
					;
			}
		}.log();
	}

	public void logSkippingPhotoImportBecauseItHasBeenAlreadyImported( final String remotePhotoSiteUserPageLink, final int remotePhotoId ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "Photo $1 of $2 has already been imported", services )
					.addIntegerParameter( remotePhotoId )
					.string( remotePhotoSiteUserPageLink )
					;
			}
		}.log();
	}

	public void logRemotePhotoHasBeenFoundInTheCache( final RemoteUser remoteUser, final RemotePhotoData remotePhotoData ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "$1: Found in the local cache: $2", services )
					.string( remoteContentHelper.getRemoteUserCardLink( remoteUser ) )
					.string( remoteContentHelper.getPhotoCardLink( remotePhotoData ) )
					;
			}
		}.log();
	}

	public void logPhotoSkipping( final RemoteUser remoteUser, final int remotePhotoId, final String customReason ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "$1 User: $2; photo: $3. Photo import skipped.", services )
					.string( customReason )
					.string( remoteContentHelper.getRemoteUserCardLink( remoteUser ) )
					.string( remoteContentHelper.getPhotoCardLink( remoteUser.getId(), remotePhotoId ) )
					;
			}
		}.log();
	}

	public void logCollectingRemotePhotoData( final int remotePhotoId, final String imageUrl ) {
		new LogMessenger() {
			@Override
			TranslatableMessage getMessage() {
				return new TranslatableMessage( "Collecting data of remote photo site photo #$1 ( $2 )", services )
					.addIntegerParameter( remotePhotoId )
					.link( imageUrl )
					;
			}
		}.log();
	}

	private abstract class LogMessenger {

		private final LogHelper log = new LogHelper( PhotosImportLogger.class );

		abstract TranslatableMessage getMessage();

		void log() {
			job.addJobRuntimeLogMessage( getMessage() );

			log.debug( getMessage().build( Language.NERD ) ); // TODO: language?
		}
	}
}
