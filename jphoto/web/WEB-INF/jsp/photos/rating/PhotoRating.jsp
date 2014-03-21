<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoRatingModel" type="controllers.photos.rating.PhotoRatingModel" scope="request"/>

<tags:page pageModel="${photoRatingModel.pageModel}">

	<tags:entryMenuJs />

	<photo:photoList photoList="${photoRatingModel.photoList}" />

	<form:form modelAttribute="photoRatingModel" method="POST">

		${eco:translate('Show photo rating by user rank')}
		<br />
		<form:radiobuttons path="selectedPhotoRatingRank" items="${photoRatingModel.photoRatingRanks}"  />
		<br />
		<html:submitButton id="submit" caption_t="Show rating" />

	</form:form>

</tags:page>