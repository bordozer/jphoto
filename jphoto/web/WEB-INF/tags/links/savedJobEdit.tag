<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="savedJob" type="admin.jobs.general.SavedJob" required="true" %>

<%=ApplicationContextHelper.getEntityLinkUtilsService().getAdminSavedJobLink( savedJob.getJobType(), savedJob, EnvironmentContext.getLanguage() )%>
