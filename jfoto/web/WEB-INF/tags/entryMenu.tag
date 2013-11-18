<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="entryMenu" required="true" type="core.general.menus.EntryMenu" %>

<c:if test="${not empty entryMenu and not empty entryMenu.entryMenuItems}">

	<c:set var="menuId" value="${entryMenu.menuId}"/>
	<c:set var="entryMenuItems" value="${entryMenu.entryMenuItems}"/>
	<c:set var="menuItems" value="${menuId}-items"/>

	<script type="text/javascript">
		$( function () {
			$( '#${menuId}' ).menuA( {
										content:$( '#${menuItems}' ).html(), showSpeed:400, width:350
									} );
		} );
	</script>

	<a tabindex="0" href="#" id="${menuId}" onclick="return false;">
		<html:img16 src="ui-menu-blue.png" alt="${eco:translate1('$1 menu', entryMenu.entryMenuType.nameTranslated)}"/>
	</a>

	<div id="${menuItems}" style="position:absolute; top:0; left:-9999px; width:1px; height:1px; overflow:hidden;">

		<div class="floatleft block-background" style="text-align: center; font-size: 60%; padding-top: 3px; padding-bottom: 3px; margin-bottom: 5px;">
			${entryMenu.entryMenuType.nameTranslated}: #${entryMenu.entryId}
		</div>

		<ul>
			<c:forEach var="entryMenuItem" items="${entryMenuItems}">

				<c:set var="isSeparator" value="${entryMenuItem.entryMenuType == 'SEPARATOR'}"/>

				<c:if test="${isSeparator}">
					<div class="floatleft block-background" style="height: 2px; margin: 2px;"></div>
				</c:if>

				<c:if test="${not isSeparator}">
					<li style="font-size: 70%;">
						<a href="#" onclick="${entryMenuItem.menuItemCommand.menuCommand}">
							<html:img12 src="${entryMenuItem.menuItemCommand.commandIcon}" alt="${eco:translate('Menu')}"/>
							&nbsp;
							${entryMenuItem.menuItemCommand.menuText}
						</a>
					</li>
				</c:if>

			</c:forEach>
		</ul>

	</div>
</c:if>