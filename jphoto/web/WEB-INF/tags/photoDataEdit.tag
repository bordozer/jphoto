<%@ tag import="core.general.configuration.ConfigurationKey" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" %>

<c:set var="photoNameMaxLength" value="<%=ApplicationContextHelper.getConfigurationService().getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH )%>"/>

<form:form modelAttribute="photoEditDataModel" method="post" action="${eco:baseUrl()}/photos/save/">

	<table:table width="700px" border="0">

		<table:separatorInfo colspan="2" title="${eco:translate('Photo data')}" />

		<table:tr>

			<table:tdtext text_t="Photo uploading: Name" labelFor="photoName" />
			<table:tddata>
				<form:input path="photoName" maxlength="${photoNameMaxLength}" />
			</table:tddata>

		</table:tr>

		<table:tr>

			<table:tredit>
				<table:tdtext text_t="Photo uploading: Genre" labelFor="selectedGenreId" isMandatory="true"/>

				<table:tddata>
					<form:select path="selectedGenreId" items="${photoEditDataModel.genreWrappers}" itemLabel="genreNameTranslated" itemValue="genre.id" htmlEscape="false" size="23"/>
				</table:tddata>
			</table:tredit>

		</table:tr>

		<%--<table:trok text_t="Photo uploading: Save button" onclick="alert( ';)' ); return false;"/>--%>
		<table:trok text_t="Photo uploading: Save button" />

	</table:table>

</form:form>
