package com.bordozer.jphoto.core.services.photo.list.factory;

import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenuContainer;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.utils.sql.PhotoListQueryBuilder;

public abstract class PhotoListFactoryBest extends AbstractPhotoListFactoryBest {

    public PhotoListFactoryBest(final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services) {
        super(photoFilteringStrategy, accessor, services);
    }

    @Override
    public boolean showPaging() {
        return true;
    }

    @Override
    public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
        return services.getGroupOperationService().getPhotoListPhotoGroupOperationMenuContainer(accessor);
    }

    @Override
    public PhotoListQueryBuilder getBaseQuery() {
        return super.getBaseQuery();
    }
}
