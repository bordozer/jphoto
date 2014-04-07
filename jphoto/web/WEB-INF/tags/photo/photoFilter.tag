<%@ tag import="core.general.user.UserMembershipType" %>
<%@ tag import="ui.controllers.photos.list.PhotoFilterSortColumn" %>
<%@ tag import="ui.controllers.photos.list.PhotoFilterSortOrder" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoFilterModel" type="ui.controllers.photos.list.PhotoFilterModel" scope="request"/>

<c:set var="membershipTypeListValues" value="<%=UserMembershipType.values()%>"/>
<c:set var="photoFilterSortColumnsValues" value="<%=PhotoFilterSortColumn.values()%>"/>
<c:set var="photoFilterSortOrderValues" value="<%=PhotoFilterSortOrder.values()%>"/>

<form:form modelAttribute="photoFilterModel" method="POST" action="${eco:baseUrlWithPrefix()}/photos/filter/">

	<table:table width="600">

		<table:separatorInfo colspan="2" title="${eco:translate('Photo search: Photo search')}" />

		<table:tr>
			<table:tdtext text_t="Photo search: Photo name contains"/>
			<table:tddata>
				<form:input path="filterPhotoName"/>
			</table:tddata>
		</table:tr>

		<table:tr>
			<table:tdtext text_t="Photo search: Photo category"/>
			<table:tddata>
				<form:select path="filterGenreId" >
					<form:option value="-1" label="- - - - - -"/>
					<form:options items="${photoFilterModel.filterGenres}" itemValue="id" itemLabel="name"/>
				</form:select>
			</table:tddata>
		</table:tr>

		<table:tr>
			<table:tdtext text_t="Photo search: Show photos with nude content" />
			<table:tddata>
				<form:checkbox path="showPhotosWithNudeContent"/>
			</table:tddata>
		</table:tr>

		<table:separator colspan="2" />

		<table:tr>
			<table:tdtext text_t="Photo search: Author name contains"/>
			<table:tddata>
				<form:input path="filterAuthorName"/>
			</table:tddata>
		</table:tr>

		<table:tr>
			<table:tdtext text_t="Photo search: Author membership type"/>
			<table:tddata>
				<form:checkboxes path="photoAuthorMembershipTypeIds" items="${membershipTypeListValues}" itemValue="id" itemLabel="name" delimiter="<br />"/>
			</table:tddata>
		</table:tr>

		<table:separator colspan="2" />

		<table:tr>
			<table:tdtext text_t="Photo search: Photo filter: Sort by"/>
			<table:tddata>
				<form:select path="photosSortColumn" items="${photoFilterSortColumnsValues}" itemValue="id" itemLabel="name" />
				<br />
				<form:radiobuttons path="photosSortOrder" items="${photoFilterSortOrderValues}" itemValue="id" itemLabel="name" delimiter="<br />" />
			</table:tddata>
		</table:tr>

		<table:separator colspan="2" />

		<table:trok text_t="Photo search: Do search photos button" />

	</table:table>

	<tags:springErrorHighliting bindingResult="${photoFilterModel.bindingResult}" />

</form:form>