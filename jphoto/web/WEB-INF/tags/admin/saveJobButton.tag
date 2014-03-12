<%@ tag import="admin.controllers.jobs.edit.AbstractAdminJobModel" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

	<%@ attribute name="jobModel" type="admin.controllers.jobs.edit.AbstractAdminJobModel" required="true" %>

<c:set var="saveJobModeFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_MODE_FORM_CONTROL%>" />
<c:set var="saveAsCopyFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_AS_COPY_FORM_CONTROL%>" />
<c:set var="saveJobNameFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_NAME_FORM_CONTROL%>" />
<c:set var="saveJobActiveFormControl" value="<%=AbstractAdminJobModel.SAVE_JOB_ACTIVE_FORM_CONTROL%>" />

<input id="${saveJobModeFormControl}" name="${saveJobModeFormControl}" type="hidden" value="1" />
<input id="${saveAsCopyFormControl}" name="${saveAsCopyFormControl}" type="hidden" value="false" />

<table:table border="0" width="100%">

	<table:tr>
		<table:td colspan="2">
			<html:img id="save" src="/icons32/save32.png" width="32" height="32" onclick="saveJob();" alt="${eco:translate1('Save \\\'$1\\\' job', jobModel.jobName)}"/>
			<html:img id="saveAsCopy" src="/icons32/saveAsCopy32.png" width="32" height="32" onclick="saveJobAsCopy();" alt="${eco:translate1('Save as copy \\\'$1\\\' job', jobModel.jobName)}"/>
			<html:img id="run" src="/icons32/start32.png" width="32" height="32" onclick="checkAndStart();" alt="${eco:translate1('Run \\\'$1\\\' job', jobModel.jobName)}"/>
		</table:td>
	</table:tr>

	<table:separatorInfo colspan="2" title="${jobModel.job.jobName}" />

	<table:tr>
		<table:td width="120">
			${eco:translate('Job name')}
		</table:td>

		<table:td>
			<html:input fieldId="${saveJobNameFormControl}" fieldValue="${jobModel.jobName}" size="60" />
		</table:td>
	</table:tr>

	<table:tr>
		<table:td>
			${eco:translate('Active')}
		</table:td>

		<table:td>
			<input type="hidden" id="${saveJobActiveFormControl}" name="${saveJobActiveFormControl}" value="${jobModel.active}">
			<html:checkbox inputId="_${saveJobActiveFormControl}" inputValue="true" isChecked="${jobModel.active}" onClick="setActive()" />
			<script type="text/javascript">
				function setActive() {
					var isChecked = $( '#_${saveJobActiveFormControl}' ).attr( 'checked' );
					if ( isChecked != undefined ) {
						$( '#${saveJobActiveFormControl}' ).val( true );
					} else {
						$( '#${saveJobActiveFormControl}' ).val( false );
					}
				}
			</script>
		</table:td>
	</table:tr>

</table:table>

<script type="text/javascript">
	function saveJob() {
		$( '#${saveJobModeFormControl}' ).val( true );
		$( '#FormName' ).attr( 'action', '${eco:baseAdminUrlWithPrefix()}/jobs/${jobModel.job.jobType.prefix}/save/' );
		$( '#FormName' ).submit();
	}

	function saveJobAsCopy() {
		if( confirmDeletion( '${eco:translate1('Save "$1" as copy?', jobModel.job.jobName)}' ) ) {
			$( '#${saveAsCopyFormControl}' ).val( true );
			saveJob();
		}
	}

	function checkAndStart() {
		$( '#${saveJobModeFormControl}' ).val( false );
		$( '#FormName' ).attr( 'action', '${eco:baseAdminUrlWithPrefix()}/jobs/${jobModel.job.jobType.prefix}/' );

		if( confirmDeletion( '${eco:translate1('Run "$1?"', jobModel.job.jobName)}' ) ) {
			$( '#FormName' ).submit();
		}
	}
</script>