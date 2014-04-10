<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="entryMenu" required="true" type="core.general.menus.EntryMenu" %>

<c:if test="${not empty entryMenu and not empty entryMenu.entryMenuItems}">

	<c:set var="menuId" value="${entryMenu.menuId}"/>
	<c:set var="entryMenuItems" value="${entryMenu.entryMenuItems}"/>
	<c:set var="menuDivId" value="${menuId}-items"/>

	<script type="text/javascript">
		$( function () {
			$( '#${menuId}' ).fgmenu( {
										content:$( '#${menuDivId}' ).html()
										  , showSpeed:400
										  , width:350
										  , maxHeight: ${entryMenu.menuHeight}
									} );
		} );
	</script>

	<%-- menu icon --%>
	<a tabindex="0" href="#" id="${menuId}" onclick="return false;">
		<c:set var="entryMenuTypeName" value="${eco:translate(entryMenu.entryMenuType.name)}"/>
		<html:img16 src="ui-menu-blue.png" alt="${eco:translate1('$1 menu', entryMenuTypeName)}"/>
	</a>
	<%-- / menu icon --%>

	<div id="${menuDivId}" class="entry-popup-menu">

		<div class="floatleft block-background entry-popup-menu-header">${entryMenu.menuTitle}</div>

		<tags:entryMenuRenderer entryMenu="${entryMenu}" />

	</div>

</c:if>