<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="entryId" required="false" type="java.lang.Integer" %>
<%@ attribute name="entryMenuType" required="false" type="core.general.menus.EntryMenuType" %>

<c:set var="container" value="entry-context-menu-${entryMenuType.id}-${entryId}" />

<div class="${container}" style="display: inline-block;">
	<a href="#" onclick="initContextMenu( ${entryId} ); return false;">
		<html:img16 src="ui-menu-blue.png" alt="${eco:translate('Entry Context menu hint')}"/>
	</a>
</div>

<script type="text/javascript">
	function initContextMenu( entryId ) {
		initPhotoContextMenu( entryId, ${entryMenuType.id}, $( '.${container}' ) );
	}
</script>