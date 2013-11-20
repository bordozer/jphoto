<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="photoLists" required="true" type="java.util.List" %>

<c:set var="hasAtLeastNotEmptyPhotoList" value="${fn:length(photoLists) > 0}"/>

<c:forEach var="userTeamMemberPhotoList" items="${photoLists}">

	<tags:entryMenuJs />

	<photo:photoList photoList="${userTeamMemberPhotoList}"/>

</c:forEach>

<c:if test="${not hasAtLeastNotEmptyPhotoList}">
	<div style="text-align: center;">
		<h3>${eco:translate("No items")}</h3>
	</div>
</c:if>