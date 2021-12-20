package com.bordozer.jphoto.ui.controllers.users.card.data;

import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.controllers.users.card.UserCardModel;
import com.bordozer.jphoto.ui.elements.PhotoList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class AlbumFillStrategy extends AbstractUserCardModelFillStrategy {

    public AlbumFillStrategy(final UserCardModel model, final UserCardModelFillService userCardModelFillService) {
        super(model, userCardModelFillService);
    }

    @Override
    public void performCustomActions() {
        userCardModelFillService.setPhotoAlbums(model);

        final List<UserPhotoAlbum> userPhotoAlbums = model.getUserPhotoAlbums();

        final List<PhotoList> userPhotoAlbumsPhotoLists = newArrayList();
        for (final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums) {
            final PhotoList photoList = userCardModelFillService.getUserPhotoAlbumLastPhotos(getUser(), userPhotoAlbum, EnvironmentContext.getCurrentUser())
                    .getPhotoList(userPhotoAlbum.getId(), 1, EnvironmentContext.getLanguage(), userCardModelFillService.getDateUtilsService().getCurrentTime());

            if (photoList.hasPhotos()) {
                userPhotoAlbumsPhotoLists.add(photoList);
            }
        }
        model.setUserPhotoAlbumsPhotoLists(userPhotoAlbumsPhotoLists);
        model.setUserPhotoAlbums(userCardModelFillService.getUserPhotoAlbums(getUserId()));
        model.setUserPhotosCountByAlbums(userCardModelFillService.setUserPhotosCountByAlbums(getUserId()));
    }
}
