<%@ page import="ui.controllers.users.avatar.UserAvatarModel" %>
<%@ page import="core.services.utils.UserPhotoFilePathUtilsService" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="edit" tagdir="/WEB-INF/tags/edit" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userAvatarModel" type="ui.controllers.users.avatar.UserAvatarModel" scope="request"/>

<c:set var="avatarFileControl" value="<%=UserAvatarModel.AVATAR_FILE_FORM_CONTROL%>"/>

<%
	final UserPhotoFilePathUtilsService userPhotoFilePathUtilsService = ApplicationContextHelper.getBean( UserPhotoFilePathUtilsService.BEAN_NAME );

	final String userAvatarFileUrl = userPhotoFilePathUtilsService.getUserAvatarFileUrl( userAvatarModel.getUser().getId() );
%>

<c:set var="user" value="<%=userAvatarModel.getUser()%>"/>
<c:set var="dimension" value="<%=userAvatarModel.getDimension()%>"/>
<c:set var="userAvatarFileUrl" value="<%=userAvatarFileUrl%>"/>
<c:set var="doesAvatarExist" value="${not empty dimension}"/>

<tags:page pageModel="${userAvatarModel.pageModel}">

	<eco:form action="${eco:baseUrl()}/members/${userAvatarModel.user.id}/avatar/" multipartForm="true">

		<edit:container title_t="User avatar: Avatar section title" width="700">

			<jsp:attribute name="footer">

				<html:button caption_t="User avatar: Save" onclick="saveAvatar();"/>

				<c:if test="${doesAvatarExist}">
					<html:button caption_t="User avatar: Delete avatar" onclick="deleteAvatar();"/>
				</c:if>

				<script type="text/javascript">
					function saveAvatar() {
						var formName = $( '#FormName' );
						formName.attr( 'action', '${eco:baseUrl()}/members/${userAvatarModel.user.id}/avatar/' );
						formName.submit();
					}

					function deleteAvatar() {
						if ( confirm( '${eco:translate('User avatar: Delete your avatar?')}' ) ) {
							var formName = $( '#FormName' );
							formName.attr( 'action', '${eco:baseUrl()}/members/${userAvatarModel.user.id}/avatar/delete/' );
							formName.submit();
						}
						return false;
					}
				</script>

			</jsp:attribute>

			<jsp:body>

				<c:if test="${doesAvatarExist}">
					<div class="row text-center row-bottom-padding-10">
						${eco:userAvatarImage(user.id, dimension.width, dimension.height, '', '', '')}
					</div>
				</c:if>

				<div class="row">
					<table width="100%">
						<tr>
							<td class="column-title">
								${eco:translate('User avatar: Avatar file field')}
							</td>
							<td class="column-data">
								<form:input path="userAvatarModel.avatarFile" type="file" id="${avatarFileControl}"/>
							</td>
						</tr>
					</table>
				</div>

			</jsp:body>

		</edit:container>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${userAvatarModel.bindingResult}"/>

</tags:page>