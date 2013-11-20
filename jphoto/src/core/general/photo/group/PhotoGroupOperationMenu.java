package core.general.photo.group;

public abstract class PhotoGroupOperationMenu {

	public abstract PhotoGroupOperationType getPhotoGroupOperation();

	public static final PhotoGroupOperationMenu ARRANGE_PHOTO_ALBUMS = new PhotoGroupOperationMenu() {

		@Override
		public PhotoGroupOperationType getPhotoGroupOperation() {
			return PhotoGroupOperationType.ARRANGE_PHOTO_ALBUMS;
		}
	};

	public static final PhotoGroupOperationMenu ARRANGE_TEAM_MEMBERS = new PhotoGroupOperationMenu() {

		@Override
		public PhotoGroupOperationType getPhotoGroupOperation() {
			return PhotoGroupOperationType.ARRANGE_TEAM_MEMBERS;
		}
	};

	public static final PhotoGroupOperationMenu DELETE_PHOTOS_MENU = new PhotoGroupOperationMenu() {

		@Override
		public PhotoGroupOperationType getPhotoGroupOperation() {
			return PhotoGroupOperationType.DELETE_PHOTOS;
		}
	};

	public static final PhotoGroupOperationMenu ARRANGE_NUDE_CONTENT_MENU = new PhotoGroupOperationMenu() {

		@Override
		public PhotoGroupOperationType getPhotoGroupOperation() {
			return PhotoGroupOperationType.ARRANGE_NUDE_CONTENT;
		}
	};

	public static final PhotoGroupOperationMenu MOVE_TO_GENRE_MENU = new PhotoGroupOperationMenu() {

		@Override
		public PhotoGroupOperationType getPhotoGroupOperation() {
			return PhotoGroupOperationType.MOVE_TO_GENRE;
		}
	};

	public static final PhotoGroupOperationMenu SEPARATOR_MENU = new PhotoGroupOperationMenu() {

		@Override
		public PhotoGroupOperationType getPhotoGroupOperation() {
			return PhotoGroupOperationType.SEPARATOR;
		}
	};

	@Override
	public int hashCode() {
		return getPhotoGroupOperation().hashCode();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof PhotoGroupOperationMenu ) ) {
			return false;
		}

		final PhotoGroupOperationMenu operationMenu = ( PhotoGroupOperationMenu ) obj;
		return operationMenu.getPhotoGroupOperation() == getPhotoGroupOperation();
	}

	@Override
	public String toString() {
		return String.format( "Photo group operation menu: %s", getPhotoGroupOperation() );
	}
}
