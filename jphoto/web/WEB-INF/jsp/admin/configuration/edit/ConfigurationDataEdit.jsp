<%@ taglib prefix="eco" uri="http://taglibs" %>
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

<tags:page pageModel="${configurationEditModel.pageModel}">

	<links:configurationTabsLine systemConfiguration="${systemConfiguration}"
								 activeConfigurationTab="${configurationEditModel.configurationTab}"
								 isEdit="true"
			/>

	<div class="configuration">

		<form:form method="POST" action="${configurationEditUrl}" modelAttribute="configurationEditModel">

			<form:hidden path="saveConfiguration"  />
			<form:hidden path="configurationTabKey" />

			<table:table border="0" width="300">

				<table:tr>
					<table:td colspan="4">
						<h3>
							${eco:translate1('Edit configuration: $1', systemConfiguration.name)}
							<c:if test="${systemConfiguration.defaultConfiguration}">
								- <span style="color: red">${eco:translate('default')}</span>
							</c:if>
						</h3>
					</table:td>
				</table:tr>

				<table:tr>
					<table:td>
						${eco:translate('Name')}
					</table:td>
					<table:td colspan="3">
						<form:input path="systemConfigurationName" size="60" />
					</table:td>
				</table:tr>

				<table:tr>
					<table:td>
						${eco:translate('Description')}
					</table:td>
					<table:td colspan="3">
						<form:textarea path="description" cols="50" rows="4" />
					</table:td>
				</table:tr>

			</table:table>

			<br />

			<html:submitButton id="save" caption_t="Save configuration" onclick="submitAndSaveConfiguration();"/>

			<script type="text/javascript">
				function submitAndSaveConfiguration() {
					var form = $( '#configurationEditModel' );
					form.attr( 'action', '${eco:baseAdminUrlWithPrefix()}/configuration/${systemConfiguration.id}/save/' );
					form.submit();
				}
			</script>

		</form:form>

	</div>

	<tags:springErrorHighliting bindingResult="${configurationEditModel.bindingResult}"/>

</tags:page>