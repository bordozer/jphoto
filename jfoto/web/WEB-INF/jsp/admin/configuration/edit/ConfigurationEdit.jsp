<%@ page import="admin.controllers.configuration.edit.ConfigurationEditModel" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/config" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<jsp:useBean id="configurationEditModel" type="admin.controllers.configuration.edit.ConfigurationEditModel" scope="request"/>

<c:set var="systemConfiguration" value="${configurationEditModel.systemConfiguration}"/>

<c:set var="configurationEditUrl" value="${eco:baseAdminUrlWithPrefix()}/configuration/${systemConfiguration.id}/edit/"/>
<c:set var="hasEntries" value="${configurationEditModel.configurationMap.size() > 0}"/>
<c:set var="activeConfigurationTab" value="${configurationEditModel.configurationTab}"/>

<c:set var="configurationInfoDivId" value="<%=ConfigurationEditModel.CONFIGURATION_INFO_DIV_ID%>"/>

<tags:page pageModel="${configurationEditModel.pageModel}">

	<c:set var="configurationMap" value="${configurationEditModel.configurationMap}"/>
	<c:set var="selectedConfigurationTab" value="${configurationEditModel.configurationTab}"/>

	<c:set var="isAllTabs" value="${selectedConfigurationTab == 'ALL'}"/>
	<c:set var="isChabgesOnly" value="${selectedConfigurationTab == 'CHANGES_ONLY'}"/>

	<links:configurationTabsLine systemConfiguration="${systemConfiguration}"
								 activeConfigurationTab="${activeConfigurationTab}"
								 isEdit="true"
			/>

	<div class="configuration">

		<c:set var="oldConfigurationTab" value=""/>

		<form:form method="POST" action="${configurationEditUrl}" modelAttribute="configurationEditModel">

			<form:hidden path="saveConfiguration"  />
			<form:hidden path="configurationTabKey" />
			<form:hidden path="resetConfigurationKey" />

			<c:set var="colspan" value="6"/>

			<table:table border="0" width="900">

				<table:tr>
					<table:td colspan="${colspan}">
						<h3>
							${eco:translate1('Edit configuration: $1', systemConfiguration.name)}
							<c:if test="${systemConfiguration.defaultConfiguration}">
								- <span style="color: red">${eco:translate('default')}</span>
							</c:if>
						</h3>
					</table:td>
				</table:tr>

				<c:forEach var="entry" items="${configurationMap}">

					<c:set var="configuration" value="${entry.value}"/>
					<c:set var="configurationKey" value="${configuration.configurationKey}"/>
					<c:set var="gotFromDefaultSystemConfiguration" value="${configuration.gotFromDefaultSystemConfiguration}"/>

					<c:if test="${isAllTabs or ( isChabgesOnly and ( not configuration.gotFromDefaultSystemConfiguration) ) or ( selectedConfigurationTab == configurationKey.tab ) }">

						<c:if test="${oldConfigurationTab != configurationKey.tab}">
							<table:tr>
								<table:td colspan="${colspan}">
									<a href="#" onclick="submitConfigurationForm( '${configurationKey.tab.key}' ); return false;"><h3>${eco:translate(configurationKey.tab.name)}</h3>
								</table:td>
							</table:tr>
						</c:if>
						<c:set var="oldConfigurationTab" value="${configurationKey.tab}"/>

						<table:tr>
							<table:td width="20">
								<c:if test="${not gotFromDefaultSystemConfiguration}">
									<a href="#" onclick="resetValueToDefault( ${configurationKey.id} );">
										<html:img id="conf_${configurationKey.id}" src="edited16.png" width="16" height="16" alt="${eco:translate('Overrided value of default system configuration. Click to reset to default value.')}" />
									</a>
								</c:if>

								<c:if test="${configurationKey.editableInDefaultConfigurationOnly}">
									<c:if test="${systemConfiguration.defaultConfiguration}">
										<html:img id="conf_${configurationKey.id}" src="icons16/editable.png" width="16" height="16" alt="${eco:translate('Editable only for default system configuration')}" />
									</c:if>
									<c:if test="${not systemConfiguration.defaultConfiguration}">
										<html:img id="conf_${configurationKey.id}" src="icons16/noteditable.png" width="16" height="16" alt="${eco:translate('Editable only for default system configuration')}" />
									</c:if>
								</c:if>
							</table:td>

							<table:tdunderlined width="40">
								<span title="${eco:translate1('Configuration key ID: #$1', configurationKey.id)}">${configurationKey.id}</span>
							</table:tdunderlined>

							<table:tdunderlined>
								<span title="${configurationKey.id}: ${configurationKey}">
									<c:if test="${not gotFromDefaultSystemConfiguration}"><b></c:if>
										${eco:translate(configurationKey.description)}
									<c:if test="${not gotFromDefaultSystemConfiguration}"></b></c:if>
								</span>
							</table:tdunderlined>

							<table:tdunderlined>
								<c:if test="${not configurationKey.editableInDefaultConfigurationOnly}">
									<config:valueRendererEdit configuration="${configuration}"/>
								</c:if>
								<c:if test="${configurationKey.editableInDefaultConfigurationOnly}">
									<c:if test="${systemConfiguration.defaultConfiguration}">
										<config:valueRendererEdit configuration="${configuration}"/>
									</c:if>
									<c:if test="${not systemConfiguration.defaultConfiguration}">
										<config:valueRendererInfo configuration="${configuration}"/>
									</c:if>
								</c:if>
							</table:tdunderlined>

							<table:tdunderlined cssClass="textright">
								<c:if test="${configuration.defaultSystemConfiguration != null}">
									<config:valueRendererInfo configuration="${configuration.defaultSystemConfiguration}"/>
								</c:if>
							</table:tdunderlined>

							<table:tdunderlined>${configurationKey.unit.nameTranslated}</table:tdunderlined>
						</table:tr>

					</c:if>

				</c:forEach>

				<c:if test="${hasEntries}">
					<table:tr>
						<table:td/>
						<table:td/>
						<table:td>
							<html:submitButton id="save" caption_t="Save configuration" onclick="submitAndSaveConfiguration();"/>
							<script type="text/javascript">
								function submitAndSaveConfiguration() {
									var form = $( '#configurationEditModel' );
									form.attr( 'action', '${eco:baseAdminUrlWithPrefix()}/configuration/${systemConfiguration.id}/save/' );
									form.submit();
								}

								function resetValueToDefault( configurationKeyId ) {
									if ( confirm( "${eco:translate('Reset value to the value of the default configuration?')}" ) ) {
										$( '#configurationTabKey' ).val( '${selectedConfigurationTab.key}' );
										$( '#resetConfigurationKey' ).val( configurationKeyId );
										$( '#configurationEditModel' ).submit();
									}
								}
							</script>
						</table:td>
						<table:td colspan="${colspan - 2}" />
					</table:tr>
				</c:if>

				<c:if test="${selectedConfigurationTab == 'RANK_VOTING'}">
					<table:tr>
						<table:td />
						<table:td />
						<table:td colspan="${colspan - 1}">
							<tags:rankInGenrePointsMap ranksInGenrePointsMap="${configurationEditModel.rankInGenrePointsMap}" />
						</table:td>
					</table:tr>
				</c:if>

			</table:table>

			<br />

			<c:if test="${!hasEntries}">
				${eco:translate("There are no configurations on this tab")}
			</c:if>

		</form:form>

	</div>

	<div class="footerseparator"></div>

	<tags:springErrorHighliting bindingResult="${configurationEditModel.bindingResult}"/>

</tags:page>