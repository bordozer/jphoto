<%@ tag import="controllers.comment.edit.PhotoCommentModel" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="org.jabsorb.JSONRPCBridge" %>
<%@ tag import="core.services.photo.PhotoCommentService" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photoId" required="true" type="java.lang.Integer" %>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "photoCommentService", ApplicationContextHelper.<PhotoCommentService>getBean( PhotoCommentService.BEAN_NAME ) );
%>

<c:set var="editUrl" value="${eco:baseUrlWithPrefix()}/photos/${photoId}/comments/"/>
<c:set var="deleteUrl" value="${eco:baseUrlWithPrefix()}/photos/${photoId}/comments/"/>

<c:set var="loggedUser" value="<%=EnvironmentContext.getCurrentUser()%>"/>
<c:set var="replyToCommentIdFormControl" value="<%=PhotoCommentModel.REPLY_TO_COMMENT_ID_FORM_CONTROL%>"/>
<c:set var="photoCommentFormInfoDiv" value="<%=PhotoCommentModel.PHOTO_COMMENT_INFO_DIV%>"/>
<c:set var="photoCommentFormAnchor" value="<%=PhotoCommentModel.PHOTO_COMMENT_FORM_ANCHOR%>"/>

<c:set var="beingEditedCommentIdFormControl" value="<%=PhotoCommentModel.BEING_EDITED_COMMENT_ID_FORM_CONTROL%>"/>
<c:set var="commentTextFormControl" value="<%=PhotoCommentModel.COMMENT_TEXTAREA_FORM_CONTROL%>"/>
<c:set var="photoCommentFormAnchor" value="<%=PhotoCommentModel.PHOTO_COMMENT_FORM_ANCHOR%>"/>

<c:set var="commentDivId" value="<%=PhotoCommentModel.COMMENT_DIV_ID%>"/>

<c:set var="submitCommentButton" value="<%=PhotoCommentModel.SUBMIT_COMMENT_BUTTON_ID%>"/>

<c:set var="addButtonCaption" value="${eco:translate('Add a new comment')}"/>

<script type="text/javascript">

	function showMessageAboutDelayToNextComment() {
		var userDelayToNextComment = jsonRPC.photoCommentService.getUserDelayToNextComment( ${loggedUser.id} );
		userDelayToNextComment = Math.round( userDelayToNextComment / 1000 );
		showInformationMessage( "${eco:translate('You can leave a comment after ')}" + userDelayToNextComment + "${eco:translate(' second(s)')}" );
	}
	function editComment( commentId ) {

		if ( isUserCanNOTCommentPhoto() ) {
			showMessageAboutDelayToNextComment();
			return;
		}

		var commentDTO = jsonRPC.photoCommentService.getCommentDTO( commentId );

		resetPhotoCommentForm();

		$( '#${beingEditedCommentIdFormControl}' ).val( commentDTO.commentId );
		$( '#${commentTextFormControl}' ).val( commentDTO.commentText );

		$( '#${submitCommentButton}' ).html( '${eco:translate('Save a comment')}' );

		$( '#${photoCommentFormInfoDiv}' ).html( '<b>${eco:translate( "Editing comment #")}' + commentDTO.commentId + '</b> &nbsp/&nbsp;' + '<a href=\"#${photoCommentFormAnchor}\" onclick=\"resetPhotoCommentForm();\">${eco:translate('Reset comment form')}</a>' );

		$( '#${commentTextFormControl}' ).focus();
		$( '#${commentTextFormControl}' ).select();
	}

	function replyToComment( commentId ) {
		if ( isUserCanNOTCommentPhoto() ) {
			showMessageAboutDelayToNextComment();
			return;
		}

		$( '#${replyToCommentIdFormControl}' ).val( commentId );
		$( '#${submitCommentButton}' ).html( '${eco:translate('Reply on the comment')}' );
		$( '#${photoCommentFormInfoDiv}' ).html( '<b>${eco:translate( "Answer on comment #")}' + commentId + '</b>' + '<br /><a href=\"#${photoCommentFormAnchor}\" onClick=\"resetPhotoCommentForm();\">${eco:translate('Reset comment form')}</a>' );
	}

	function deleteComment( commentId ) {
		if ( isUserCanNOTCommentPhoto() ) {
			showMessageAboutDelayToNextComment();
			return;
		}

		var beingEditedCommentId = $( '#${beingEditedCommentIdFormControl}' ).val();
		if ( beingEditedCommentId == commentId ) {
			showInformationMessage( "${eco:translate('You can not delete a comment which you are editing now')}" );
			return;
		}

		if ( confirmDeletion( "${eco:translate('Delete comment?')}" ) ) {
			var commentDTO = jsonRPC.photoCommentService.markCommentAsDeletedAjax( ${loggedUser.id}, commentId );
			if( commentDTO.errorMessage != undefined ) {
				showErrorMessage( commentDTO.errorMessage );
				return;
			}
			$( '#photoCommentText_' + commentId ).html( commentDTO.commentText );
		}
	}

	function resetPhotoCommentForm() {
		$( '#${beingEditedCommentIdFormControl}' ).val( '0' );
		$( '#${replyToCommentIdFormControl}' ).val( '0' );
		$( '#${commentTextFormControl}' ).val( '' );
		$( '#${submitCommentButton}' ).html( '${eco:translate(addButtonCaption)}' );
		$( '#${photoCommentFormInfoDiv}' ).html( '${addButtonCaption}' );
	}

	function isUserCanNOTCommentPhoto() {
		return ! jsonRPC.photoCommentService.isUserCanCommentPhotos( ${loggedUser.id} );
	}

</script>