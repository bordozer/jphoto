<%@ tag import="core.general.configuration.ConfigurationTab" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="systemConfiguration" required="true" type="core.general.configuration.SystemConfiguration" %>
<%@ attribute name="activeConfigurationTab" required="true" type="core.general.configuration.ConfigurationTab" %>
<%@ attribute name="isEdit" required="false" type="java.lang.Boolean" %>

<c:set var="configurationTabs" value="<%=ConfigurationTab.values()%>"/>
<c:set var="systemConfigurationId" value="${systemConfiguration.id}"/>

<c:set var="splitter" value="&nbsp;<br />"/>

<div style="float: left; width: 320px; text-align: left; height: auto; line-height: 30px; margin-left: 30px; margin-top: 30px; border-right: solid 1px #C4C4C4;">

	<c:if test="${isEdit}">

		<c:if test="${empty activeConfigurationTab}"><b></c:if>
			<a href="#" onclick="submitProperties();">${eco:translate('Properties')}</a>
		<c:if test="${empty activeConfigurationTab}"></b></c:if>

		<script type="text/javascript">
			function submitProperties() {
				$( '#configurationTabKey' ).val( '' );
				$( '#resetConfigurationKey' ).val( '' );
				$( '#configurationEditModel' ).submit();
			}

			function submitConfigurationForm( configurationTabKey ) {
				$( '#configurationTabKey' ).val( configurationTabKey );
				$( '#resetConfigurationKey' ).val( '' );
				$( '#configurationEditModel' ).submit();
			}
		</script>

		${splitter}
	</c:if>

	<c:forEach var="configurationTab" items="${configurationTabs}" varStatus="status">

		<c:if test="${not ( systemConfiguration.defaultConfiguration and configurationTab == 'CHANGES_ONLY' )}">

			<c:if test="${configurationTab == activeConfigurationTab}">
				<b>
			</c:if>

			<c:if test="${not isEdit}">
				<links:configurationTabEdit systemConfigurationId="${systemConfiguration.id}" configurationTab="${configurationTab}">
					<html:img8 src="edit16.png" alt="${eco:translate1('Edit configuration tab: $1', configurationTab.name)}"/>
				</links:configurationTabEdit>

				<links:configurationInfo systemConfigurationId="${systemConfigurationId}" configurationTab="${configurationTab}">
					${eco:translate(configurationTab.name)}
				</links:configurationInfo>
			</c:if>

			<c:if test="${isEdit}">
				<links:configurationTabEditPost systemConfigurationId="${systemConfigurationId}" configurationTab="${configurationTab}">
					${eco:translate(configurationTab.name)}
				</links:configurationTabEditPost>
			</c:if>

			<c:if test="${configurationTab == activeConfigurationTab}">
				</b>
			</c:if>

			<c:if test="${not status.last}">
				${splitter}
			</c:if>

		</c:if>

	</c:forEach>

</div>