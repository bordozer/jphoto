<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="jobId" type="java.lang.Integer" required="true" %>

${eco:translate('Job execution log')}:

<br />

<iframe id="jobExecutionLogIFrame" src="${eco:baseAdminUrl()}/jobs/execution/${jobId}/log/" frameborder="1" width="90%" height="390px"></iframe>
