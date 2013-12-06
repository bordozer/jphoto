<%@ tag import="admin.controllers.configuration.edit.ConfigurationEditModel" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="configuration" required="true" type="core.general.configuration.Configuration" %>

<c:set var="configurationKey" value="${configuration.configurationKey}"/>

<c:set var="configurationInfoDivId" value="<%=ConfigurationEditModel.CONFIGURATION_INFO_DIV_ID%>"/>
<c:set var="configurationEditInputId" value="<%=ConfigurationEditModel.CONFIGURATION_EDIT_INPUT_ID%>"/>

<c:set var="configurationKeyId" value="${configurationKey.id}" />
<c:set var="dataType" value="${configurationKey.dataType}" />
<c:set var="value" value="${configuration.value}" />
<c:set var="defaultConfiguration" value="${configuration.defaultSystemConfiguration}" />

<c:set var="isCheckboxDataType" value="${dataType eq 'YES_NO'}" />

<c:set var="size" value="" />

<c:if test="${dataType == 'STRING'}">
	<c:set var="align" value="center" />
	<c:set var="size" value="25" />
</c:if>

<c:if test="${dataType == 'INTEGER' || dataType == 'BYTE' || dataType == 'FLOAT'}">
	<c:set var="align" value="right" />
	<c:set var="size" value="5" />
</c:if>

<c:if test="${dataType == 'BYTE'}">
	<c:set var="align" value="right" />
	<c:set var="size" value="10" />
</c:if>

<c:if test="${dataType == 'ARRAY_OF_STRINGS'}">
	<c:set var="size" value="30" />
</c:if>

<c:if test="${dataType == 'YES_NO'}">
	<c:set var="value" value="${configuration.valueYesNo}" />
</c:if>

<c:set var="fieldCurrentValueDivId" value="${configurationInfoDivId}_${configurationKeyId}" />
<c:set var="fieldId" value="configurationMap['${configurationKeyId}'].value" />

<div style="text-align: right;">

	<c:if test="${!isCheckboxDataType}">
		<html:input fieldId="${fieldId}" fieldValue="${value}" maxLength="255" size="${size}" />
	</c:if>

	<c:if test="${isCheckboxDataType}">
		<input id="${fieldId}" name="${fieldId}" type="radio" value="1" ${configuration.valueYesNo ? 'checked' : ''} /> ${eco:translate('Yes')}
		<input id="${fieldId}" name="${fieldId}" type="radio" value="0" ${not configuration.valueYesNo ? 'checked' : ''} /> ${eco:translate('No')}
	</c:if>

</div>

