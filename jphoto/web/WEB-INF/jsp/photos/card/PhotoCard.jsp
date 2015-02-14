<%@ page import="core.enums.FavoriteEntryType" %>
<%@ page import="ui.controllers.comment.edit.PhotoCommentModel" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page import="utils.UserUtils" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="org.springframework.mobile.device.DeviceType" %>
<%@ page import="ui.services.menu.entry.items.EntryMenuType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="comments" tagdir="/WEB-INF/tags/photo/comments" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<jsp:useBean id="photoCardModel" type="ui.controllers.photos.card.PhotoCardModel" scope="request"/>

<c:set var="photo" value="${photoCardModel.photo}"/>
<c:set var="photoInfo" value="${photoCardModel.photoInfo}"/>
<c:set var="photoInfoUrl" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoInfoLink( photoCardModel.getPhoto().getId() )%>"/>

<c:set var="shownDimension" value="<%=photoCardModel.getShownDimension()%>"/>
<c:set var="originalDimension" value="${photo.imageDimension}"/>

<c:set var="commentDelay" value="<%=photoCardModel.getCommentDelay()%>"/>
<c:set var="nextCommentTime" value="<%=photoCardModel.getUserNextCommentTime()%>"/>
<c:set var="commentTextFormControl" value="<%=PhotoCommentModel.COMMENT_TEXTAREA_FORM_CONTROL%>"/>
<c:set var="commentsEndAnchor" value="<%=PhotoCommentModel.COMMENTS_END_ANCHOR%>"/>

<c:set var="photoId" value="${photo.id}"/>
<c:set var="photoName" value="${photo.name}"/>
<c:set var="imageUrl" value="<%=photoCardModel.getPhotoInfo().getPhotoImgUrl()%>"/>

<c:set var="favoriteEntryType" value="<%=FavoriteEntryType.FAVORITE_PHOTOS%>"/>
<c:set var="newCommentsNotificationEntryType" value="<%=FavoriteEntryType.NEW_COMMENTS_NOTIFICATION%>"/>
<c:set var="favoriteEntryTypeBookmark" value="<%=FavoriteEntryType.BOOKMARKED_PHOTOS%>"/>

<c:set var="commentEditIconId" value="<%=PhotoCommentModel.COMMENT_EDIT_ICON_ID%>"/>
<c:set var="commentReplyIconId" value="<%=PhotoCommentModel.COMMENT_REPLY_ICON_ID%>"/>
<c:set var="commentDeleteIconId" value="<%=PhotoCommentModel.COMMENT_DELETE_ICON_ID%>"/>

<c:set var="submitCommentButton" value="<%=PhotoCommentModel.SUBMIT_COMMENT_BUTTON_ID%>"/>

<c:set var="photoNameEscaped" value="${eco:escapeHtml(photo.name)}"/>

<c:set var="isLoggedUser" value="<%=UserUtils.isCurrentUserLoggedUser()%>" />
<c:set var="loggedUser" value="<%=EnvironmentContext.getCurrentUser()%>" />
<c:set var="blackListText" value="You are int the black list of photo\'s author" />
<c:set var="photoMenuTypeId" value="<%=EntryMenuType.PHOTO.getId()%>" />

<c:set var="isMobile" value="<%=EnvironmentContext.getDeviceType() == DeviceType.MOBILE%>"/>
<c:set var="baseUrl" value="${eco:baseUrl()}" />

<c:set var="photoImageID" value="photo_${photo.id}" />

