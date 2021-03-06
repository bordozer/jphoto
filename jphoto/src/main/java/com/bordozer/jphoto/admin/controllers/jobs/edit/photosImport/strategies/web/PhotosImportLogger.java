package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web;

import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import org.dom4j.DocumentException;

import java.io.File;

public class PhotosImportLogger {

    private AbstractJob job;
    private AbstractRemotePhotoSiteUrlHelper remoteContentHelper;
    private final Services services;

    public PhotosImportLogger(final AbstractJob job, final AbstractRemotePhotoSiteUrlHelper remoteContentHelper, final Services services) {
        this.job = job;
        this.remoteContentHelper = remoteContentHelper;
        this.services = services;
    }

    public void logUserImportImportStart(final String remoteUserId, final String remoteUserName) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("Remote photos import: $1 $2: starting import", services)
                        .link(remoteContentHelper.getRemotePhotoSiteHost())
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUserId, remoteUserName))
                        ;
            }
        }.log();
    }

    public void logUserCanNotBeCreated(final RemoteUser remoteUser) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("Remote photos import: $1: error creating an local user", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        ;
            }
        }.log();
    }

    public void logInitRemoteUserCacheFileStructure(final RemoteUser remoteUser) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: initialization of user cache file structure failed", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        ;
            }
        }.log();
    }

    public void logErrorReadingUserDataFile(final RemoteUser remoteUser, final File userDataFile, final DocumentException e) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: Error reading user info file: $2<br />$3", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        .string(userDataFile.getAbsolutePath())
                        .string(e.getMessage())
                        ;
            }
        }.log();
    }

    public void logUserPageCountError(final RemoteUser remoteUser) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: can not extract photos count from user card", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        ;
            }
        }.log();
    }

    public void logUserPageCountGotSuccessfully(final RemoteUser remoteUser, final int totalPagesQty) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: pages to import $2", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        ;
            }
        }.log();
    }

    public void logErrorGettingUserPagesCount(final String remotePhotoSiteUserId) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("ERROR getting remote photo site user #$1 pages qty. Photos import of the user will be skipped.", services)
                        .string(remotePhotoSiteUserId)
                        ;
            }
        }.log();
    }

    public void logUserPagesCountToProcessHasGotFromThePageContext(final String remotePhotoSiteUserId, final int userPagesCount, final int totalPages) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: user pages count to process has got from the page context - $2 ( total: $3 )", services)
                        .string(remotePhotoSiteUserId)
                        .addIntegerParameter(userPagesCount)
                        .addIntegerParameter(totalPages)
                        ;
            }
        }.log();
    }

    public void logNoPhotosFoundOnPage(final RemoteUser remoteUser, final int page) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("User #$2: no photo have been found on page $1", services)
                        .string(remoteUser.getId())
                        .addIntegerParameter(page)
                        ;
            }
        }.log();
    }

    public void logSkippingTheRestPhotosBecauseAlreadyImportedPhotoFound(final String remotePhotoSiteUserPageLink, final int remotePhotoId) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("Photo $1 of $2 has already been imported", services)
                        .addIntegerParameter(remotePhotoId)
                        .string(remotePhotoSiteUserPageLink)
                        ;
            }
        }.log();
    }

    public void logSkippingPhotoImportBecauseItHasBeenAlreadyImported(final String remotePhotoSiteUserPageLink, final int remotePhotoId) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("Photo $1 of $2 has already been imported", services)
                        .addIntegerParameter(remotePhotoId)
                        .string(remotePhotoSiteUserPageLink)
                        ;
            }
        }.log();
    }

    public void logRemotePhotoHasBeenFoundInTheCache(final RemoteUser remoteUser, final RemotePhotoData remotePhotoData) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: Found in the local cache: $2", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        .string(remoteContentHelper.getPhotoCardLink(remotePhotoData))
                        ;
            }
        }.log();
    }

    public void logPhotoSkipping(final RemoteUser remoteUser, final int remotePhotoId, final String customReason) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1 User: $2; photo: $3. Photo import skipped.", services)
                        .string(customReason)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        .string(remoteContentHelper.getPhotoCardLink(remoteUser.getId(), remotePhotoId))
                        ;
            }
        }.log();
    }

    public void logCollectingRemotePhotoDataForImage(final String remoteUserId, final int remotePhotoId, final String imageUrl) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("Collecting data of remote photo site photo $1: $2", services)
                        .string(remoteContentHelper.getPhotoCardLink(remoteUserId, remotePhotoId))
                        .link(imageUrl)
                        ;
            }
        }.log();
    }

    public void logErrorGettingPhotoUploadTime(final String remoteUserId, final int remotePhotoId) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("Can not get upload time from remote photo page ( $1 ). Random time is used.", services)
                        .string(remoteContentHelper.getPhotoCardLink(remoteUserId, remotePhotoId))
                        ;
            }
        }.log();
    }

    public void logSuccessDataCollectingOfRemotePhoto(final int counter, final int total, final RemotePhotoData remotePhotoData, final RemoteUser remoteUser, final Language language) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1 / $2 Got data of '$3': $4 of $5, photo category: $6", services)
                        .addIntegerParameter(counter)
                        .addIntegerParameter(total)
                        .string(remoteContentHelper.getRemotePhotoSiteHost())
                        .string(remoteContentHelper.getPhotoCardLink(remotePhotoData))
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        .string(remoteContentHelper.getPhotoCategoryLink(remotePhotoData.getRemotePhotoSiteCategory(), services.getEntityLinkUtilsService(), services.getGenreService(), language, services.getRemotePhotoCategoryService()))
                        ;
            }
        }.log();
    }

    public void logEmptyRemoteUserCardPageContent(final RemoteUser remoteUser, final int page) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: remote user card page ( $2 ) content is empty - skipping import user's photos", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        .addIntegerParameter(page)
                        ;
            }
        }.log();
    }

    public void logFoundPhotosOnRemoteUserCardPage(final RemoteUser remoteUser, final int page, final int count) {
        new LogMessenger() {
            @Override
            TranslatableMessage getMessage() {
                return new TranslatableMessage("$1: There are $2 photo(s) found on page $3", services)
                        .string(remoteContentHelper.getRemoteUserCardLink(remoteUser))
                        .addIntegerParameter(count)
                        .addIntegerParameter(page)
                        ;
            }
        }.log();
    }

    private abstract class LogMessenger {

        private final LogHelper log = new LogHelper();

        abstract TranslatableMessage getMessage();

        void log() {
            job.addJobRuntimeLogMessage(getMessage());

            log.debug(getMessage().build(Language.NERD)); // TODO: language?
        }
    }
}
