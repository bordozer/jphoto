<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user/card" %>
<%@ taglib prefix="userCardTabs" tagdir="/WEB-INF/tags/user/card/tabs" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<jsp:useBean id="userCardModel" type="controllers.users.card.UserCardModel" scope="request"/>

<c:set var="user" value="${userCardModel.user}"/>
<c:set var="editingUserDataIsAccessible" value="${userCardModel.editingUserDataIsAccessible}" />

<tags:page pageModel="${userCardModel.pageModel}">

	<tags:entryMenuJs />

	<user:userCardTab user="${user}" selectedTab="${userCardModel.selectedUserCardTab}" />

	<div style="width: 85%; margin: 0 auto;">

		<div style="float: left; width: 800px; padding-right: 20px;">
			<userCardTabs:userPersonalData user="${user}" editingUserDataIsAccessible="${editingUserDataIsAccessible}" lastUserActivityTime="${userCardModel.lastUserActivityTime}" entryMenu="${userCardModel.entryMenu}" />
		</div>

		<div style="float: left; width: 400px;">
			<user:userPhotosByGenresList user="${user}" genreInfoMap="${userCardModel.genreInfoMap}"/>
		</div>
	</div>

	<c:forEach var="photoList" items="${userCardModel.photoLists}">
		<photo:photoList photoList="${photoList}" />
	</c:forEach>

	<div class="footerseparator"></div>

	<tags:devMode>
		<input type="hidden" id="dev_mode_user_card" value="${user.id}">
	</tags:devMode>

</tags:page>
