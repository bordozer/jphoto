<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" scope="request"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

	<div class="floatleft">

		<div class="floatleft text-centered">

			<div class="floatleft" style="width: 350px; vertical-align: top;">
				<table:table width="350px" border="0">
					<table:separatorInfo colspan="1" title="${eco:translate('Photo')}" />
					<table:tr>
						<table:td>
							<img src="${eco:baseUrl()}/download/file/?filePath=${photoEditDataModel.tempPhotoFile}" alt="Photo file" width="300px">
						</table:td>
					</table:tr>
				</table:table>
			</div>

			<div style="display: inline-block;">
				<tags:photoDataEdit photoEditDataModel="${photoEditDataModel}" />
			</div>

		</div>

	</div>

</tags:page>