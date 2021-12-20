<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="systemConfigurationId" required="true" type="java.lang.Integer" %>
<%@ attribute name="configurationTab" required="true" type="com.bordozer.jphoto.core.general.configuration.ConfigurationTab" %>

<a href="#" onclick="submitConfigurationForm( '${configurationTab.key}' );">
    <jsp:doBody/>
</a>
