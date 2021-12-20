package com.bordozer.jphoto.ui.controllers.photos.groupoperations.handlers;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.GroupOperationResult;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntry;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntryProperty;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationModel;
import com.bordozer.jphoto.utils.NumberUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractGroupOperationHandler {

    public static final int ENTRY_ID = 1;

    protected final Services services;
    protected final PhotoGroupOperationModel model;

    protected AbstractGroupOperationHandler(final PhotoGroupOperationModel model, final Services services) {
        this.services = services;
        this.model = model;
    }

    protected abstract PhotoGroupOperationType getPhotoGroupOperation();

    protected void setPhotoOperationAllowance(final PhotoGroupOperationEntry groupOperationEntry) {
        setPhotoEditAccess(groupOperationEntry);
    }

    protected void setGroupOperationEntryProperties(final PhotoGroupOperationEntry groupOperationEntry) {
    }

    protected abstract List<GroupOperationResult> performPhotoOperation(final Photo photo, final PhotoGroupOperationEntryProperty entryProperty);

    public void fillModel() {
        model.setPhotoGroupOperationType(getPhotoGroupOperation());

        setPhotoGroupOperationEntries();
    }

    final public List<GroupOperationResult> performGroupOperations(final List<PhotoGroupOperationEntryProperty> selectedEntries) {
        final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

        final SecurityService securityService = services.getSecurityService();
        final User currentUser = getUser();

        final List<GroupOperationResult> operationResults = newArrayList();

        for (final Photo photo : getPhotos()) {

            if (!securityService.userCanDeletePhoto(currentUser, photo)) {
                operationResults.add(GroupOperationResult.error(String.format("You do not have permission to perform this operation with '%s'.", entityLinkUtilsService.getPhotoCardLink(photo, currentUser.getLanguage()))));
                continue;
            }

            for (final PhotoGroupOperationEntryProperty entryProperty : selectedEntries) {

                if (entryProperty == null) {
                    continue;
                }

                if (entryProperty.getPhotoId() != photo.getId()) {
                    continue;
                }

                operationResults.addAll(performPhotoOperation(photo, entryProperty));
            }
        }

        return operationResults;
    }

    public static AbstractGroupOperationHandler getInstance(final PhotoGroupOperationType groupOperationType, final PhotoGroupOperationModel model, final Services services) {

        switch (groupOperationType) {
            case ARRANGE_PHOTO_ALBUMS:
                return new ArrangePhotoAlbumsHandler(model, services);
            case ARRANGE_TEAM_MEMBERS:
                return new ArrangeTeamMembersHandler(model, services);
            case DELETE_PHOTOS:
                return new DeletePhotosHandler(model, services);
            case ARRANGE_NUDE_CONTENT:
                return new ArrangeNudeContentHandler(model, services);
            case MOVE_TO_GENRE:
                return new MoveToGenreHandler(model, services);
        }

        throw new IllegalArgumentException(String.format("Illegal PhotoGroupOperation: %s", groupOperationType));
    }

    final protected void setPhotoGroupOperationEntries() {
        final PhotoService photoService = services.getPhotoService();
        final UserPhotoFilePathUtilsService userPhotoFilePathUtilsService = services.getUserPhotoFilePathUtilsService();

        final List<PhotoGroupOperationEntry> photoGroupOperationEntries = newArrayList();

        for (final String _photoId : model.getSelectedPhotoIds()) {
            final int photoId = NumberUtils.convertToInt(_photoId);
            if (!photoService.exists(photoId)) {
                continue;
            }

            final Photo photo = photoService.load(photoId);

            final PhotoGroupOperationEntry groupOperationEntry = new PhotoGroupOperationEntry(photo);
            groupOperationEntry.setPhotoPreviewImgUrl(userPhotoFilePathUtilsService.getPhotoPreviewUrl(photo));

            setPhotoOperationAllowance(groupOperationEntry);

            if (groupOperationEntry.isGroupOperationAccessible()) {
                setGroupOperationEntryProperties(groupOperationEntry);
            }

            photoGroupOperationEntries.add(groupOperationEntry);
        }

        model.setPhotoGroupOperationEntries(photoGroupOperationEntries);
    }

    protected List<Photo> getPhotos() {
        final List<Photo> result = newArrayList();

        for (final PhotoGroupOperationEntry photoGroupOperationEntry : model.getPhotoGroupOperationEntries()) {
            result.add(photoGroupOperationEntry.getPhoto());
        }

        return result;
    }

    protected int getUserId() {
        return getUser().getId();
    }

    protected User getUser() {
        return EnvironmentContext.getCurrentUser();
    }

    protected String getDefaultEntryKey(final Photo photo) {
        return String.format("%d_%d", photo.getId(), ENTRY_ID);
    }

    protected TranslatorService getTranslatorService() {
        return services.getTranslatorService();
    }

    private void setPhotoEditAccess(final PhotoGroupOperationEntry groupOperationEntry) {
        final SecurityService securityService = services.getSecurityService();

        final Photo photo = groupOperationEntry.getPhoto();
        if (!securityService.userCanEditPhoto(getUser(), photo)) {
            groupOperationEntry.setPhotoOperationAllowanceMessage(getTranslatorService().translate("You do not have permission to edit this photo", getLanguage()));
            groupOperationEntry.setGroupOperationAccessible(false);
        }
    }

    protected Language getLanguage() {
        return getUser().getLanguage();
    }
}
