<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

	<form:form modelAttribute="photoEditDataModel" method="POST" action="${eco:baseUrl()}/photos/new/" enctype="multipart/form-data" >

		<table:table>

			<table:separatorInfo colspan="1" title="${eco:translate('Photo uploading: Select file header')}" />

			<table:tr>
				<table:td>
					<form:input path="photoFile" type="file" id="photoFile"/>
				</table:td>

				<table:trok text_t="${eco:translate('Photo uploading: Upload file button')}" />
			</table:tr>

		</table:table>

	</form:form>

	<tags:springErrorHighliting bindingResult="${photoEditDataModel.bindingResult}"/>

</tags:page>