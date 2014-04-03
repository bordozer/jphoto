<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="ui.controllers.comment.edit.PhotoCommentModel" %>
<%@ tag import="core.enums.FavoriteEntryType" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.services.system.ConfigurationService" %>
<%@ tag import="core.general.configuration.ConfigurationKey" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="comments" tagdir="/WEB-INF/tags/photo/comments" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<%@ attribute name="commentInfo" required="true" type="ui.controllers.comment.edit.PhotoCommentInfo" %>
<%@ attribute name="useAnimation" required="false" type="java.lang.Boolean" %>

<c:set var="comment" value="${commentInfo.photoComment}"/>
<c:set var="commentAuthor" value="${comment.commentAuthor}"/>
<c:set var="commentAuthorAvatar" value="${commentInfo.commentAuthorAvatar}"/>
<c:set var="commentAuthorAvatarUrl" value="${commentAuthorAvatar.userAvatarFileUrl}"/>

<c:set var="currentUserId" value="<%=EnvironmentContext.getCurrentUser().getId()%>"/>
<c:set var="photoCommentFormAnchor" value="<%=PhotoCommentModel.PHOTO_COMMENT_FORM_ANCHOR%>"/>

<c:set var="commentEditIconId" value="<%=PhotoCommentModel.COMMENT_EDIT_ICON_ID%>"/>
<c:set var="commentReplyIconId" value="<%=PhotoCommentModel.COMMENT_REPLY_ICON_ID%>"/>
<c:set var="commentDeleteIconId" value="<%=PhotoCommentModel.COMMENT_DELETE_ICON_ID%>"/>

<c:set var="commentDivId" value="<%=PhotoCommentModel.COMMENT_DIV_ID%>"/>
<c:set var="commentStartAnchor" value="<%=PhotoCommentModel.COMMENT_START_ANCHOR%>"/>
<c:set var="commentEndAnchor" value="<%=PhotoCommentModel.COMMENT_END_ANCHOR%>"/>
<c:set var="commentTextDivId" value="<%=PhotoCommentModel.COMMENT_TEXT_DIV_ID%>"/>

<c:set var="commentId" value="${comment.id}"/>
<c:set var="fullCommentDivId" value="${commentDivId}${commentId}"/>

<%
	final ConfigurationService configurationService = ApplicationContextHelper.getConfigurationService();
%>
<c:set var="anonymouslyPostedName" value="<%=configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME )%>"/>

<%--TODO: highlight own comments--%>
<%--<c:set var="ownPhotoStyle" value=""/>
<c:if test="${comment.commentAuthor.id == currentUserId}">
	<c:set var="ownPhotoStyle" value="block-background"/>
</c:if>--%>

<c:set var="photoAuthor" value="${commentInfo.photoAuthor}"/>
<c:set var="photoAuthorId" value="${photoAuthor.id}"/>
<c:set var="commentAuthor" value="${comment.commentAuthor}"/>
<c:set var="commentAuthorId" value="${commentAuthor.id}"/>
<c:set var="isThisPhotoOfCurrentUser" value="${photoAuthorId == currentUserId}"/>
<c:set var="isCommentOfPhotoAuthor" value="${photoAuthorId == commentAuthorId}"/>
<c:set var="showCommentAuthorData" value="${not commentInfo.authorNameMustBeHidden}"/>

<c:set var="commentReadTime" value="${comment.readTime.time}"/>

