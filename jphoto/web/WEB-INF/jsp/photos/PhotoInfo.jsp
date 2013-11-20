<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="photoInfoModel" type="controllers.photos.card.PhotoInfoModel" scope="request"/>

<c:set var="photo" value="${photoInfoModel.photoInfo.photo}" />

<tags:pageLight title="${eco:translate1('$1 - photo info', photo.name)}">

	<div style="float: left; width: 100%; margin-top: 10px;">
		<photo:photoInfo photoInfo="${photoInfoModel.photoInfo}" votingModel="${photoInfoModel.votingModel}" hideAuthorIconsAndMenu="true" />
	</div>

</tags:pageLight>
