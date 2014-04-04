<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="core.services.utils.DateUtilsServiceImpl" %>
<%@ tag import="core.enums.FavoriteEntryType" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.services.system.ConfigurationService" %>
<%@ tag import="core.general.configuration.ConfigurationKey" %>
<%@ tag import="ui.controllers.photos.groupoperations.PhotoGroupOperationModel" %>
<%@ tag import="core.services.utils.DateUtilsService" %>
<%@ tag import="java.util.Date" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ attribute name="photoInfo" required="true" type="core.general.photo.PhotoInfo" %>
<%@ attribute name="isGroupOperationEnabled" required="true" type="java.lang.Boolean" %>
<%@ attribute name="sortColumnNumber" required="false" type="java.lang.Integer" %>

<c:set var="photo" value="${photoInfo.photo}"/>

<%
	final ConfigurationService configurationService = ApplicationContextHelper.getBean( ConfigurationService.BEAN_NAME );
	final DateUtilsService dateUtilsService = ApplicationContextHelper.getBean( DateUtilsService.BEAN_NAME );
	final Date currentTime = dateUtilsService.getCurrentTime();
%>

<c:set var="days" value="<%=configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS )%>"/>

<c:set var="user" value="${photoInfo.user}"/>
<c:set var="loggedUser" value="<%=EnvironmentContext.getCurrentUser()%>"/>
<c:set var="isUserOwnedPhoto" value="${loggedUser.id == user.id}"/>

<c:set var="currentTime" value="<%=currentTime%>"/>
<c:set var="todaysDate" value="<%=dateUtilsService.formatDate( currentTime )%>"/>
<c:set var="isPhotoUploadedToday" value="${photo.uploadTime.time == currentTime.time}"/>

<c:set var="photoId" value="${photo.id}"/>

<c:set var="favoriteEntryTypePhoto" value="<%=FavoriteEntryType.FAVORITE_PHOTOS%>"/>
<c:set var="favoriteEntryTypeBookmark" value="<%=FavoriteEntryType.BOOKMARKED_PHOTOS%>"/>
<c:set var="newCommentsNotificationEntryType" value="<%=FavoriteEntryType.NEW_COMMENTS_NOTIFICATION%>"/>

<c:set var="controlSelectedPhotoIds" value="<%=PhotoGroupOperationModel.FORM_CONTROL_SELECTED_PHOTO_IDS%>"/>

<c:set var="containerCss" value="containerPhotoMain block-border block-shadow block-background"/>
<c:if test="${isUserOwnedPhoto}">
	<c:set var="containerCss" value="${containerCss} block-user-photo"/>
</c:if>

<c:set var="showAnonumousFlag" value="${photo.anonymousPosting and ( photoInfo.superAdminUser or isUserOwnedPhoto )}"/>
<c:set var="showNudeContentSight" value="${photoInfo.superAdminUser && photoInfo.photo.containsNudeContent}"/>

