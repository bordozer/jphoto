<%@ page import="core.enums.UserTeamMemberType" %>
<%@ page import="ui.controllers.users.photoAlbums.edit.UserPhotoAlbumEditDataModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userPhotoAlbumEditDataModel" type="ui.controllers.users.photoAlbums.edit.UserPhotoAlbumEditDataModel" scope="request" />

<c:set var="user" value="${userPhotoAlbumEditDataModel.user}"/>

<c:set var="albumNameControl" value="<%=UserPhotoAlbumEditDataModel.FORM_CONTROL_PHOTO_ALBUM_NAME%>"/>
<c:set var="albumDescriptionControl" value="<%=UserPhotoAlbumEditDataModel.FORM_CONTROL_PHOTO_ALBUM_DESCRIPTION%>"/>

<c:set var="userTeamMemberTypeValues" value="<%=UserTeamMemberType.values()%>"/>

<tags:page pageModel="${userPhotoAlbumEditDataModel.pageModel}">

	<form:form modelAttribute="userPhotoAlbumEditDataModel" method="POST" action="${eco:baseUrl()}/members/${user.id}/albums/save/">

		<table:table width="700">

			<table:separatorInfo colspan="2" title="${eco:translate('PhotoAlbum: Album parameters')}" />

			<table:tr>
				<table:tdtext text_t="PhotoAlbum: Album name" />
				<table:tddata>
					<form:input path="${albumNameControl}" />
				</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="PhotoAlbum: Description" />
				<table:tddata>
					<form:textarea path="${albumDescriptionControl}" cols="60" rows="5"/>
				</table:tddata>
			</table:tr>

			<table:trok text_t="PhotoAlbum: Save button text" />

		</table:table>

	</form:form>

	<tags:springErrorHighliting bindingResult="${userPhotoAlbumEditDataModel.bindingResult}" />

</tags:page>