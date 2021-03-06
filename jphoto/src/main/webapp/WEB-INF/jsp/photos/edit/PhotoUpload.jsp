<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="com.bordozer.jphoto.ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<c:set var="photoUploadAllowance" value="${photoEditDataModel.photoUploadAllowance}"/>
<c:set var="baseUrl" value="${eco:baseUrl()}"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

    <form:form modelAttribute="photoEditDataModel" method="POST" action="${eco:baseUrl()}/photos/new/" enctype="multipart/form-data">

        <table:table width="900">

            <table:separatorInfo colspan="1" title="${eco:translate('Photo uploading: Select file header')}"/>

            <table:tr>

                <table:td>
                    <photo:photoAllowance uploadAllowance="${photoUploadAllowance}"/>
                </table:td>

            </table:tr>

            <c:if test="${photoUploadAllowance.userCanUploadPhoto}">

                <table:tr>

                    <table:td>
                        <form:input path="photoFile" type="file" id="photoFile"/>
                    </table:td>

                </table:tr>

                <table:trok text_t="Photo uploading: Upload file button"/>

            </c:if>

        </table:table>

    </form:form>

    <tags:springErrorHighliting bindingResult="${photoEditDataModel.bindingResult}"/>

</tags:page>
