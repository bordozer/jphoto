<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="userCard" tagdir="/WEB-INF/tags/user/card" %>
<%@ taglib prefix="userCardTabs" tagdir="/WEB-INF/tags/user/card/tabs" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<jsp:useBean id="userCardModel" type="ui.controllers.users.card.UserCardModel" scope="request"/>

<c:set var="user" value="${userCardModel.user}"/>
<c:set var="selectedUserCardTab" value="${userCardModel.selectedUserCardTab}"/>
<c:set var="editingUserDataIsAccessible" value="${userCardModel.editingUserDataIsAccessible}" />

<tags:page pageModel="${userCardModel.pageModel}">

	<userCard:userCardTab user="${user}" selectedTab="${userCardModel.selectedUserCardTab}" />

	<div class="floatleft" style="margin: 0 auto; margin-top: 30px;">

		<c:if test="${selectedUserCardTab == 'PERSONAL_DATA'}">
			<userCardTabs:userPersonalData user="${user}" editingUserDataIsAccessible="${editingUserDataIsAccessible}" lastUserActivityTime="${userCardModel.lastUserActivityTime}" entryMenu="${userCardModel.entryMenu}"/>

			<div class="user-card-block block-background block-border justify-font" style="width: 500px; margin-top: 20px;">
				<b>${eco:translate('User card: Member self description')}:</b>
				<br/>
				${eco:formatPhotoCommentText(user.selfDescription)}
			</div>

		</c:if>

		<c:if test="${selectedUserCardTab == 'PHOTOS_OVERVIEW'}">
			<userCardTabs:photosOverview user="${user}" userPhotosByGenres="${userCardModel.userPhotosByGenres}" photoLists="${userCardModel.photoLists}" />
		</c:if>

		<c:if test="${selectedUserCardTab == 'STATISTICS'}">
			<userCardTabs:statistics userCardModel="${userCardModel}" />
		</c:if>

		<c:if test="${selectedUserCardTab == 'TEAM'}">
			<div class="divPadd">
				<userCard:customPhotoLists photoLists="${userCardModel.userTeamMemberPhotoLists}"/>
			</div>
		</c:if>

		<c:if test="${selectedUserCardTab == 'ALBUMS'}">
			<div class="divPadd">
				<c:set var="userPhotosCountByAlbums" value="${userCardModel.userPhotosCountByAlbums}" />
				${eco:translate('User photo albums')}:
				<ul>
				<c:forEach var="userPhotoAlbum" items="${userCardModel.userPhotoAlbums}">
					<li><links:userPhotoAlbumPhotos userPhotoAlbum="${userPhotoAlbum}"/> - ${userPhotosCountByAlbums[userPhotoAlbum.id]} ${eco:translate('ROD PLURAL photos')}</li>
				</c:forEach>
				</ul>

				<userCard:customPhotoLists photoLists="${userCardModel.userPhotoAlbumsPhotoLists}"/>
			</div>
		</c:if>

		<c:if test="${selectedUserCardTab == 'ACTIVITY_STREAM'}">
			<div class="divPadd">
				<userCard:userActivityStream user="${user}" activities="${userCardModel.userLastActivities}" filterActivityTypeId="${userCardModel.filterActivityTypeId}" />
			</div>
		</c:if>

	</div>

	<div class="footerseparator"></div>

</tags:page>
