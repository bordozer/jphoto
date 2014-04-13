package ui.controllers.users.card.data;

import core.general.user.userAlbums.UserPhotoAlbum;
import ui.controllers.users.card.UserCardModel;
import ui.elements.PhotoList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class AlbumFillStrategy extends AbstractUserCardModelFillStrategy {

	public AlbumFillStrategy( final UserCardModel model, final UserCardModelFillService userCardModelFillService ) {
		super( model, userCardModelFillService );
	}

	@Override
	public void performCustomActions() {
		userCardModelFillService.setPhotoAlbums( model );

		final List<UserPhotoAlbum> userPhotoAlbums = model.getUserPhotoAlbums();

		final List<PhotoList> userPhotoAlbumsPhotoLists = newArrayList();
		for ( final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums ) {
			final PhotoList photoList = userCardModelFillService.getUserPhotoAlbumLastPhotos( getUserId(), userPhotoAlbum, model.getUserPhotoAlbumsQtyMap() );

			if ( photoList.hasPhotos() ) {
				userPhotoAlbumsPhotoLists.add( photoList );
			}
		}
		model.setUserPhotoAlbumsPhotoLists( userPhotoAlbumsPhotoLists );
	}
}
