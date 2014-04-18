<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="photoList" required="true" type="ui.elements.PhotoList" %>

<c:set var="photoListTitle" value="${photoList.photoListTitle}"/>
<c:set var="totalPhotos" value="${photoList.totalPhotos}"/>

<c:set var="groupOperationForm" value="groupOperationForm" />

<c:set var="photoGroupOperationMenues" value="<%=photoList.getPhotoGroupOperationMenuContainer().getGroupOperationMenus()%>" />
<c:set var="isGroupOperationEnabled" value="${not empty photoGroupOperationMenues}" />

<c:set var="formAction" value="" />
<c:if test="${isGroupOperationEnabled}">
	<c:set var="formAction" value="${eco:baseUrl()}/photos/groupOperations/" />
</c:if>

<eco:form action="${formAction}" formName="${groupOperationForm}">

	<icons:favoritesJS />

	<div class="photo-list-container">

		<div class="photo-list-title block-background block-border block-shadow">

			<c:if test="${not empty photoListTitle}">
				${photoListTitle}
			</c:if>

			<c:if test="${totalPhotos == 0}">
				${eco:translate(photoList.noPhotoText)}
			</c:if>

		</div>

		<c:forEach var="photoInfo" items="${photoList.photoInfos}" varStatus="status">

			<c:set var="photoId" value="${photoInfo.photo.id}" />

			<div class="photo-container photo-container-${photoId}">
				${photoId}
			</div>

		</c:forEach>

	</div>

</eco:form>