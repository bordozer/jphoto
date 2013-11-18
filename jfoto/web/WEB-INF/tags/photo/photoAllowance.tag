<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="uploadAllowance" type="controllers.photos.edit.description.AbstractPhotoUploadAllowance" required="true" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<div style="float: left; width: 100%;">

	<table:table width="800">

		<table:tr>
			<table:td>${eco:translate1('Your status is $1', uploadAllowance.photoAuthor.userStatus.nameTranslated )}</table:td>
		</table:tr>

		<c:set var="uploadThisWeekPhotos" value="${uploadAllowance.uploadThisWeekPhotos}"/>
		<c:if test="${not empty uploadThisWeekPhotos}">
			<table:tr>
				<table:td>
					${eco:translate('Uploaded this week photos:')}
					<ul>
						<c:forEach var="photo" items="${uploadThisWeekPhotos}">
							<li><photo:photoCard photo="${photo}" />
								, ${eco:translate('uploaded')} <links:photosOnDate uploadTime="${photo.uploadTime}"/>
								, ${eco:fileSizeToKb(photo.fileSize)} ${eco:translate('Kb')}
							</li>
						</c:forEach>
					</ul>
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
</div>