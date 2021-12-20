<%@ tag import="com.bordozer.jphoto.core.general.configuration.ConfigurationTab" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="systemConfiguration" required="true" type="com.bordozer.jphoto.core.general.configuration.SystemConfiguration" %>
<%@ attribute name="activeConfigurationTab" required="true" type="com.bordozer.jphoto.core.general.configuration.ConfigurationTab" %>
<%@ attribute name="isEdit" required="false" type="java.lang.Boolean" %>

<c:set var="configurationTabs" value="<%=ConfigurationTab.values()%>"/>
<c:set var="systemConfigurationId" value="${systemConfiguration.id}"/>

<ul class="nav nav-pills nav-stacked">

    <c:if test="${isEdit}">

        <li role="presentation" class="${empty activeConfigurationTab ? 'active' : ''}"><a href="#"
                                                                                           onclick="submitProperties();">${eco:translate('Configuration tabs: Properties')}</a>
        </li>

        <script type="text/javascript">
            function submitProperties() {
                $('#configurationTabKey').val('');
                $('#resetConfigurationKey').val('');
                $('#configurationEditModel').submit();
            }

            function submitConfigurationForm(configurationTabKey) {
                $('#configurationTabKey').val(configurationTabKey);
                $('#resetConfigurationKey').val('');
                $('#configurationEditModel').submit();
            }
        </script>

    </c:if>

    <c:forEach var="configurationTab" items="${configurationTabs}" varStatus="status">

        <c:if test="${not ( systemConfiguration.defaultConfiguration and configurationTab == 'CHANGES_ONLY' )}">

            <c:set var="isSelectedTab" value="${configurationTab == activeConfigurationTab}"/>

            <li role="presentation" class="${isSelectedTab ? 'active' : ''}">
                <c:if test="${not isEdit}">
                    <%--<links:configurationTabEdit systemConfigurationId="${systemConfiguration.id}" configurationTab="${configurationTab}">
                        <html:img8 src="edit16.png" alt="${eco:translate1('Links: Edit configuration tab: $1', configurationTab.name)}"/>
                    </links:configurationTabEdit>--%>


                    <links:configurationInfo systemConfigurationId="${systemConfigurationId}" configurationTab="${configurationTab}">
                        ${eco:translate(configurationTab.name)}
                    </links:configurationInfo>
                </c:if>

                <c:if test="${isEdit}">
                    <links:configurationTabEditPost systemConfigurationId="${systemConfigurationId}" configurationTab="${configurationTab}">
                        ${eco:translate(configurationTab.name)}
                    </links:configurationTabEditPost>
                </c:if>

            </li>
        </c:if>

    </c:forEach>

</ul>
