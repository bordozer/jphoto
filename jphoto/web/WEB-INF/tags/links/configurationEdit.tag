<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="systemConfigurationId" required="true" type="java.lang.Integer" %>

<c:set var="configurationEditUrl" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminConfigurationEditLink( systemConfigurationId )%>" />

<a href="${configurationEditUrl}">
	<jsp:doBody/>
</a>