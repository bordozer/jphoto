package menuItems.photo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractGoToAuthorPhotosTest_ extends AbstractPhotoMenuItemTest_ {

//	protected abstract ServicesImpl getServicesGoTo( final GoToParameters goToParameters );

	protected void assertT( final boolean accessibleFor ) {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, accessibleFor );
	}

	protected void assertF( final boolean accessibleFor ) {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, accessibleFor );
	}
}
