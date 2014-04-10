<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminSchedulerRunLink()%>" />

<a href ="${link}" onclick="return runScheduler();"><jsp:doBody/></a>

<script type="text/javascript">
	function runScheduler() {
		return confirm( "${eco:translate('SchedulerTaskList: Run scheduler?')}" );
	}
</script>