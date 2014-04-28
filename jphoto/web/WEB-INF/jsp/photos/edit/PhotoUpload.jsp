<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

	<form:form modelAttribute="photoEditDataModel" method="POST" action="${eco:baseUrl()}/photos/new/" enctype="multipart/form-data" >

		<div class="floatleft">

			${eco:translate('PhotoEditData: Select photo file')}:
			<form:input path="photoFile" type="file" id="photoFile"/>

			<br />

			<html:submitButton id="" caption_t="PhotoEditData: Upload photo file button" />

		</div>

	</form:form>

</tags:page>