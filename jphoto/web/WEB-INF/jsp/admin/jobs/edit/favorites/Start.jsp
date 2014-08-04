<%@ page import="admin.controllers.jobs.edit.favorites.FavoritesJobModel" %>
<%@ page import="core.enums.FavoriteEntryType" %>
<%@ page import="ui.translatable.GenericTranslatableList" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="favoritesJobModel" type="admin.controllers.jobs.edit.favorites.FavoritesJobModel" scope="request"/>

<c:set var="actionsQtyControl" value="<%=FavoritesJobModel.ACTIONS_QTY_FORM_CONTROL%>"/>
<c:set var="photosQtyControl" value="<%=FavoritesJobModel.PHOTO_QTY_FORM_CONTROL%>"/>
<c:set var="favoriteEntriesIdsControl" value="<%=FavoritesJobModel.FAVORITE_ENTRIES_IDS_FORM_CONTROL%>"/>

<c:set var="favoriteEntriesIds" value="<%=GenericTranslatableList.favoriteEntryTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>

<tags:page pageModel="${favoritesJobModel.pageModel}">

	<admin:jobEditData jobModel="${favoritesJobModel}">

		<jsp:attribute name="jobForm">

			<table:table width="700">
				<table:tr>
					<table:td colspan="2">
						<admin:saveJobButton jobModel="${favoritesJobModel}"/>
					</table:td>
				</table:tr>

				<table:separatorInfo colspan="2" title="${eco:translate('Job JSP: Job parameters')}"/>

				<table:tr>
					<table:tdtext text_t="FavoritesJob: Total job steps" isMandatory="true"/>
					<table:tddata>
						<form:input path="${actionsQtyControl}" size="4"/>
					</table:tddata>
				</table:tr>

				<table:tr>
					<table:tdtext text_t="FavoritesJob: Photos"/>
					<table:tddata>
						<form:input path="${photosQtyControl}" size="4"/>
					</table:tddata>
				</table:tr>

				<table:tr>
					<table:tdtext text_t="FavoritesJob: Favorite entries"/>
					<table:tddata>
						<js:checkboxMassChecker checkboxClass="favoriteEntriesIds"/>
						<br/>
						<form:checkboxes path="${favoriteEntriesIdsControl}" items="${favoriteEntriesIds}" itemValue="id" itemLabel="name" delimiter="<br />" cssClass="favoriteEntriesIds"/>
						<br/>
						<br/>
						${eco:translate('Each action generates one of selected favorite entries randomly')}
					</table:tddata>
				</table:tr>

			</table:table>

		</jsp:attribute>

	</admin:jobEditData>

</tags:page>