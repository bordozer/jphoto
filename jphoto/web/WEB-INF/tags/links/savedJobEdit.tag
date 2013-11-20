<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="savedJob" type="admin.jobs.general.SavedJob" required="true" %>

<%=ApplicationContextHelper.getEntityLinkUtilsService().getAdminSavedJobLink( savedJob.getJobType(), savedJob )%>

<%--<c:set var="link" value="<%=UrlUtilsServiceImpl.getAdminSavedJobEditLink( savedJob.getJobType(), savedJob.getId() )%>" />--%>

<%--<a href="${link}" title="${eco:translate1('Edit or Run job \'$1\'', savedJob.name)}">--%>
	<%--${savedJob.name}--%>
<%--</a>--%>