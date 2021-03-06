package com.bordozer.jphoto.ui.controllers.photos.groupoperations.handlers;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationType;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.GroupOperationResult;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntry;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationEntryProperty;
import com.bordozer.jphoto.ui.controllers.photos.groupoperations.PhotoGroupOperationModel;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ArrangePhotoAlbumsHandler extends AbstractGroupOperationHandler {

    public ArrangePhotoAlbumsHandler(final PhotoGroupOperationModel model, final Services services) {
        super(model, services);
    }

    @Override
    public PhotoGroupOperationType getPhotoGroupOperation() {
        return PhotoGroupOperationType.ARRANGE_PHOTO_ALBUMS;
    }

    @Override
    public void fillModel() {
        super.fillModel();

        model.setUserPhotoAlbums(services.getUserPhotoAlbumService().loadAllForEntry(getUserId()));
    }

    @Override
    protected void setGroupOperationEntryProperties(final PhotoGroupOperationEntry groupOperationEntry) {
        final UserPhotoAlbumService userPhotoAlbumService = services.getUserPhotoAlbumService();
        final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

        final Photo photo = groupOperationEntry.getPhoto();
        final List<UserPhotoAlbum> userPhotoAlbums = services.getUserPhotoAlbumService().loadAllForEntry(getUserId());

        final Map<String, PhotoGroupOperationEntryProperty> map = model.getPhotoGroupOperationEntryPropertiesMap();

        for (final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums) {
            final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty(photo.getId(), userPhotoAlbum.getId(), userPhotoAlbum.getName());
            entryProperty.setValue(userPhotoAlbumService.isPhotoInAlbum(photo.getId(), userPhotoAlbum.getId()));

            final String key = String.format("%d_%d", photo.getId(), userPhotoAlbum.getId());
            map.put(key, entryProperty);
        }

        model.setPhotoGroupOperationEntryPropertiesMap(map);
    }

    @Override
    protected List<GroupOperationResult> performPhotoOperation(final Photo photo, final PhotoGroupOperationEntryProperty entryProperty) {

        final List<GroupOperationResult> operationResults = newArrayList();

        final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
        final UserPhotoAlbumService userPhotoAlbumService = services.getUserPhotoAlbumService();

        final int photoAlbumId = entryProperty.getEntryId();

        final UserPhotoAlbum photoAlbum = userPhotoAlbumService.load(photoAlbumId);

        final String photoCardLink = entityLinkUtilsService.getPhotoCardLink(photo, getLanguage());
        final String albumLink = entityLinkUtilsService.getUserPhotoAlbumPhotosLink(photoAlbum, getLanguage());

        final boolean isAlbumChecked = entryProperty.isValue();
        final boolean isPhotoInAlbum = userPhotoAlbumService.isPhotoInAlbum(photo.getId(), photoAlbumId);

        if (isAlbumChecked && !isPhotoInAlbum) {
            operationResults.add(GroupOperationResult.successful(String.format("Photo '%s' has been added to album '%s'", photoCardLink, albumLink)));
            userPhotoAlbumService.addPhotoToAlbum(photo.getId(), photoAlbumId);
        }

        if (!isAlbumChecked && isPhotoInAlbum) {
            operationResults.add(GroupOperationResult.successful(String.format("Photo '%s' has been removed from album '%s'", photoCardLink, albumLink)));
            userPhotoAlbumService.deletePhotoFromAlbum(photo.getId(), photoAlbumId);
        }

        return operationResults;
    }
}
