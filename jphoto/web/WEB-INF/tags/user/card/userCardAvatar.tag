<%@ tag import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ tag import="core.general.img.Dimension" %>
<%@ tag import="core.services.utils.ImageFileUtilsServiceImpl" %>
<%@ tag import="java.io.File" %>
<%@ tag import="core.services.utils.ImageFileUtilsService" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.services.utils.UrlUtilsService" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="userAvatar" required="true" type="core.general.user.UserAvatar" %>
<%@ attribute name="isEditable" required="true" type="java.lang.Boolean" %>

<%
	final ImageFileUtilsService imageFileUtilsService = ApplicationContextHelper.getImageFileUtilsService();

	final File userAvatarFile = userAvatar.getFile();
	Dimension dimension = null; /* TODO: move to controller! */
	Dimension resultDimension = null; /* TODO: move to controller! */
	String userAvatarFileUrl = null;
	final boolean userHasAvatar = userAvatar.isHasAvatar();
	if ( userHasAvatar ) {
		userAvatarFileUrl = userAvatar.getUserAvatarFileUrl();
		dimension = imageFileUtilsService.getImageDimension( userAvatarFile );
		resultDimension = imageFileUtilsService.resizePhotoImage( dimension, new Dimension( 200, 200 ) );
	}

	final UrlUtilsService urlUtilsService = ApplicationContextHelper.getUrlUtilsService();

	final String userAvatarLink = urlUtilsService.getEditUserAvatarLink( user.getId() );
%>

<c:set var="userHasAvatar" value="<%=userHasAvatar%>"/>
<c:set var="userAvatarFileUrl" value="<%=userAvatarFileUrl%>"/>
<c:set var="dimension" value="<%=dimension%>"/>
<c:set var="resultDimension" value="<%=resultDimension%>"/>
<c:set var="userAvatarLink" value="<%=userAvatarLink%>"/>

<c:set var="fullAvatarDivId" value="avatar_${user.id}" />

<style type="text/css">
	.divPadd {
		float: left;
		padding: 5px;
		width: 100%;
		text-align: center;
		/*border: dotted;*/
	}

	.userCardUserAvatarFrame {
		width: 100%;
		min-width: 200px;
		min-height: 200px;
		line-height: 200px;
		height: auto;
		float: left;
		margin-top: 15px;
		padding: 5px;
		text-align: center;
		vertical-align: middle;
		border: #8A8E99 1px solid;
		/*border: dotted;*/
	}
</style>

<div style="float: left; width: 220px; padding: 25px;">
	<div style="float: left;">
		<div class="userCardUserAvatarFrame">
			<c:if test="${userHasAvatar}">
				<c:set var="avatarTitle" value="${eco:translate('Click to see full size')}"/>
				${eco:userAvatarImage(user.id, 200, 200, '', 'showAvatarInFullSize();', 'vertical-align: middle;' )}
			</c:if>

			<c:if test="${not userHasAvatar}">
				<c:set var="noAvatar" value="noAvataBoy.png"/>
				<c:if test="${user.gender == 'FEMALE'}">
					<c:set var="noAvatar" value="noAvatarGirl.png"/>
				</c:if>

				<c:set var="avatarOnClick" value="alert('${eco:translate('Tme member has not downloaded avatar yet... :(')}');"/>
				<c:set var="avatarHint" value="${eco:translate('Tme member has not downloaded avatar yet... :(')}"/>
				<html:img id="" src="icons48/${noAvatar}" width="48" height="48" onclick="${avatarOnClick}" alt="${avatarHint}"/>
			</c:if>
		</div>

		<div class="divPadd">
			<c:if test="${isEditable}">
				<c:set var="avatarHint" value="${eco:translate('Change avatar')}"/>
				<a href="${userAvatarLink}" title="${avatarHint}">${avatarHint}</a>
			</c:if>
		</div>
	</div>
</div>

<div id="${fullAvatarDivId}" style="display: none;">
	<img src="${userAvatarFileUrl}" alt="${eco:escapeHtml(user.name)}" width="${dimension.width}" height="${dimension.height}" onclick="closeMessageBox( '${fullAvatarDivId}' )">
</div>

<script type="text/javascript">
	function showAvatarInFullSize() {
		var parameters = { closeClick:true, closeEsc:true, centered:true, showOverlay:true, overlayCSS:{background: 'black',opacity: .70} };
		showDiv( '${fullAvatarDivId}', parameters, 0 );
	}
</script>