<div id="${fullCommentDivId}" class="floatleft" style="margin-bottom: 10px; ${useAnimation ? "display: none;" : ""}">

	<div class="floatleft photoCommentContainerInner block-border">

		<div class="floatleft block-background">

			&nbsp;#${commentId} /&nbsp;

			<a name="${commentStartAnchor}${commentId}"></a>

			<c:if test="${not isCommentOfPhotoAuthor}">
				<c:if test="${commentReadTime == 0}">
					<c:if test="${isThisPhotoOfCurrentUser}">
						<html:img id="" src="icons16/photoCommentNew.png" width="16" height="16" alt="${eco:translate('New comment')}" />
					</c:if>

					<c:if test="${not isThisPhotoOfCurrentUser}">
						<c:set var="readHint" value="${eco:translate1('$1 has not read this comment yet', eco:escapeHtml(photoAuthor.name))}"/>
						<c:if test="${not showCommentAuthorData}">
							<c:set var="readHint" value="${eco:translate('Photo\\\'s author has not read this comment yet')}"/>
						</c:if>
						<html:img id="" src="icons16/photoCommentNew.png" width="16" height="16" alt="${readHint}" />
					</c:if>
				</c:if>

				<c:if test="${commentReadTime > 0}">
					<html:img id="" src="icons16/photoCommentRead.png" width="16" height="16"
							  alt="${eco:translate3('$1 read this comment at $2 $3', eco:escapeHtml(photoAuthor.name), eco:formatDate(comment.readTime), eco:formatTime(comment.readTime))}" />
				</c:if>
			</c:if>

			<c:if test="${showCommentAuthorData}">
				<user:userCard user="${commentAuthor}"/>
				<icons:favoritesUser user="${commentAuthor}" entryType="<%=FavoriteEntryType.BLACKLIST%>" />
			</c:if>

			<c:if test="${isCommentOfPhotoAuthor}">
				<span title="${eco:translate('The photo is posted anonymously')}">${anonymouslyPostedName}</span>
				/
				<html:img id="" src="icons16/photoCommentAuthor.png" width="16" height="16" alt="${eco:translate('Photo\\\'s author comment')}" />
			</c:if>

			<c:if test="${showCommentAuthorData}">
				/ <user:userRankInGenreRenderer userRankIconContainer="${commentInfo.userRankIconContainer}"/>
			</c:if>

			/ ${eco:formatDate(comment.creationTime)} ${eco:formatTime(comment.creationTime)}

			<c:if test="${not empty commentInfo and not empty commentInfo.entryMenu.entryMenuItems}">
				/
				<tags:entryMenu entryMenu="${commentInfo.entryMenu}" />
			</c:if>

		</div>

		<div id="${commentTextDivId}${commentId}" class="photoCommentText">

			<c:set var="hasAvatar" value="${commentAuthorAvatar.hasAvatar}" />

			<c:if test="${showCommentAuthorData && hasAvatar}">
				<div class="textcentered" style="float: left; width: 90px;">
					<img id="avatar_${commentAuthor.id}" src="${commentAuthorAvatarUrl}" height="50" alt="${eco:translate1('$1 - avatar', eco:escapeHtml(commentAuthor.name))}" />
				</div>
			</c:if>

			<c:if test="${!showCommentAuthorData}">
				<div class="textcentered" style="float: left; width: 90px;">
					<html:img src="hidden_picture.png" alt="${eco:translate('Author name is hidden due to anonymous period of photo')}"  id="avatar_${comment.id}" height="100" width="100"/>
				</div>
			</c:if>

			<div style="float: left; width: ${hasAvatar ? '75' : '95'}%; height: 100%; vertical-align: middle; margin-left: 10px;">
				<div id="photoCommentText_${commentId}" style="float: left; width: 100%;">
					${eco:formatPhotoCommentText(comment.commentText)}
				</div>

				<div style="float: left; width: 100%; padding-top: 15px; font-size: 10px;">
					<c:forEach var="userPhotoVote" items="${commentInfo.commentAuthorVotes}" varStatus="status">
						${eco:translateVotingCategory(userPhotoVote.photoVotingCategory.id)}:
						<span title="${eco:translate1('Set by $1 mark', commentAuthor.nameEscaped)}">${userPhotoVote.mark > 0 ? '+' : ''}${userPhotoVote.mark}</span>
						/
						<span title="${eco:translate1('Max accessible at voting time for $1 mark', commentAuthor.nameEscaped)}">+${userPhotoVote.maxAccessibleMark}</span>
						<c:if test="${not status.last}">
							,
						</c:if>
					</c:forEach>
				</div>
			</div>
		</div>

		<c:forEach var="childrenPommentInfo" items="${commentInfo.childrenComments}">
			<comments:commentView commentInfo="${childrenPommentInfo}" useAnimation="${useAnimation}" />
		</c:forEach>

		<div class="${commentEndAnchor}${commentId}"></div>
	</div>
</div>

<script type="text/javascript">
//	jQuery().ready( function() {
		$( '#${fullCommentDivId}' ).fadeIn( 700, "linear" );
//	});
</script>