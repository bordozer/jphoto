<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="com.bordozer.jphoto.ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<c:set var="photo" value="${photoEditDataModel.photo}"/>
<c:set var="isNew" value="<%=photoEditDataModel.isNew()%>"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

    <div class="floatleft">

        <div class="floatleft">

            <div class="floatleft text-centered" style="width: 400px; vertical-align: top;">
                <table:table width="400px" border="0">
                    <table:separatorInfo colspan="1"
                                         title="${eco:translate(isNew ? 'Photo uploading: Photo uploading' : 'Photo uploading: Photo data editing')}"/>
                    <table:tr>
                        <table:td>

                            <c:if test="${isNew}">
                                <img src="${eco:baseUrl()}/download/file/?filePath=${photoEditDataModel.tempPhotoFile}" alt="Photo file" width="300px">
                                <br/>
                                <br/>
                                ${eco:translate('Photo uploading: File size')}: ${eco:fileSizeToKb(photo.fileSize)} ${eco:translate('Kb')}
                                <br/>
                                <br/>
                                <c:set var="dimension" value="${photoEditDataModel.photoDimension}"/>
                                ${eco:translate('Photo uploading: Image dimension width')}: ${dimension.width} ${eco:translate('px')}
                                <br/>
                                ${eco:translate('Photo uploading: Image dimension height')}: ${dimension.height} ${eco:translate('px')}
                            </c:if>

                            <c:if test="${not isNew}">
                                <%--<photo:photoPreview photo="${photo}" />--%>
                                <links:photoCard id="${photo.id}">
                                    <img src="${eco:baseUrl()}/download/photos/${photo.id}/" class="photo-preview-image" style="vertical-align: middle;"
                                         width="300px"/>
                                </links:photoCard>
                                <br/>
                                <br/>
                                <photo:photoCard photo="${photo}"/>
                            </c:if>

                        </table:td>
                    </table:tr>

                </table:table>

            </div>

            <div style="display: inline-block;">
                <tags:photoDataEdit photoEditDataModel="${photoEditDataModel}"/>
            </div>

        </div>

    </div>

    <tags:springErrorHighliting bindingResult="${photoEditDataModel.bindingResult}"/>

    <div class="footerseparator"></div>

</tags:page>
