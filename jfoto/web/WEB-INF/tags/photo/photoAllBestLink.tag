<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="linkToFullList" required="true" type="java.lang.String" %>
<%@ attribute name="linkToFullListText" required="true" type="java.lang.String" %>

<div style="float: right; width: 15%; text-align: right;">
	<c:if test="${not empty linkToFullList}">

		<c:set var="text" value="${linkToFullListText}" />

		<a href="${linkToFullList}" title="${eco:translate('Show all photos of the list')}">
			${text} &nbsp; <html:img id="forward" src="forward16x16.png" width="16" height="16" alt="${text}" />
		</a>

	</c:if>
</div>
