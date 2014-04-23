<%@ page import="admin.jobs.enums.JobRunMode" %>
<%@ page import="admin.controllers.jobs.edit.chain.JobChainJobModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="jobChainJobModel" type="admin.controllers.jobs.edit.chain.JobChainJobModel" scope="request"/>

<c:set var="savedRealJobs" value="${jobChainJobModel.savedJobs}"/>
<c:set var="jobRunModeValues" value="<%=JobRunMode.values()%>"/>
<c:set var="jobRunModeSeriallyId" value="<%=JobRunMode.SERIALLY.getId()%>"/>

<c:set var="selectedSavedJobsIdsControl" value="<%=JobChainJobModel.SELECTED_SAVED_JOBS_IDS_FORM_CONTROL%>"/>
<c:set var="jobRunModeIdControl" value="<%=JobChainJobModel.JOB_RUN_MODE_ID_FORM_CONTROL%>"/>
<c:set var="stopNextJobsInChainIfErrorControl" value="<%=JobChainJobModel.BREAK_CHAIN_EXECUTION_IF_ERROR_FORM_CONTROL%>"/>

<tags:page pageModel="${jobChainJobModel.pageModel}">

	<admin:jobEditData jobModel="${jobChainJobModel}">

			<jsp:attribute name="jobForm">

				<table:table width="800">

					<table:tr>
						<table:td colspan="2">
							<admin:saveJobButton jobModel="${jobChainJobModel}"/>
						</table:td>
					</table:tr>

					<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}"/>

					<table:tr>
						<table:tdtext text_t="Jobs chain" isMandatory="true"/>
						<table:td>
							<c:forEach var="savedJob" items="${savedRealJobs}">
								<form:checkbox path="${selectedSavedJobsIdsControl}" value="${savedJob.id}"/>
								<links:savedJobEdit savedJob="${savedJob}"/>
								<br/>
							</c:forEach>
						</table:td>
					</table:tr>

					<table:tr>
						<table:tdtext text_t="Job run mode"/>
						<table:td>
							<form:radiobuttons path="${jobRunModeIdControl}" items="${jobRunModeValues}" itemValue="id" itemLabel="name" delimiter="<br />" htmlEscape="false"/>
						</table:td>
					</table:tr>

					<table:tr>
						<table:tdtext text_t="Break chain execution if error"/>
						<table:td>
							<form:checkbox path="${stopNextJobsInChainIfErrorControl}" value="ON" disabled="${jobChainJobModel.jobRunModeId != jobRunModeSeriallyId}"/>
						</table:td>
					</table:tr>

				</table:table>

			</jsp:attribute>

	</admin:jobEditData>

	<script type="text/javascript">

		$( document ).ready( function () {
			setErrorHandlingVisibility();
		} );

		function setErrorHandlingVisibility() {
			var stopNextJobsControl = $( "[name='${stopNextJobsInChainIfErrorControl}']" );

			$( "input:radio[name=${jobRunModeIdControl}]" ).click( function () {
				var value = $( this ).val();
				if ( value == '${jobRunModeSeriallyId}' ) {
					stopNextJobsControl.removeAttr( 'disabled' );
				} else {
					stopNextJobsControl.attr( 'disabled', true );
				}
			} );
		}
	</script>

</tags:page>