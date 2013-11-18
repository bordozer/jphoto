<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user/card" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="editingUserDataIsAccessible" required="true" type="java.lang.Boolean" %>
<%@ attribute name="lastUserActivityTime" required="true" type="java.util.Date" %>
<%@ attribute name="entryMenu" required="true" type="core.general.menus.EntryMenu" %>

<div class="floatleft">
	<div class="floatleft" style="padding-bottom: 20px;">

		<user:userCardAvatar user="${user}" userAvatar="${userCardModel.userAvatar}" isEditable="${editingUserDataIsAccessible}"/>

		<div style="float: right; width: 500px;">
			<user:userInfo user="${user}" lastUserActivityTime="${lastUserActivityTime}" entryMenu="${entryMenu}"/>
		</div>

	</div>
</div>