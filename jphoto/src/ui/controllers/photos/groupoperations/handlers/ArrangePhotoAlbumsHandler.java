package ui.controllers.photos.groupoperations.handlers;

import ui.controllers.photos.groupoperations.GroupOperationResult;
import ui.controllers.photos.groupoperations.PhotoGroupOperationEntry;
import ui.controllers.photos.groupoperations.PhotoGroupOperationEntryProperty;
import ui.controllers.photos.groupoperations.PhotoGroupOperationModel;
import core.general.photo.Photo;
import core.general.photo.group.PhotoGroupOperationType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.Services;
import core.services.user.UserPhotoAlbumService;
import core.services.utils.EntityLinkUtilsService;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ArrangePhotoAlbumsHandler extends AbstractGroupOperationHandler {

	public ArrangePhotoAlbumsHandler( final PhotoGroupOperationModel model, final Services services ) {
		super( model, services );
	}

	@Override
	public PhotoGroupOperationType getPhotoGroupOperation() {
		return PhotoGroupOperationType.ARRANGE_PHOTO_ALBUMS;
	}

	@Override
	public void fillModel() {
		super.fillModel();

		model.setUserPhotoAlbums( services.getUserPhotoAlbumService().loadAllForEntry( getUserId() ) );
	}

	@Override
	protected void setGroupOperationEntryProperties( final PhotoGroupOperationEntry groupOperationEntry ) {
		final UserPhotoAlbumService userPhotoAlbumService = services.getUserPhotoAlbumService();
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

		final Photo photo = groupOperationEntry.getPhoto();
		final List<UserPhotoAlbum> userPhotoAlbums = services.getUserPhotoAlbumService().loadAllForEntry( getUserId() );

		final Map<String, PhotoGroupOperationEntryProperty> map = model.getPhotoGroupOperationEntryPropertiesMap();

		for ( final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums ) {
			final PhotoGroupOperationEntryProperty entryProperty = new PhotoGroupOperationEntryProperty( photo.getId(), userPhotoAlbum.getId(), entityLinkUtilsService.getUserPhotoAlbumPhotosLink( userPhotoAlbum, getLanguage() ) );
			entryProperty.setValue( userPhotoAlbumService.isPhotoInAlbum( photo.getId(), userPhotoAlbum.getId() ) );

			final String key = String.format( "%d_%d", photo.getId(), userPhotoAlbum.getId() );
			map.put( key, entryProperty );
		}

		model.setPhotoGroupOperationEntryPropertiesMap( map );
	}

	@Override
	protected List<GroupOperationResult> performPhotoOperation( final Photo photo, final PhotoGroupOperationEntryProperty entryProperty ) {

		final List<GroupOperationResult> operationResults = newArrayList();

		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
		final UserPhotoAlbumService userPhotoAlbumService = services.getUserPhotoAlbumService();

		final int photoAlbumId = entryProperty.getEntryId();

		final UserPhotoAlbum photoAlbum = userPhotoAlbumService.load( photoAlbumId );

		final String photoCardLink = entityLinkUtilsService.getPhotoCardLink( photo, getLanguage() );
		final String albumLink = entityLinkUtilsService.getUserPhotoAlbumPhotosLink( photoAlbum, getLanguage() );

		final boolean isAlbumChecked = entryProperty.isValue();
		final boolean isPhotoInAlbum = userPhotoAlbumService.isPhotoInAlbum( photo.getId(), photoAlbumId );

		if ( isAlbumChecked && !isPhotoInAlbum ) {
			operationResults.add( GroupOperationResult.successful( String.format( "Photo '%s' has been added to album '%s'", photoCardLink, albumLink ) ) );
			userPhotoAlbumService.addPhotoToAlbum( photo.getId(), photoAlbumId );
		}

		if ( !isAlbumChecked && isPhotoInAlbum ) {
			operationResults.add( GroupOperationResult.successful( String.format( "Photo '%s' has been removed from album '%s'", photoCardLink, albumLink ) ) );
			userPhotoAlbumService.deletePhotoFromAlbum( photo.getId(), photoAlbumId );
		}

		return operationResults;
	}
}
