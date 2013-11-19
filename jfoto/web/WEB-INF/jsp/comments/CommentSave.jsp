<%@ page import="org.springframework.validation.BindingResult" %>
<%@ page import="controllers.comment.edit.PhotoCommentModel" %>
<%@ page import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="comments" tagdir="/WEB-INF/tags/photo/comments" %>

<jsp:useBean id="photoCommentModel" type="controllers.comment.edit.PhotoCommentModel" scope="request"/>

<%
	final BindingResult bindingResult = photoCommentModel.getBindingResult();
%>

<c:set var="hasErrors" value="<%=bindingResult != null && bindingResult.hasErrors()%>"/>
<c:set var="commentDelay" value="<%=photoCommentModel.getCommentDelay()%>"/>
<c:set var="isNew" value="<%=photoCommentModel.isNew()%>"/>

<c:set var="commentFormControl" value="<%=PhotoCommentModel.COMMENT_TEXT_FORM_CONTROL%>"/>
<c:set var="commentTextFormControl" value="<%=PhotoCommentModel.COMMENT_TEXTAREA_FORM_CONTROL%>"/>
<c:set var="commentTextDivId" value="<%=PhotoCommentModel.COMMENT_TEXT_DIV_ID%>"/>
<c:set var="commentStartAnchor" value="<%=PhotoCommentModel.COMMENT_START_ANCHOR%>"/>

<c:set var="url" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoCardLink( photoCommentModel.getPhotoCommentInfo().getPhotoComment().getPhotoId() )%>"/>

<c:if test="${not hasErrors}">

	<c:set var="commentInfo" value="${photoCommentModel.photoCommentInfo}" />
	<c:set var="comment" value="${commentInfo.photoComment}" />

	<comments:commentView commentInfo="${commentInfo}" useAnimation="false" />

	<script type="text/javascript">
		resetPhotoCommentForm();

		<c:if test="${isNew}">
			disableCommentText();
			countdownDelay( ${commentDelay} );
		</c:if>

		<c:if test="${not isNew}">
			countdownDelay( 1000 );
		</c:if>

		$( "#${commentTextDivId}${comment.id}" ).addClass( 'newInsertedComment' );

		document.location.href = "${url}#${commentStartAnchor}${comment.id}";

	</script>
</c:if>

<c:if test="${hasErrors}">
	<tags:springErrorHighliting bindingResult="${photoCommentModel.bindingResult}"/>
	<script type="text/javascript">
		enableCommentText();
	</script>
</c:if>