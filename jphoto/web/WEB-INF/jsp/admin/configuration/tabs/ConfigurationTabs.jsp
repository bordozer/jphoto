<%@ page import="core.general.configuration.ConfigurationTab" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/config" %>

<jsp:useBean id="configurationTabsModel" type="admin.controllers.configuration.tabs.ConfigurationTabsModel" scope="request"/>

<tags:page pageModel="${configurationTabsModel.pageModel}">

	<c:set var="configurationTabs" value="<%=ConfigurationTab.values()%>"/>
	<c:set var="systemConfiguration" value="${configurationTabsModel.systemConfiguration}"/>

	<c:set var="configurationMap" value="${configurationTabsModel.configurationMap}"/>
	<c:set var="hasEntries" value="${configurationMap.size() > 0}"/>

	<c:set var="selectedConfigurationTab" value="${configurationTabsModel.configurationTab}"/>
	<c:set var="isAllTabs" value="${selectedConfigurationTab == 'ALL'}"/>
	<c:set var="isChabgesOnly" value="${selectedConfigurationTab == 'CHANGES_ONLY'}"/>
	<c:set var="colspan" value="5"/>

	<links:configurationTabsLine systemConfiguration="${systemConfiguration}"
								 activeConfigurationTab="${configurationTabsModel.configurationTab}"
								 isEdit="false"
			/>

	<div class="configuration">

		<table:table border="0" width="800" oddEven="true">

			<c:forEach var="configurationTab" items="${configurationTabs}">

				<c:if test="${(isAllTabs || isChabgesOnly || configurationTab == selectedConfigurationTab) && ( configurationTab != 'ALL' && configurationTab != 'CHANGES_ONLY' )}">

					<c:if test="${not(systemConfiguration.defaultConfiguration and configurationTab == 'CHANGES_ONLY')}">
						<table:tr>

							<table:tdunderlined width="20">
								<links:configurationTabEdit systemConfigurationId="${systemConfiguration.id}" configurationTab="${configurationTab}">
									<html:img id="configure" src="edit16.png" width="16" height="16" alt="${eco:translate1('Edit configuration tab: $1', configurationTab.name)}"/>
								</links:configurationTabEdit>
							</table:tdunderlined>

							<table:tdunderlined colspan="${colspan}">
								<links:configurationInfo systemConfigurationId="${systemConfiguration.id}" configurationTab="${configurationTab}">
									<h3>${eco:translate(configurationTab.name)}</h3></links:configurationInfo>
							</table:tdunderlined>

						</table:tr>
					</c:if>

					<c:forEach var="entry" items="${configurationMap}" varStatus="status">

						<c:set var="configuration" value="${entry.value}"/>
						<c:set var="configurationKey" value="${configuration.configurationKey}"/>

						<c:set var="gotFromDefaultSystemConfiguration" value="${configuration.gotFromDefaultSystemConfiguration}"/>

						<c:if test="${configurationKey.tab == configurationTab}">
							<table:tr>
								<table:tdunderlined>
									<c:if test="${not gotFromDefaultSystemConfiguration}">
										<html:img id="conf_${configurationKey.id}" src="edited16.png" width="16" height="16" alt="${eco:translate('Overrides value of default system configuration.')}" />
									</c:if>

									<c:if test="${configurationKey.editableInDefaultConfigurationOnly}">
										<c:if test="${systemConfiguration.defaultConfiguration}">
											<html:img id="conf_${configurationKey.id}" src="icons16/editable.png" width="16" height="16" alt="${eco:translate('Editable only for default system configuration')}" />
										</c:if>
										<c:if test="${not systemConfiguration.defaultConfiguration}">
											<html:img id="conf_${configurationKey.id}" src="icons16/noteditable.png" width="16" height="16" alt="${eco:translate('Editable only for default system configuration')}" />
										</c:if>
									</c:if>
								</table:tdunderlined>

								<table:tdunderlined cssClass="textcentered">
									<span title="${eco:translate1('Configuration key ID: #$1', configurationKey.id)}">${configurationKey.id}</span>
								</table:tdunderlined>

								<c:set var="css" value=""/>
								<c:if test="${not gotFromDefaultSystemConfiguration}">
									<c:set var="css" value="changedConfigurationValue"/>
								</c:if>
								<table:tdunderlined cssClass="${css}">
									<span title="${configurationKey.id}: ${configurationKey}">${eco:translate(configurationKey.description)}
								</table:tdunderlined>

								<table:tdunderlined>
									<config:valueRendererInfo configuration="${configuration}"/>
								</table:tdunderlined>

								<table:tdunderlined cssClass="textright">
									<c:if test="${configuration.defaultSystemConfiguration != null}">
										<config:valueRendererInfo configuration="${configuration.defaultSystemConfiguration}"
																  titlePrefix="${eco:translate('Default system configuration value: ')}"/>
									</c:if>
								</table:tdunderlined>

								<table:tdunderlined>${eco:translate(configurationKey.unit.name)}</table:tdunderlined>

							</table:tr>
						</c:if>

					</c:forEach>

				</c:if>

			</c:forEach>

			<c:if test="${selectedConfigurationTab == 'RANK_VOTING'}">
				<table:tr>
					<table:td />
					<table:td />
					<table:td colspan="${colspan - 1}">
						<tags:rankInGenrePointsMap ranksInGenrePointsMap="${configurationTabsModel.rankInGenrePointsMap}" />
					</table:td>
				</table:tr>
			</c:if>

		</table:table>

	</div>

	<div class="footerseparator"></div>

</tags:page>