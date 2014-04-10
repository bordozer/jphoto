<%@ tag import="admin.controllers.scheduler.tasks.edit.SchedulerTaskEditModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="schedulerTaskTime" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_TIME_CONTROL%>" />

<span onclick="setExecutionTime( '00:00:00' );" title="${eco:translate('Set execution time to midnight')}">00:00:00</span>

<script type="text/javascript">
	function setExecutionTime( executionTime ) {
		$( '#${schedulerTaskTime}' ).val( executionTime );
	}
</script>