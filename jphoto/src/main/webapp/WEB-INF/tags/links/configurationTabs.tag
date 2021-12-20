<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="systemConfiguration" required="true" type="com.bordozer.jphoto.core.general.configuration.SystemConfiguration" %>

<c:set var="configurationEditUrl" value="<%=ApplicationContextHelper.getUrlUtilsService().getAdminConfigurationTabsLink( systemConfiguration.getId() )%>"/>

<a href="${configurationEditUrl}">
    <jsp:doBody/>
</a>
