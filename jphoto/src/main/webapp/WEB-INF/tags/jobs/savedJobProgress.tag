<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="savedJob" type="com.bordozer.jphoto.admin.jobs.general.SavedJob" required="true" %>
<%@ attribute name="jobId" type="java.lang.Integer" required="true" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminSavedJobProgressLink( savedJob.getJobType(), jobId )%>"/>

<a href="${link}" title="${eco:translate1('\'$1\' - progress', savedJob.name)}">
    ${savedJob.name}
</a>
