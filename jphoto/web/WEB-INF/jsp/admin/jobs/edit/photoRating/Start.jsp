<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoRatingJobModel" type="admin.controllers.jobs.edit.photoRating.PhotoRatingJobModel" scope="request"/>

<tags:page pageModel="${photoRatingJobModel.pageModel}">

	<admin:jobStart jobModel="${photoRatingJobModel}" />

	<form:form action="${eco:baseAdminUrlWithPrefix()}/jobs/${photoRatingJobModel.job.jobType.prefix}/" id="FormName" name="FormName">

		<table:table width="500">

			<table:tr>
				<table:td colspan="2">
					<admin:saveJobButton jobModel="${photoRatingJobModel}" />
				</table:td>
			</table:tr>

			<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}" />

			<table:tr>
				<table:td colspan="2">
					<tags:dateRange dateRangeTypeId="${photoRatingJobModel.dateRangeTypeId}"
									dateFrom="${photoRatingJobModel.dateFrom}"
									dateTo="${photoRatingJobModel.dateTo}"
									timePeriod="${photoRatingJobModel.timePeriod}" />
				</table:td>
			</table:tr>

			<table:separator colspan="2" />

		</table:table>

		<admin:jobFinish jobModel="${photoRatingJobModel}" />

	</form:form>

	<js:confirmAction />

	<tags:springErrorHighliting bindingResult="${photoRatingJobModel.bindingResult}"/>

</tags:page>