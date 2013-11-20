<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="jobId" type="java.lang.Integer" required="true" %>

${eco:translate('Execution log:')}

<br />

<iframe id="jobExecutionLogIFrame" src="${eco:baseAdminUrlWithPrefix()}/jobs/execution/${jobId}/log/" frameborder="1" width="90%" height="300px"></iframe>
