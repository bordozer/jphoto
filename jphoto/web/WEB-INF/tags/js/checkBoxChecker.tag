<%@ tag import="java.util.Date" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="namePrefix" type="java.lang.String" required="true" %>
<%@ attribute name="uniqueId" type="java.lang.String" required="false" %>

<c:set var="iconId" value="checkAllIcon${uniqueId}"/>

<html:img id="${iconId}" src="icons16/notchecked.png" width="16" height="16" onclick="checkAll${uniqueId}();" alt="${eco:translate('Invert selection')}" />

<script type="text/javascript">

	var isChecked = false;

	jQuery().ready( function() {
		var icon = $( '#${iconId}' );
		icon.bind( 'click', checkAll${uniqueId} );

		icon.click( function() {
			if ( isChecked ) {
				icon.attr( 'src', '${eco:imageFolderURL()}/icons16/uncheckAll.png' );
				checkNone${uniqueId}();
			} else {
				icon.attr( 'src', '${eco:imageFolderURL()}/icons16/checkAll.png' );
				checkAll${uniqueId}();
			}
			isChecked = !isChecked;
		});
	});

	function checkAll${uniqueId}() {
		$( "[name*='${namePrefix}']" ).each( function () {
			$( this ).attr( "checked", "checked" );
		} );
	}

	function checkNone${uniqueId}() {
		$( "[name*='${namePrefix}']" ).each( function () {
			$( this ).removeAttr( 'checked' );
		} );
	}

</script>