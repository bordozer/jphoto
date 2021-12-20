package com.bordozer.jphoto.ui.controllers.photos.groupoperations.handlers;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.GroupOperationResult;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntry;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntryProperty;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationModel;
import com.bordozer.jphoto.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class DeletePhotosHandler extends AbstractGroupOperationHandler {

    public DeletePhotosHandler(final PhotoGroupOperationModel model, final Services services) {
        super(model, services);
    }

    @Override
    public PhotoGroupOperationType getPhotoGroupOperation() {
        return PhotoGroupOperationType.DELETE_PHOTOS;
    }

    @Override
    public void fillModel() {
        super.fillModel();
    }

    @Override
    protected void setPhotoOperationAllowance(final PhotoGroupOperationEntry groupOperationEntry) {
        final SecurityService securityService = services.getSecurityService();

        if (!securityService.userCanDeletePhoto(getUser(), groupOperationEntry.getPhoto())) {
            groupOperationEntry.setPhotoOperationAllowanceMessage(getTranslatorService().translate("You do not have permission to delete this photo", getLanguage()));
            groupOperationEntry.setGroupOperationAccessible(false);
        }
    }

    @Override
    protected void setGroupOperationEntryProperties(final PhotoGroupOperationEntry groupOperationEntry) {
        final Photo photo = groupOperationEntry.getPhoto();

        final Map<String, PhotoGroupOperationEntryProperty> map = model.getPhotoGroupOperationEntryPropertiesMap();

        final String key = getDefaultEntryKey(photo);
        final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty(photo.getId(), ENTRY_ID, getTranslatorService().translate("Group deletion: To be deleted check box", getLanguage()));
        entryProperty.setValue(true);

        map.put(key, entryProperty);

        model.setPhotoGroupOperationEntryPropertiesMap(map);
    }

    @Override
    protected List<GroupOperationResult> performPhotoOperation(final Photo photo, final PhotoGroupOperationEntryProperty entryProperty) {
        final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

        final List<GroupOperationResult> operationResults = newArrayList();

        final boolean isSelectedForDeletion = entryProperty.isValue();
        if (!isSelectedForDeletion) {
            operationResults.add(GroupOperationResult.skipped(String.format("Photo '%s' is skipped", entityLinkUtilsService.getPhotoCardLink(photo, getLanguage()))));

            return operationResults;
        }

        final PhotoService photoService = services.getPhotoService();

        final String photoNameEscaped = StringUtilities.escapeHtml(photo.getName());

        if (photoService.delete(photo.getId())) {
            operationResults.add(GroupOperationResult.successful(String.format("Photo '%s' has been deleted", photoNameEscaped)));
        } else {
            operationResults.add(GroupOperationResult.error(String.format("Photo '%s' has NOT been deleted because of error", photoNameEscaped)));
        }

        return operationResults;
    }
}
