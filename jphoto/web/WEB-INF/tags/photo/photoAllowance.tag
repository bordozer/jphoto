<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="uploadAllowance" type="json.photo.upload.description.AbstractPhotoUploadAllowance" required="true" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>


<table:table width="800">

	<c:set var="uploadThisWeekPhotos" value="${uploadAllowance.uploadThisWeekPhotos}"/>
	<c:if test="${not empty uploadThisWeekPhotos}">

		<table:tr>

			<table:td>
				${eco:translate('Uploaded this week photos')}:

				<ul>
					<c:forEach var="photo" items="${uploadThisWeekPhotos}">
						<li>
							<photo:photoCard photo="${photo}" />
							, ${eco:translate('PhotoUploadAllowance: uploaded')}
							<links:photosOnDate uploadTime="${photo.uploadTime}"/>
							, ${eco:fileSizeToKb(photo.fileSize)}
							${eco:translate('Kb')}
						</li>
					</c:forEach>
				</ul>

				${eco:translate('PhotoUploadAllowance: Uploaded this week')}: ${eco:fileSizeToKb(uploadAllowance.summaryFilesSizeUploadedThisWeek)} ${eco:translate('Kb')}
			</table:td>

		</table:tr>

	</c:if>

	<c:forEach var="uploadDescription" items="${uploadAllowance.uploadAllowance}">

		<table:tr>

			<c:set var="css" value=""/>
			<c:if test="${not uploadDescription.passed}">
				<c:set var="css" value="redfont"/>
			</c:if>

			<table:td cssClass="${css}">${uploadDescription.uploadRuleDescription}</table:td>

		</table:tr>

	</c:forEach>

</table:table>