<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="systemConfigurationId" required="true" type="java.lang.Integer" %>
<%@ attribute name="configurationTab" required="true" type="core.general.configuration.ConfigurationTab" %>

<c:set var="configurationEditUrl" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminConfigurationTabLink( systemConfigurationId, configurationTab.getKey() )%>" />

<a href="${configurationEditUrl}">
	<jsp:doBody/>
</a>