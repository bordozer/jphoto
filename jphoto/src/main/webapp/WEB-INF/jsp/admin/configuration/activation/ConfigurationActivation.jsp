<%@ page import="com.bordozer.jphoto.admin.controllers.configuration.activation.SystemConfigurationActivationModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="systemConfigurationActivationModel"
             type="com.bordozer.jphoto.admin.controllers.configuration.activation.SystemConfigurationActivationModel" scope="request"/>

<c:set var="systemConfigurationIdControl" value="<%=SystemConfigurationActivationModel.SYSTEM_CONFIGURATION_ID_FORM_CONTROL%>"/>

<tags:page pageModel="${systemConfigurationActivationModel.pageModel}">

    <form:form modelAttribute="systemConfigurationActivationModel" method="POST" action="${eco:baseAdminUrl()}/configuration/activation/">

        <table:table>

            <table:tr>

                <table:tdtext text_t="Active system configuration"/>

                <table:tddata>
                    <form:select path="${systemConfigurationIdControl}">
                        <c:forEach var="systemConfiguration" items="${systemConfigurationActivationModel.systemConfigurations}">

                            <c:set var="label" value="${systemConfiguration.name}"/>
                            <c:if test="${systemConfiguration.defaultConfiguration}">
                                <c:set var="label" value="${label} - ${eco:translate('default')}"/>
                            </c:if>
                            <c:if test="${systemConfiguration.activeConfiguration}">
                                <c:set var="label" value="${label} - ${eco:translate('This is an active configuration')}"/>
                            </c:if>

                            <form:option value="${systemConfiguration.id}" label="${label}"/>

                        </c:forEach>
                    </form:select>
                </table:tddata>

            </table:tr>

            <table:trok text_t="Set active configuration" onclick="return confirmAndSave();"/>

        </table:table>

        <script type="text/javascript">
            function confirmAndSave() {
                return confirm("${eco:translate('Set active configuration?')}");
            }
        </script>

    </form:form>

</tags:page>
