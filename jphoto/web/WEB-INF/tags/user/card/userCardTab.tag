<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="userCardTabDTOs" required="true" type="java.util.List" %>
<%@ attribute name="selectedTab" required="true" type="core.enums.UserCardTab" %>
<%@ attribute name="albums" required="true" type="java.util.List" %>

<c:set var="userCardLink" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserCardLink( user.getId() )%>"/>
<c:set var="userCardTabsLen" value="<%=userCardTabDTOs.size()%>"/>

<ul class="nav nav-tabs">

	<c:forEach var="userCardTabDTO" items="${userCardTabDTOs}">

		<c:set var="userCardTab" value="${userCardTabDTO.userCardTab}"/>
		<c:set var="isSelectedTab" value="${selectedTab == userCardTab}"/>

		<li id="tab-${userCardTabDTO.userCardTab.key}" class="${isSelectedTab ? "active" : ""}">

			<a href="${userCardLink}${userCardTab.key}/">${eco:translate(userCardTab.name)}
				<c:if test="${userCardTabDTO.itemsOnTab > 0}">( ${userCardTabDTO.itemsOnTab} )</c:if>
			</a>
		</li>

	</c:forEach>

</ul>