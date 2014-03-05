<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<a href="#" onclick="openAnonymousDaysPopup(); return false;">${eco:translate('Anonymous posting schedule')}</a>

<script type="text/javascript">

	function openAnonymousDaysPopup( ow_win_url, ow_win_name, ow_width_x, ow_height_y, ow_pos_x, ow_pos_y ) {
		openPopupWindowCustom( "${eco:baseUrlWithPrefix()}/anonymousDays/", "AnonymousDays", 450, 450, 100, 100 );
	}

</script>