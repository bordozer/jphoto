<%@ page import="core.general.configuration.ConfigurationTab" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/config" %>

<jsp:useBean id="systemConfigurationListModel" type="admin.controllers.configuration.list.SystemConfigurationListModel" scope="request"/>

<tags:page pageModel="${systemConfigurationListModel.pageModel}">

	<c:set var="systemConfigurations" value="${systemConfigurationListModel.systemConfigurations}" />
	<c:set var="initialConfigurationTab" value="<%=ConfigurationTab.getDefaultConfigurationTab()%>" />

	<links:configurationNew>
		<html:img id="addSystemConfigurationIcon" src="add32.png" width="32" height="32" alt="${eco:translate('Create new system configuration')}" />
	</links:configurationNew>

	<br/>
	<br/>

	<table:table width="100%" oddEven="true">

		<table:separatorInfo colspan="6" title="${eco:translate('Saved system configurations')}" />

		<c:forEach var="systemConfiguration" items="${systemConfigurations}">
			<table:tr>

				<table:tdunderlined width="20">
					<links:configurationEdit systemConfigurationId="${systemConfiguration.id}">
						<html:img id="edit_${systemConfiguration.id}" src="edit16.png" width="16" height="16" />
					</links:configurationEdit>
				</table:tdunderlined>

				<table:tdunderlined width="20">

					<c:if test="${!systemConfiguration.defaultConfiguration}">
						<html:img16 src="delete16.png" onclick="deleteConfiguration();" />
						<script type="text/javascript">
							function deleteConfiguration() {
								if ( confirm( "${eco:translate1('Delete system configuration $1?', systemConfiguration.name)}" ) ) {
									document.location.href = "${eco:baseAdminUrl()}/configuration/${systemConfiguration.id}/delete/";
								}
							}
						</script>
					</c:if>

					<c:if test="${systemConfiguration.defaultConfiguration}">
						<c:set var="deleteMessage" value="${eco:translate('The default system configuration can not be deleted')}" />
						<html:img16 src="delete16.png" onclick="showUIMessage_Information( '${deleteMessage}' );" />
					</c:if>

				</table:tdunderlined>

				<table:tdunderlined width="20">
					<c:if test="${systemConfiguration.defaultConfiguration}">
						<html:img id="${systemConfiguration.id}" src="defaultSystemConfiguration.png" width="16" height="16" alt="${eco:translate('The default configuration')}" />
					</c:if>
				</table:tdunderlined>

				<table:tdunderlined width="20">
					<c:if test="${systemConfiguration.activeConfiguration}">
						<html:img id="${systemConfiguration.id}" src="activeSysConfig.png" width="16" height="16" alt="${eco:translate('The active configuration')}" />
					</c:if>

					<c:if test="${not systemConfiguration.activeConfiguration}">
						<a href="${eco:baseAdminUrl()}/configuration/activation/?id=${systemConfiguration.id}" >
							<html:img id="${systemConfiguration.id}" src="inactiveSysConfig.png" width="16" height="16" alt="${eco:translate('Activate configuration')}" />
						</a>
					</c:if>
				</table:tdunderlined>

				<table:tdunderlined width="300">
					<links:configurationTabs systemConfiguration="${systemConfiguration}" >${systemConfiguration.name}</links:configurationTabs>
				</table:tdunderlined>
				
				<table:tdunderlined>${systemConfiguration.description}</table:tdunderlined>
				
			</table:tr>
		</c:forEach>

	</table:table>

	<tags:springErrorHighliting bindingResult="${configurationEditModel.bindingResult}"/>

</tags:page>