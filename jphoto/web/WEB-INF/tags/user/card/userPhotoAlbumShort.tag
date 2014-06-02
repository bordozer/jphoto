<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="userPhotoAlbums" required="true" type="java.util.List" %>
<%@ attribute name="userPhotoAlbumsQtyMap" required="true" type="java.util.Map" %>

<c:set var="cssClass" value="textright"/>
<c:set var="width" value="50%"/>

<table:table width="100%" oddEven="true">

	<table:separatorInfo colspan="3" title="${eco:translate('User Statistics: Tab: Photo albums')}"/>

	<c:if test="${not empty userPhotoAlbums}">

		<c:forEach var="userPhotoAlbum" items="${userPhotoAlbums}">

			<table:tr>

				<table:td cssClass="${cssClass}" width="${width}">
					<links:userPhotoAlbumPhotos userPhotoAlbum="${userPhotoAlbum}"/>
				</table:td>

				<table:td>
					${userPhotoAlbumsQtyMap[userPhotoAlbum]}
				</table:td>

			</table:tr>

		</c:forEach>

	</c:if>

	<c:if test="${empty userPhotoAlbums}">
		<table:tr>
			<table:td colspan="2" cssClass="text-centered">
				${eco:translate('No albums')}
			</table:td>
		</table:tr>
	</c:if>

</table:table>