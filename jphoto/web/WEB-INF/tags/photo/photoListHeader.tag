<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="photoList" required="true" type="ui.elements.PhotoList" %>

<div class="panel-heading">

	<h3 class="panel-title">

		<c:if test="${not empty photoList.photoListTitle}">
			${photoList.photoListTitle}
		</c:if>

		<c:if test="${not empty photoList.accessiblePhotoListViewModes}">
			<div style="float: right; width: 100px; text-align: right; padding-right: 10px;">
				<c:forEach var="viewMode" items="${photoList.accessiblePhotoListViewModes}">
					<a href="${viewMode.viewModeLink}"><html:img24 src="${viewMode.viewModeType.icon}" alt=""/></a>
					&nbsp;
				</c:forEach>
			</div>
		</c:if>

	</h3>

</div>
