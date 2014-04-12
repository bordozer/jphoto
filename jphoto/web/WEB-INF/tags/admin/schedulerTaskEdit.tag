<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="schedulerTask" type="core.general.scheduler.SchedulerTask" required="true" %>

<a href="${eco:baseAdminUrl()}/scheduler/tasks/${schedulerTask.id}/edit/" title="${eco:translate2('Edit scheduler task $1: \'$2\'', schedulerTask.id, schedulerTask.name)}">
	<jsp:doBody/>
</a>