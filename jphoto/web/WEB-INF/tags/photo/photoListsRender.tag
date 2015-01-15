<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoLists" required="true" type="java.util.List" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<c:set var="baseUrl" value="${eco:baseUrl()}"/>

<link rel="stylesheet" href="${baseUrl}/css/photo-list.css" type="text/css"/>

<c:forEach var="photoList" items="${photoLists}">

	<c:if test="${photoList.selectedPhotoListViewModeType == 'VIEW_MODE_PREVIEW'}">
		<photo:photoListLazyLoading photoList="${photoList}"/>
	</c:if>

	<c:if test="${photoList.selectedPhotoListViewModeType == 'VIEW_MODE_BIG_PREVIEW'}">
		<photo:photoListDetails photoList="${photoList}"/>
	</c:if>

	<div style="float: left; width: 100%; border-bottom: 1px dashed #848078; margin-bottom: 15px;"></div>

</c:forEach>