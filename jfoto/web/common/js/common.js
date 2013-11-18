function openPopupWindow( url, width, height ) {
	window.open( url, '', 'height=' + height + ',width=' + width );
}

function openPopupWindowCustom( ow_win_url, ow_win_name, ow_width_x, ow_height_y, ow_pos_x, ow_pos_y ) {

	var ow_str = "width=" + ow_width_x + ",height=" + ow_height_y + ",left=" + ow_pos_x + ",top=" + ow_pos_y + ",resizable=yes,scrollbars=yes,menubar=no,toolbar=no,directories=no,location=no,status=no";

	var ow_win = window.open( ow_win_url, ow_win_name, ow_str );
	if ( null != ow_win ) {
		ow_win.focus();
	}
}