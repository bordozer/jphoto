package ui.controllers.photos.list.title;

import org.apache.commons.lang.StringUtils;

public class EmptyPhotoListTitle extends AbstractPhotoListTitle {

	public EmptyPhotoListTitle() {
		super( null, null );
	}

	@Override
	public String getPhotoListTitle() {
		return StringUtils.EMPTY;
	}
}
