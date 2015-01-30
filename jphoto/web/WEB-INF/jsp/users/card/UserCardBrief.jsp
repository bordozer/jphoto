<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user/card" %>
<%@ taglib prefix="userCardTabs" tagdir="/WEB-INF/tags/user/card/tabs" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<jsp:useBean id="userCardModel" type="ui.controllers.users.card.UserCardModel" scope="request"/>

<c:set var="user" value="${userCardModel.user}"/>
<c:set var="editingUserDataIsAccessible" value="${userCardModel.editingUserDataIsAccessible}" />

<tags:page pageModel="${userCardModel.pageModel}">

	<tags:contextMenuJs />

	<div class="panel">

		<div class="panel-body">

			<div class="row row-bottom-padding-10">
				<user:userCardTab
						user="${user}"
						userCardTabDTOs="${userCardModel.userCardTabDTOs}"
						selectedTab="${userCardModel.selectedUserCardTab}"
						albums="${userCardModel.userPhotosCountByAlbums}"
						/>
			</div>

			<div class="row row-bottom-padding-10">
				<div class="col-lg-6">
					<userCardTabs:userPersonalData user="${user}" editingUserDataIsAccessible="${editingUserDataIsAccessible}" lastUserActivityTime="${userCardModel.lastUserActivityTime}" entryMenu="${userCardModel.entryMenu}" />
				</div>
				<div class="col-lg-6">
					<user:userPhotosByGenresList user="${user}" userCardGenreInfoMap="${userCardModel.userCardGenreInfoMap}"/>
				</div>
			</div>

			<div class="row">
				<photo:photoListsRender photoLists="${userCardModel.photoLists}" />
			</div>
		</div>

	</div>

	<tags:devMode>
		<input type="hidden" id="dev_mode_user_card" value="${user.id}">
	</tags:devMode>

</tags:page>
