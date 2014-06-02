<%@ page import="ui.controllers.users.avatar.UserAvatarModel" %>
<%@ page import="java.io.File" %>
<%@ page import="core.general.img.Dimension" %>
<%@ page import="core.services.utils.UserPhotoFilePathUtilsService" %>
<%@ page import="core.services.utils.ImageFileUtilsService" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userAvatarModel" type="ui.controllers.users.avatar.UserAvatarModel" scope="request"/>

<c:set var="avatarFileControl" value="<%=UserAvatarModel.AVATAR_FILE_FORM_CONTROL%>" />

<%
	final UserPhotoFilePathUtilsService userPhotoFilePathUtilsService = ApplicationContextHelper.getBean( UserPhotoFilePathUtilsService.BEAN_NAME );

	final String userAvatarFileUrl = userPhotoFilePathUtilsService.getUserAvatarFileUrl( userAvatarModel.getUser().getId() );
%>

<c:set var="user" value="<%=userAvatarModel.getUser()%>" />
<c:set var="dimension" value="<%=userAvatarModel.getDimension()%>" />
<c:set var="userAvatarFileUrl" value="<%=userAvatarFileUrl%>" />
<c:set var="doesAvatarExist" value="${not empty dimension}" />

<tags:page pageModel="${userAvatarModel.pageModel}">

	<eco:form action="${eco:baseUrl()}/members/${userAvatarModel.user.id}/avatar/" multipartForm="true">

		<table:table width="800" border="0">

			<table:separatorInfo colspan="2" height="50" title="${eco:translate('Avatar')}"/>

			<c:if test="${doesAvatarExist}">
				<table:tr>
					<table:td colspan="2" cssClass="text-centered">
						${eco:userAvatarImage(user.id, dimension.width, dimension.height, '', '', '')}
					</table:td>
				</table:tr>

				<table:separator colspan="2" />
			</c:if>

			<table:tredit>
				<table:tdtext text_t="File"/>

				<table:tddata>
					<form:input path="userAvatarModel.avatarFile" type="file" id="${avatarFileControl}" />
				</table:tddata>
			</table:tredit>

			<c:if test="${doesAvatarExist}">
				<table:tr>
					<table:td colspan="2">
						<a href="#" onclick="return deleteAvatar();">${eco:translate('Delete avatar')}</a>
						<script type="text/javascript">
							function deleteAvatar() {
								if ( confirm( '${eco:translate('Delete your avatar?')}' ) ) {
									document.location.href = '${eco:baseUrl()}/members/${userAvatarModel.user.id}/avatar/delete/';
								}
								return false;
							}
						</script>
					</table:td>
				</table:tr>
			</c:if>

			<table:trok text_t="Save" />

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${userAvatarModel.bindingResult}"/>

</tags:page>