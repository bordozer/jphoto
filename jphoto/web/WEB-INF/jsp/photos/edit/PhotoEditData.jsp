<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<c:set var="photo" value="${photoEditDataModel.photo}" />
<c:set var="isNew" value="${photoEditDataModel.new}" />

<tags:page pageModel="${photoEditDataModel.pageModel}">

	<div class="floatleft">

		<div class="floatleft text-centered">

			<div class="floatleft" style="width: 400px; vertical-align: top;">
				<table:table width="400px" border="0">
					<table:separatorInfo colspan="1" title="${eco:translate(isNew ? 'Photo uploading: Photo uploading' : 'Photo uploading: Photo data editing')}" />
					<table:tr>
						<table:td>

							<c:if test="${isNew}">
								<img src="${eco:baseUrl()}/download/file/?filePath=${photoEditDataModel.tempPhotoFile}" alt="Photo file" width="300px">
							</c:if>

							<c:if test="${not isNew}">
								<photo:photoPreview photo="${photo}" />
								<br />
								<br />
								<photo:photoCard photo="${photo}" />
							</c:if>

						</table:td>
					</table:tr>
				</table:table>
			</div>

			<div style="display: inline-block;">
				<tags:photoDataEdit photoEditDataModel="${photoEditDataModel}" />
			</div>

		</div>

	</div>

	<tags:springErrorHighliting bindingResult="${photoEditDataModel.bindingResult}"/>

	<div class="footerseparator"></div>

</tags:page>