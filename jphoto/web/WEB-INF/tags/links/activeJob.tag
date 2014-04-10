<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="savedJob" type="admin.jobs.general.SavedJob" required="true" %>
<%@ attribute name="jobId" type="java.lang.Integer" required="true" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminSavedJobProgressLink( savedJob.getJobType(), jobId )%>" />
<a href ="${link}"><jsp:doBody/></a>
