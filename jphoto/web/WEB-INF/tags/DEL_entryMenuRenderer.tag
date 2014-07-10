<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="entryMenu" required="true" type="ui.services.menu.entry.items.EntryMenu" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<c:set var="menuId" value="${entryMenu.menuId}"/>
<c:set var="entryMenuItems" value="${entryMenu.entryMenuItems}"/>
<c:set var="menuDivId" value="${menuId}-items"/>

<ul class='top-menu-item'>

	<c:forEach var="entryMenuItem" items="${entryMenuItems}">

		<c:set var="isSeparator" value="${entryMenuItem.entryMenuType == 'SEPARATOR'}"/>

		<c:if test="${isSeparator}">
			<li>
				<div class="floatleft block-background" style="height: 2px; margin: 2px; width: 95%;"></div>
			</li>
		</c:if>

		<c:if test="${not isSeparator}">

			<li style="font-size: 10px;">

				<a class="${entryMenuItem.menuCssClass}" href="#" onclick="${entryMenuItem.menuItemCommand.menuCommand}">
					<html:img12 src="${entryMenuItem.commandIcon}" alt="${entryMenuItem.menuItemCommand.menuText}"/>
					&nbsp;
					${entryMenuItem.menuItemCommand.menuText}
				</a>

				<c:if test="${entryMenuItem.subMenu}">
					<tags:DEL_entryMenuRenderer entryMenu="${entryMenuItem.entrySubMenu}"/>
				</c:if>

			</li>

		</c:if>

	</c:forEach>
</ul>

