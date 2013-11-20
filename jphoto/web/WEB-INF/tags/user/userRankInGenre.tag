<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>
<%@ attribute name="rank" required="true" type="java.lang.Integer" %>

<c:if test="${rank == 0}">
	<html:img8 src="user_rank_icon_zero_16x16.png" alt="${eco:translate1('Zero rank in category \\\'$1\\\'', genre.name)}" />
</c:if>

<c:set var="absRank" value="${rank > 0 ? rank : -rank}" />

<c:forEach begin="1" end="${absRank}">

	<c:set var="title" value="${eco:translate2('The member has $1 rank in category \\\'$2\\\': $3', rank, genre.name)}" />

	<c:set var="icon" value="user_rank_icon_16x16.png" />
	<c:if test="${ rank < 0}">
		<c:set var="icon" value="user_negative_rank_icon_16x16.png" />
	</c:if>

	<html:img8 src="${icon}" alt="${title}" />

</c:forEach>
