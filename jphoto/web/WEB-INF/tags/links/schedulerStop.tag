<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminSchedulerStopLink()%>" />

<a href ="${link}" onclick="return stopScheduler();"><jsp:doBody/></a>

<script type="text/javascript">
	function stopScheduler() {
		return confirm( "${eco:translate('SchedulerTaskList: Stop scheduler?')}" );
	}
</script>