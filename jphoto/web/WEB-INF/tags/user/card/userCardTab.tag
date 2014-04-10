<%@ tag import="core.enums.UserCardTab" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="selectedTab" required="true" type="core.enums.UserCardTab" %>

<c:set var="userCardLink" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserCardLink( user.getId() )%>" />
<c:set var="userCardTabs" value="<%=UserCardTab.values()%>" />
<c:set var="userCardTabsLen" value="<%=UserCardTab.values().length%>" />

<style type="text/css">
	.userCardTab {
		float: left;
		width: ${eco:floor(99 / userCardTabsLen)}%;
		padding-top: 10px;
		padding-bottom: 10px;
		text-align: center;
		height: 19px;
		border-radius: 12px 12px 0 0;
	}

</style>

<div class="block-border tabHeader">

	<c:forEach var="userCardTab" items="${userCardTabs}">

		<c:set var="isSelectedTab" value="${selectedTab == userCardTab}" />

		<div class="userCardTab block-border${isSelectedTab ? " block-background" : ""}">
			<a href="${userCardLink}${userCardTab.key}/">${eco:translate(userCardTab.name)}</a>
		</div>

	</c:forEach>

</div>