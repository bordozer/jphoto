<%@ tag import="core.general.user.UserMembershipType" %>
<%@ tag import="ui.controllers.photos.list.PhotoFilterSortColumn" %>
<%@ tag import="ui.controllers.photos.list.PhotoFilterSortOrder" %>
<%@ tag import="ui.translatable.GenericTranslatableList" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoFilterModel" type="ui.controllers.photos.list.PhotoFilterModel" scope="request"/>

<c:set var="membershipTypeListValues"
	   value="<%=GenericTranslatableList.userMembershipTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="photoFilterSortColumnsValues"
	   value="<%=GenericTranslatableList.photoFilterSortColumnTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="photoFilterSortOrderValues"
	   value="<%=GenericTranslatableList.photoFilterSortOrderTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>

<form:form modelAttribute="photoFilterModel" method="POST" action="${eco:baseUrl()}/photos/filter/">

	<div class="panel panel-info photo-filter-panel photo-filter-panel-collapsed" style="width: 700px;">

		<div class="panel-heading">

			<div class="btn-group pull-right">
				<button class="btn btn-default photo-filter-collapse-button" onclick="toggleFilterVisibility(); return false;">_</button>
				<button class="btn btn-default photo-filter-expand-button" onclick="toggleFilterVisibility(); return false;">+</button>
			</div>

			<h3 class="panel-title">
				${eco:translate('Photo search: Photo filter')}
			</h3>
		</div>

		<div class="panel-body">

			<table:table width="600">

				<table:tr>
					<table:tdtext text_t="Photo search: Photo name contains"/>
					<table:tddata>
						<form:input path="filterPhotoName"/>
					</table:tddata>
				</table:tr>

				<table:tr>
					<table:tdtext text_t="Photo search: Photo category"/>
					<table:tddata>
						<form:select path="filterGenreId">
							<form:option value="-1" label="- - - - - -"/>
							<form:options items="${photoFilterModel.genreWrappers}" itemValue="genre.id" itemLabel="genreNameTranslated"/>
						</form:select>
					</table:tddata>
				</table:tr>

				<table:tr>
					<table:tdtext text_t="Photo search: Show photos with nude content"/>
					<table:tddata>
						<form:checkbox path="showPhotosWithNudeContent"/>
					</table:tddata>
				</table:tr>

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

				<table:tr>
					<table:tdtext text_t="Photo search: Photo filter: Sort by"/>
					<table:tddata>
						<form:select path="photosSortColumn" items="${photoFilterSortColumnsValues}" itemValue="id" itemLabel="name"/>
						<br/>
						<form:radiobuttons path="photosSortOrder" items="${photoFilterSortOrderValues}" itemValue="id" itemLabel="name" delimiter="<br />"/>
					</table:tddata>
				</table:tr>

			</table:table>

		</div>

		<div class="panel-footer">
			<html:submitButton id="submit_photos_search" caption_t="Photo search: Do search photos button" />
		</div>

	</div>

	<script type="text/javascript">

		function toggleFilterVisibility() {
			require( [ 'jquery' ], function ( $ ) {
				$( '.photo-filter-panel' ).toggleClass( 'photo-filter-panel-collapsed' );
			});
		}
	</script>

	<tags:springErrorHighliting bindingResult="${photoFilterModel.bindingResult}"/>

</form:form>
