<%@ page import="com.bordozer.jphoto.admin.controllers.jobs.edit.preview.PreviewGenerationModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="previewGenerationModel" type="com.bordozer.jphoto.admin.controllers.jobs.edit.preview.PreviewGenerationModel" scope="request"/>

<c:set var="previewSizeControl" value="<%=PreviewGenerationModel.PREVIEW_SIZE_FORM_CONTROL%>"/>
<c:set var="savedJob" value="${previewGenerationModel.job}"/>

<tags:page pageModel="${previewGenerationModel.pageModel}">

    <admin:jobEditData jobModel="${previewGenerationModel}">

		<jsp:attribute name="jobForm">

			<div class="row">
                <div class="col-lg-7 text-right">
                        ${eco:translate('Preview generation job: Preview size')}:
                </div>
                <div class="col-lg-5">
                    <form:input path="${previewSizeControl}" cssErrorClass="invalid" size="4"/>
                </div>
            </div>

			<div class="row">
                <div class="col-lg-7 text-right">
                        ${eco:translate('Preview generation job: Skip generation if preview already exists')}
                </div>
                <div class="col-lg-5">
                    <form:checkbox path="skipPhotosWithExistingPreview"/>
                </div>
            </div>

		</jsp:attribute>

    </admin:jobEditData>

</tags:page>
