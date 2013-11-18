package core.general.data.photoList;

import sql.builder.SqlIdsSelectQuery;

public class PhotoListData extends AbstractPhotoListData {

	public PhotoListData( final SqlIdsSelectQuery photoListQuery ) {
		super( photoListQuery );
	}

	@Override
	public boolean isGroupOperationEnabled() {
		if ( photoGroupOperationMenuContainer == null ) {
			return false;
		}

		if ( photoGroupOperationMenuContainer.getGroupOperationMenus() == null ) {
			return false;
		}

		return photoGroupOperationMenuContainer.getGroupOperationMenus().size() > 0;
	}
}
