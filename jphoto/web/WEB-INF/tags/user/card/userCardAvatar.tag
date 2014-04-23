<%@ tag import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ tag import="core.general.img.Dimension" %>
<%@ tag import="core.services.utils.ImageFileUtilsServiceImpl" %>
<%@ tag import="java.io.File" %>
<%@ tag import="core.services.utils.ImageFileUtilsService" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="core.services.utils.UrlUtilsService" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
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

<div class="user-card-block">

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

	<%-- TODO: move this link to the user's context menu --%>
	<c:if test="${isEditable}">
		<div style="text-align: center; width: 200px; padding-top: 10px;">
			<c:set var="avatarHint" value="${eco:translate('User card: Change avatar')}"/>
			<a href="${userAvatarLink}" title="${avatarHint}">${avatarHint}</a>
		</div>
	</c:if>
</div>

<div id="${fullAvatarDivId}" style="display: none;">
	<img src="${userAvatarFileUrl}" alt="${eco:escapeHtml(user.name)}" width="${dimension.width}" height="${dimension.height}" onclick="closeMessageBox( '${fullAvatarDivId}' )">
</div>

<script type="text/javascript">
	function showAvatarInFullSize() {
//		var parameters = { closeClick:true, closeEsc:true, centered:true, showOverlay:true, overlayCSS:{background: 'black',opacity: .70} };
		showMessage_CustomDiv( '${fullAvatarDivId}' );
	}
</script>