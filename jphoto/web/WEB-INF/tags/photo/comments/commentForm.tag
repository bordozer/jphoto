<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="ui.tags.FormTag" %>
<%@ tag import="ui.controllers.comment.edit.PhotoCommentModel" %>
<%@ tag import="core.enums.FavoriteEntryType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="comments" tagdir="/WEB-INF/tags/photo/comments" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="photo" required="true" type="core.general.photo.Photo" %>
<%@ attribute name="minCommentLength" required="true" type="java.lang.Integer" %>
<%@ attribute name="maxCommentLength" required="true" type="java.lang.Integer" %>
<%@ attribute name="usedDelayBetweenComments" required="true" type="java.lang.Integer" %>

<c:set var="loggedUser" value="<%=EnvironmentContext.getCurrentUser()%>"/>

<c:set var="beingEditedCommentIdFormControl" value="<%=PhotoCommentModel.BEING_EDITED_COMMENT_ID_FORM_CONTROL%>"/>
<c:set var="replyToCommentIdFormControl" value="<%=PhotoCommentModel.REPLY_TO_COMMENT_ID_FORM_CONTROL%>"/>
<c:set var="commentFormControl" value="<%=PhotoCommentModel.COMMENT_TEXT_FORM_CONTROL%>"/>
<c:set var="commentTextFormControl" value="<%=PhotoCommentModel.COMMENT_TEXTAREA_FORM_CONTROL%>"/>
<c:set var="photoCommentFormAnchor" value="<%=PhotoCommentModel.PHOTO_COMMENT_FORM_ANCHOR%>"/>
<c:set var="photoCommentFormInfoDiv" value="<%=PhotoCommentModel.PHOTO_COMMENT_INFO_DIV%>"/>
<c:set var="submitCommentButton" value="<%=PhotoCommentModel.SUBMIT_COMMENT_BUTTON_ID%>"/>

<c:set var="commentDivId" value="<%=PhotoCommentModel.COMMENT_DIV_ID%>"/>
<c:set var="commentEndAnchor" value="<%=PhotoCommentModel.COMMENT_END_ANCHOR%>"/>
<c:set var="commentsEndAnchor" value="<%=PhotoCommentModel.COMMENTS_END_ANCHOR%>"/>

<c:set var="addButtonCaption" value="Comment form: Add a new comment"/>
<c:set var="addButtonCaptionTranslated" value="${eco:translate( addButtonCaption )}"/>

<c:set var="newCommentsNotificationEntryType" value="<%=FavoriteEntryType.NEW_COMMENTS_NOTIFICATION%>"/>

<c:set var="photoId" value="${photo.id}"/>

<comments:commentJS photoId="${photoId}"/>

<tags:inputHintForm/>

<c:set var="url" value="${eco:baseUrl()}/photos/${photoId}/comments/save/"/>
<c:set var="frmPhotoComment" value="<%=FormTag.FORM_NAME%>"/>

<eco:form formName="${frmPhotoComment}" action="">

	<input type="hidden" id="${beingEditedCommentIdFormControl}" name="${beingEditedCommentIdFormControl}" value="0"/>
	<input type="hidden" id="${replyToCommentIdFormControl}" name="${replyToCommentIdFormControl}" value="0"/>
	<input type="hidden" id="${commentFormControl}" name="${commentFormControl}" value=""/>

	<a name="${photoCommentFormAnchor}"></a>

	<table width="90%">

		<tr>
			<td width="50px">&nbsp;</td>
			<td>
				<div id="${photoCommentFormInfoDiv}">${addButtonCaptionTranslated}</div>
			</td>
		</tr>

		<tr>
			<td>${eco:translate('Comment form: Comment from')}</td>
			<td>
				<user:userCard user="${loggedUser}"/>
			</td>
		</tr>

		<tr>
			<td>
					${eco:translate('Comment form: Comment text')}
			</td>
			<td>
				<tags:inputHint inputId="${commentTextFormControl}">
					<jsp:attribute name="inputField">
						<html:textarea inputId="${commentTextFormControl}" inputValue="" cols="40" rows="5"/>
						<div style="float: left; width: 100%; height: 20px;">
								${eco:translate2( "Comment form: Comments length: $1 - $2 symbols", minCommentLength, maxCommentLength )}
							<br/>
								${eco:translate1( "Comment form: Your delay between comments: $1 seconds", usedDelayBetweenComments )}
						</div>
					</jsp:attribute>
				</tags:inputHint>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<html:submitButton id="${submitCommentButton}" caption_t="${addButtonCaption}" onclick="return submitForm();"/>
			</td>
		</tr>

		<tr>
			<td colspan="2">
					${eco:translate('Comment form: Comments subscribe')}: <icons:favoritesPhoto photo="${photo}" entryType="${newCommentsNotificationEntryType}"/>
			</td>
		</tr>

		<tr>
			<td colspan="2"><a href="#">${eco:translate('Comment form: To the top of page')}</a></td>
		</tr>

	</table>

</eco:form>

<script type="text/javascript">

	function submitForm() {
		var textArea = $( '#${commentTextFormControl}' );
		var commentText = textArea.val();

		var commentField = $( '#${commentFormControl}' );
		commentField.val( commentText );

		if ( commentText.length < ${minCommentLength} ) {
			showUIMessage_Information( "${eco:translate2('Comment form: Comment must be more then $1 and less then $2 symbols', minCommentLength, maxCommentLength)}" );
			return false;
		}

		disableCommentText();

		var beingEditedCommentId = $( '#${beingEditedCommentIdFormControl}' ).val();
		var isCommentEditing = beingEditedCommentId == 0;
		var replyToCommentId = $( '#${replyToCommentIdFormControl}' ).val();

		function insertBeforeAnchor( anchor, response ) {
			$( '.' + anchor ).before( response );
		}

		function updateCommentDivContent( commentId, response ) {
			$( '#${commentDivId}' + commentId ).html( response );
		}

		$.ajax( {
					type: 'POST',
					url: '${url}',
					data: $( '#${frmPhotoComment}' ).serialize(),
					success: function ( response ) {
						// response == CommentSave.jsp
						if ( isCommentEditing ) {
							var endOfTheBeingEditedCommentAnchor = '${commentsEndAnchor}';
							if ( replyToCommentId > 0 ) {
								endOfTheBeingEditedCommentAnchor = '${commentEndAnchor}' + replyToCommentId;
							}
							insertBeforeAnchor( endOfTheBeingEditedCommentAnchor, response );
						} else {
							updateCommentDivContent( beingEditedCommentId, response );
							disableCommentText(); // TODO: processes ALL icons!!! Replace with desabling only 'commentId'
						}
						refreshPhotoInfo();
					},
					error: function () {
						showUIMessage_Error( '${eco:translate('Comment form: Error saving message')}' );
						enableCommentText();
					}
				} );

		return false;
	}

</script>