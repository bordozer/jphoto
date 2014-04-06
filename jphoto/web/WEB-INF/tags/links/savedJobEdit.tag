<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="savedJob" type="admin.jobs.general.SavedJob" required="true" %>

<%=ApplicationContextHelper.getEntityLinkUtilsService().getAdminSavedJobLink( savedJob.getJobType(), savedJob, EnvironmentContext.getLanguage() )%>