<div class="${containerCss}">

	<div class="containerPhotoGenre" style="font-size: 10px;">
		<links:genrePhotos genre="${photoInfo.genre}" />
	</div>

	<div class="containerPhotoCkeckbox">
		<c:if test="${isGroupOperationEnabled}">
			<html:checkbox inputId="${controlSelectedPhotoIds}" inputValue="${photoId}" />
		</c:if>
		<span class="photoUploadDate">
			<c:if test="${isPhotoUploadedToday}">
				<links:photosOnDate uploadTime="${photo.uploadTime}" text="${eco:translate('Today')}" />
				${eco:formatTimeShort(photo.uploadTime)}
			</c:if>
			<c:if test="${not isPhotoUploadedToday}">
				<links:photosOnDate uploadTime="${photo.uploadTime}" />
				&nbsp;${eco:formatTimeShort(photo.uploadTime)}
			</c:if>
		</span>
	</div>

	<div class="containerPhotoImg">
		<links:photoCard id="${photoId}">
			<c:if test="${not photoInfo.photoPreviewHasToBeHiddenBecauseOfNudeContent}">
				<img src="${photoInfo.photoPreviewImgUrl}" class="photopreview"/>
			</c:if>
			<c:if test="${photoInfo.photoPreviewHasToBeHiddenBecauseOfNudeContent}">
				<photo:nudeContentPreview />
			</c:if>
		</links:photoCard>
	</div>

	<div class="containerPhotoLine">
		<div style="text-align: center;">

			<c:forEach var="favoriteEntryType" items="${photoInfo.photoIconsTypes}">
				<icons:favoritesPhoto photo="${photo}" entryType="${favoriteEntryType}"/>
			</c:forEach>

			<tags:entryMenu entryMenu="${photoInfo.photoMenu}" />

		</div>
	</div>

	<div class="containerPhotoLine">

		<c:set var="maxPhotoNameLenght" value="25"/>
		<c:set var="photoNameLenght" value="${fn:length(photo.name)}"/>
		<c:if test="${photoNameLenght <= maxPhotoNameLenght}">
			<photo:photoCard photo="${photo}" />
		</c:if>

		<c:if test="${photoNameLenght > maxPhotoNameLenght}">
			<links:photoCard id="${photo.id}" title="${photo.nameEscaped}">
				${eco:escapeHtml(fn:substring(photo.name, 0, maxPhotoNameLenght))}...
			</links:photoCard>
		</c:if>

	</div>

	<c:if test="${photoInfo.showStatisticInPhotoList}">
		<div class="containerPhotoLine" style="font-size: 90%;">
			<span class="photoPreviewMarks ${sortColumnNumber == 1 ? 'underlined' : ''}" title="${eco:translate1('Today\'s marks', days )}">${photoInfo.todayMarks}</span>
			/
			<span class="photoPreviewMarks ${sortColumnNumber == 2 ? 'underlined' : ''}" title="${eco:translate1('Marks for last $1 days', days )}">${photoInfo.topBestMarks}</span>
			/
			<span class="photoPreviewMarks ${sortColumnNumber == 3 ? 'underlined' : ''}"><links:photoMarkList photo="${photo}">${photoInfo.totalMarks}</links:photoMarkList></span>

			<c:if test="${photoInfo.showPhotoRatingPosition}">
			/ <span class="photoPreviewMarks" style="color: green" title="${photoInfo.photoRatingPositionDescription}">#${photoInfo.photoRatingPosition}</span>
			</c:if>

			&nbsp;&nbsp;&nbsp; <html:img8 src="photo_preview_views_icon.png" alt="${eco:translate1('Previews: $1', photoInfo.previewCount)}" /> <links:photoPreviewsList photoInfo="${photoInfo}"/>

			<c:set var="commantsHint" value="${eco:translate1('Comments: $1', photoInfo.commentsCount)}"/>
			/ <html:img8 src="photo_preview_comments_icon.png" alt="${commantsHint}" />: <span title="${commantsHint}">${photoInfo.commentsCount}</span>
		</div>
	</c:if>

	<c:if test="${not photoInfo.photoAuthorNameMustBeHidden}">
		<div class="containerPhotoLine" style="font-size: 80%; margin-top: 5px;">
			<c:set var="maxUserNameLenght" value="32"/>
			<c:set var="userNameLenght" value="${fn:length(user.name)}"/>
			<c:if test="${userNameLenght <= maxUserNameLenght}">
				<user:userCard user="${user}" />
			</c:if>

			<c:if test="${userNameLenght > maxUserNameLenght}">
				<links:userCard  id="${user.id}" title="${user.nameEscaped}">
					${eco:escapeHtml(fn:substring(user.name, 0, maxUserNameLenght))}...
				</links:userCard>
			</c:if>

			<%--<icons:userIcons user="${user}"
							 hideIconToFavoriteUser="false"
							 hideIconToFriend="false"
							 hideIconNewPhotoNotification="false"
							 hideIconToBlackList="true"
							 hideIconSendPrivateMessage="true"
					/>--%>
		</div>
		<c:if test="${photoInfo.showUserRankInGenreInPhotoList}">
			<div class="containerPhotoLine">
				<user:userRankInGenreRenderer userRankIconContainer="${photoInfo.userRankWhenPhotoWasUploadedIconContainer}"/>
			</div>
		</c:if>
	</c:if>

	<c:if test="${photoInfo.photoAuthorNameMustBeHidden}">
		<div class="containerPhotoLine small-text note-text-color" style="padding-top: 7px;">
			${photoInfo.photoAuthorAnonymousName}
			<br />
			${eco:translate('Anonymous till')} ${eco:formatDate(photoInfo.photoAnonymousPeriodExpirationTime)} ${eco:formatTimeShort(photoInfo.photoAnonymousPeriodExpirationTime)}
		</div>
	</c:if>

	<div style="float: left; width: 100%; height: 7px; padding-bottom: 5px;">
		<c:if test="${showNudeContentSight}">
			<div class="adminSpecialFlag"  style="border-color: #ff13e9;" title="${eco:translate('The photo is marked as \'Contains nude content\'')}"></div>
		</c:if>
		<c:if test="${showAnonumousFlag}">
			<div class="adminSpecialFlag" style="border-color: #111111;" title="${eco:translate('The photo is posted anonymously')}"></div>
		</c:if>
	</div>

</div>

