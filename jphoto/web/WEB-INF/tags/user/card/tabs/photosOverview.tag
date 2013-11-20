<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user/card" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="userPhotosByGenres" required="true" type="java.util.List" %>
<%@ attribute name="photoLists" required="true" type="java.util.List" %>

<photo:photosByUserByGenre user="${user}" userPhotosByGenres="${userPhotosByGenres}"/>

<c:forEach var="photoList" items="${photoLists}">

	<tags:entryMenuJs />

	<photo:photoList photoList="${photoList}"/>

</c:forEach>

<photo:photosByUserByGenre user="${user}" userPhotosByGenres="${userPhotosByGenres}"/>