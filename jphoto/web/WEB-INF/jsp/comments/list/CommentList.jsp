<%@ page import="utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="comments" tagdir="/WEB-INF/tags/photo/comments" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:useBean id="photoCommentsListModel" type="controllers.comment.list.PhotoCommentsListModel" scope="request"/>
<jsp:useBean id="pagingModel" type="core.general.base.PagingModel" scope="request"/>

<%
	final boolean userEqualsToLoggedUser = UserUtils.isTheUserThatWhoIsCurrentUser( photoCommentsListModel.getUser() );
%>

<c:set var="isLoggedUserLokingOnOwnUnreadMessages" value="<%=userEqualsToLoggedUser%>" />

<tags:page pageModel="${photoCommentsListModel.pageModel}">

	<icons:favoritesJS/>
	<tags:entryMenuJs />

	<c:set var="user" value="${photoCommentsListModel.user}" />
	<c:set var="photoCommentInfoMap" value="${photoCommentsListModel.photoCommentInfoMap}" />
	<c:set var="noComments" value="${pagingModel.totalItems == 0}" />

	<c:set var="showPaging" value="${photoCommentsListModel.showPaging || not isLoggedUserLokingOnOwnUnreadMessages}" />
	<c:set var="showNextButton" value="${not photoCommentsListModel.showPaging && isLoggedUserLokingOnOwnUnreadMessages}" />

	<c:if test="${noComments}">
		<h3>${eco:translate('There are no comments')}</h3>
	</c:if>

	<c:if test="${not noComments}">

		<c:if test="${showPaging}">
			<tags:paging showSummary="false"/>
		</c:if>

		<c:if test="${showNextButton}">
			<comments:unreadCommentsNextButton userId="${user.id}" pagingModel="${pagingModel}" showInfo="false" />
		</c:if>

	</c:if>

	<div style="float: left; width: 100%;">
		<c:forEach var="entry" items="${photoCommentInfoMap}">

			<c:set var="photoPreviewWrapper" value="${entry.key}" />
			<c:set var="photoCommentInfos" value="${entry.value}" />
			<c:set var="photo" value="${photoPreviewWrapper.photo}" />

			<div style="float: left; width: 100%; border: 1px solid #0070a3; padding-top: 10px; padding-bottom: 10px; margin-top: 10px; margin-bottom: 10px;">

				<div class="textcentered" style="float: left; width: 25%;">
					<photo:photoPreviewLight photoPreviewWrapper="${photoPreviewWrapper}" />
				</div>

				<div style="float: left; width: 70%;">
					<c:forEach  var="photoCommentInfo" items="${photoCommentInfos}">
						<div style="float: left; width: 95%;">
							<comments:commentView commentInfo="${photoCommentInfo}" useAnimation="true" />
						</div>
					</c:forEach>
				</div>
			</div>

		</c:forEach>
	</div>

	<c:if test="${not noComments}">

		<c:if test="${showPaging}">
			<tags:paging showSummary="true"/>
		</c:if>

		<html:submitButton
				id="markAllAsReadButton"
				caption_t="Mark all as read"
				onclick="return markAllAsRead();"
				doNotTranslate="true"
				icon="markAsRead.png"
				/>
		<script type="text/javascript">
			function markAllAsRead() {
				if ( confirm( "${eco:translate('Mark ALL unread comments as read?')}" ) ) {
					document.location.href = '${eco:baseUrlWithPrefix()}/members/${user.id}/comments/to/markAllAsRead/';
					return true;
				}
				return false;
			}
		</script>

		<c:if test="${showNextButton}">
			<comments:unreadCommentsNextButton userId="${user.id}" pagingModel="${pagingModel}" showInfo="true" />
		</c:if>

	</c:if>

	<div class="footerseparator"></div>

</tags:page>