<tags:page pageModel="${photoCardModel.pageModel}">

	<%--<link rel="stylesheet" type="text/css" href="${baseUrl}/js/lib/slimbox2/slimbox2.css"/>--%>

	<tags:contextMenuJs />

	<div class="panel panel-default">

		<div class="panel-heading">
			<h3 class="panel-title text-center">${photoNameEscaped}</h3>
		</div>

		<div class="panel-body text-center" style="background-color: ${not empty photo.bgColor ? photo.bgColor : 'transparent'};">

			<a href="#" onclick="return false;">
				<img id="${photoImageID}"
					 src="${imageUrl}"
					 alt="${photoNameEscaped}"
					 title="${photoNameEscaped}"
					 width="${shownDimension.width}"
					 height="${shownDimension.height}"
				/>
			</a>

			<script type="text/javascript">
				/*require( [ 'jquery', '/js/lib/slimbox2/slimbox2.js'], function ( $, lightbox ) {

				} );*/
			</script>

		</div>

		<div class="panel-footer text-center">

				<icons:favoritesPhoto photo="${photo}" entryType="${favoriteEntryType}" iconSize="32"/>

				<icons:favoritesPhoto photo="${photo}" entryType="${newCommentsNotificationEntryType}" iconSize="32"/>

				<icons:favoritesPhoto photo="${photo}" entryType="${favoriteEntryTypeBookmark}" iconSize="32"/>

				<tags:contextMenu entryId="${photoId}" entryMenuType="<%=EntryMenuType.PHOTO%>" />

			<c:if test="${isLoggedUser}">
				<br />
				<html:img id="cropDisabled16" src="photo/cropDisabled16.png" width="16" height="16" />
				<html:img id="cropEnabled16" src="photo/cropEnabled16.png" width="16" height="16" />
			</c:if>
		</div>

	</div>

	<js:confirmAction/>

	<div class="row"> <%-- comments, info, voting --%>

		<div class="col-lg-8"> <%-- description and comments column --%>

			<c:if test="${not empty photo.description}">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="page-title">
							${eco:translate('Photo info: Photo description')}
						</h4>
					</div>
					<div class="panel-body">
						${eco:formatPhotoCommentText(photo.description)}
					</div>
				</div>
			</c:if>

			<div class="panel">
				<div class="col-lg-12" id="commentList">
					<%-- all comments are going to be places here by JS --%>
					<div class="${commentsEndAnchor}"></div>
				</div>
			</div>

			<script type="text/javascript">

				var rootComments = [ <c:forEach var="commentId" items="${photoCardModel.rootCommentsIds}" varStatus="status">${commentId}<c:if test="${not status.last}">, </c:if></c:forEach> ];

				require( [ 'jquery' ], function ( $ ) {

					function renderComment( index ) {

						var commentId = rootComments[ index ];
						if ( commentId == undefined ) {
							return;
						}

						$.ajax( {
									type:'GET',
									url:'${eco:baseUrl()}/photo/${photoId}/comment/' + commentId + "/",
									success:function ( response ) {
										$( '.${commentsEndAnchor}' ).before( response ); // response == /comments/view/PhotoComment.jsp
										renderComment( index + 1 ); // TOD
									},
									error:function () {
										showUIMessage_Error( '${eco:translate('Photo card: Error getting photo comment')}' + ' ' + commentId );
									}
								} );
					}

					renderComment( 0 );
				} );

			</script>

			<c:if test="${photoCardModel.commentingValidationResult.validationPassed}">
				<comments:commentForm photo="${photo}" minCommentLength="${photoCardModel.minCommentLength}" maxCommentLength="${photoCardModel.maxCommentLength}" usedDelayBetweenComments="${photoCardModel.usedDelayBetweenComments}" />
			</c:if>

			<c:if test="${photoCardModel.commentingValidationResult.validationFailed}">
				<tags:validationResult title_t="ValidationResult: You can not comment the photo" validationMessage="${photoCardModel.commentingValidationResult.validationMessage}" />
			</c:if>

			<script type="text/javascript">

				function countdownDelay( commentDelay ) {
					setTimeout( function () {
						var textareaInfo = $( '#textarea_${commentTextFormControl}' );
						if ( commentDelay > 0 ) {

							var commentDelayTxt = Math.round( commentDelay / 1000 );
							textareaInfo.html( "${eco:translate( "Photo commenting: Time to the next comment" )}: " + commentDelayTxt + " ${eco:translate( "second(s)" )}" );

							commentDelay = commentDelay - 1000;
							if ( commentDelay < 0 ) {
								commentDelay = 0;
							}

							countdownDelay( commentDelay );
						} else {
							enableCommentText();
							textareaInfo.html( '' );
							<%--showUIMessage_Information( '${eco:translate( "You can add comments again" )}' ); --%> <%-- TODO: message that commenting is accessible--%>
						}

					}, 1000 );
				}

				var iconIDs = [ '${commentEditIconId}', '${commentReplyIconId}', '${commentDeleteIconId}' ];

				function enableCommentText() {
					var textarea = $( '#${commentTextFormControl}' );
					textarea.removeAttr( 'disabled' );
					textarea.css( 'background', '' );
					$( "#${submitCommentButton}" ).removeAttr( 'disabled' );

					$( iconIDs ).each( function() {
						var selector = "[id^='" + this + "']";
						$( selector ).each( function() {
							enableIcon( this );
						});
					});
				}

				function disableCommentText() {
					var textarea = $( '#${commentTextFormControl}' );
					textarea.attr( 'disabled', 'disabled' );
					textarea.css( 'background', 'silver' );
					$( "#${submitCommentButton}" ).attr( 'disabled', 'disabled' );

					$( iconIDs ).each( function() {
						var selector = "[id^='" + this + "']";
						$( selector ).each( function() {
							disableIcon( this );
						});
					});
				}

				function enableIcon( icon ) {
					$( icon ).removeAttr( 'disabled' ).css( 'opacity', 1 );
				}

				function disableIcon( icon ) {
					$( icon ).attr( 'disabled', 'disabled' ).css( 'opacity', 0.5 );
				}

				<c:if test="${commentDelay > 0}">
					require( [ 'jquery' ], function( $ ) {
						$( document ).ready( function () {
							disableCommentText();
							countdownDelay( ${commentDelay} - 1000 );
						} );
					} );
				</c:if>

				function refreshPhotoInfo() {
					$.ajax( {
								type:'GET',
								url:'${photoInfoUrl}',
								success:function ( response ) {
									$( '#photoInfoDiv' ).html( response ); // response == photos/PhotoInfo.jsp

								},
								error:function () {
									showUIMessage_Error( '${eco:translate('Photo info: Error getting photo info')}' );
								}
							} );
				}
			</script>

		</div> <%-- / description and comments column --%>

		<div class="col-lg-4">

			<div class="row">
				<div class="col-lg-12">
					<photo:photoInfo photoInfo="${photoCardModel.photoInfo}" votingModel="${photoCardModel.votingModel}" />
				</div>
			</div>

			<div class="row votingDiv">
				<div class="col-lg-12 text-center photo-appraisal-form-container">
					<html:spinningWheel16 title="${eco:translate('Photo appraisal form is being loaded...')}" />
				</div>
			</div>

			<script type="text/javascript">
				require( [ 'jquery', 'modules/photo/appraisal/photo-appraisal'], function ( $, photoAppraisal ) {
					photoAppraisal( ${photoId}, ${loggedUser.id}, $( '.photo-appraisal-form-container' ) );
				} );
			</script>

		</div>

	</div> <%-- / comments, info, voting --%>

	<c:if test="${isLoggedUser}">

		<%--<script type="text/javascript" src="${eco:baseUrl()}/js/lib/jcrop/js/jquery.Jcrop.js"></script>--%>
		<%--<link rel="stylesheet" href="${eco:baseUrl()}/js/lib/jcrop/css/jquery.Jcrop.css" type="text/css"/>--%>

		<%--<script type="text/javascript">

			require( [ 'jquery' ], function( $ ) {

				var cropBox = '#photo_${photo.id}';
				var jcrop_api;

				$( '#cropEnabled16' ).hide();

				function initJcrop() {
					jcrop_api = $.Jcrop( cropBox, {
						addClass: 'text-centered', bgOpacity: 0.3, bgFade: true, onDblClick: updateCoords
					} );
				}

				function updateCoords( c ) {
					var coord = '<!--x' + c.x + 'y' + c.y + 'w' + c.w + 'h' + c.h + '-->';
					$( '#commentTextArea' ).val( coord );
				}

				function cropDisable() {
					jcrop_api.disable();
					jcrop_api.destroy();
					$( '#cropEnabled16' ).hide();
					$( '#cropDisabled16' ).show();
				}

				function cropEnable() {
					initJcrop();
					$( '#cropDisabled16' ).hide();
					$( '#cropEnabled16' ).show();
				}

				$( '#cropDisabled16' ).click( function ( e ) {
					cropEnable();
				} );

				$( '#cropEnabled16' ).click( function ( e ) {
					cropDisable();
				} );

			});
		</script>--%>
	</c:if>

	<c:set var="bgColor" value="${not empty photo.bgColor ? '#'.concat(photo.bgColor) : '#000000'}" />
	<c:set var="photoDiv" value="photoDiv" />

	<div id="${photoDiv}" style="width: ${originalDimension.width}px; height:${originalDimension.height}px;display: none;" onclick="fadeoutAndCloseMessageBox( '${photoDiv}' );">
		<img id="showPhoto_${photo.id}" src="${imageUrl}" alt="${photoNameEscaped}" title="${photoNameEscaped}" width="${originalDimension.width}" height="${originalDimension.height}"/>
	</div>

</tags:page>
