<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="savedJob" type="com.bordozer.jphoto.admin.jobs.general.SavedJob" required="true" %>
<%@ attribute name="showIcon" type="java.lang.Boolean" required="false" %>

<c:set var="jobType" value="<%=savedJob.getJobType()%>"/>

<c:if test="${showIcon}">
    <html:img16 src="jobtype/${jobType.icon}"/>
</c:if>

<%=ApplicationContextHelper.getEntityLinkUtilsService().getAdminSavedJobLink(savedJob.getJobType(), savedJob, EnvironmentContext.getLanguage())%>
