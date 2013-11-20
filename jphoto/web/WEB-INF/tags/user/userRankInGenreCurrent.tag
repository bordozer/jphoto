<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>
<%@ attribute name="rank" required="true" type="java.lang.Integer" %>
<%@ attribute name="userHasEnoughPhotos" required="true" type="java.lang.Boolean" %>

<c:if test="${userHasEnoughPhotos && rank == 0}">
	<html:img id="userrankIn${genre.id}" src="user_rank_icon_zero_16x16.png" width="8" height="8" alt="${eco:translate1('Author has zero rank in category \\\'$1\\\'', genre.name)}" />
</c:if>

<c:if test="${not userHasEnoughPhotos}">
	<c:set var="title" value="${eco:translate1('The member does not have enough photos in category \\\'$1\\\'', genre.name)}" />
	<html:img id="userrankIn${genre.id}" src="user_rank_icon_disabled16x16.png" width="8" height="8" alt="${title}" />
</c:if>

<c:set var="absRank" value="${rank > 0 ? rank : -rank}" />

<c:forEach begin="1" end="${absRank}">

	<c:set var="title" value="${eco:translate2('Current rank in category \\\'$1\\\': $2', genre.name, rank)}" />

	<c:set var="icon" value="user_rank_icon_16x16.png" />
	<c:if test="${ rank < 0}">
		<c:set var="icon" value="user_negative_rank_icon_16x16.png" />
	</c:if>

	<html:img8 src="${icon}" alt="${title}" />

</c:forEach>
