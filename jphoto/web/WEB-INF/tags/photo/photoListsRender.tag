<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoLists" required="true" type="java.util.List" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<c:set var="baseUrl" value="${eco:baseUrl()}" />

<script type="text/javascript" src="${baseUrl}/js/require-config.js.jsp"></script>
<script type="text/javascript" src="${baseUrl}/js/lib/front-end/require.js"></script>

<link rel="stylesheet" href="${baseUrl}/css/photo-list.css" type="text/css"/>

<tags:entryMenuJs />

<c:forEach var="photoList" items="${photoLists}">

	<%--<photo:photoList photoList="${photoList}" />--%>
	<photo:photoListPostponedLoading photoList="${photoList}" />

</c:forEach>