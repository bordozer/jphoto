<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="configuration" required="true" type="core.general.configuration.Configuration" %>
<%@ attribute name="titlePrefix" required="false" type="java.lang.String" %>

<c:set var="configurationKey" value="${configuration.configurationKey}"/>

<c:set var="configurationKeyId" value="${configurationKey.id}" />
<c:set var="dataType" value="${configurationKey.dataType}" />
<c:set var="unit" value="${configurationKey.unit}" />
<c:set var="value" value="${configuration.value}" />
<c:set var="gotFromDefaultSystemConfiguration" value="${configuration.gotFromDefaultSystemConfiguration}" />

<c:set var="isCheckboxDataType" value="${dataType eq 'YES_NO'}" />

<c:set var="arrayElements" value="<%=configuration.getValueListString()%>" />

<c:set var="align" value="left" />
<c:set var="title" value="${configurationKey.dataType.nameTranslated}: ${value} ${unit.nameTranslated}" />

<c:if test="${dataType == 'STRING' || isCheckboxDataType}">
	<c:set var="align" value="left" />
</c:if>

<c:if test="${dataType == 'INTEGER' || dataType == 'BYTE' || dataType == 'FLOAT' || isCheckboxDataType}">
	<c:set var="align" value="right" />
</c:if>

<c:if test="${dataType == 'BYTE'}">
	<c:set var="valueInKb" value="${eco:fileSizeToKb(value)}" />
	<c:set var="valueInMb" value="${eco:fileSizeToKb(valueInKb)}" />
	<c:set var="title" value="${valueInKb} ${eco:translate('Kb')}, ${valueInMb} ${eco:translate('Mb')}" />
</c:if>

<c:if test="${dataType == 'ARRAY_OF_STRINGS' || dataType == 'ARRAY_OF_INTEGERS'}">
	<c:set var="align" value="left" />

	<c:forEach var="arrayElement" items="${arrayElements}" varStatus="status">
		<c:if test="${status.first}">
			<c:set var="value" value="${arrayElement}" />
		</c:if>
		<c:if test="${not status.first}">
			<c:set var="value" value="${value}<br />${arrayElement}" />
		</c:if>
	</c:forEach>
</c:if>

<c:if test="${isCheckboxDataType}">
	<c:set var="value" value="${configuration.valueYesNo ? eco:translate('Yes') : eco:translate('No')}" />
</c:if>

<c:set var="css" value=""/>
<c:if test="${not gotFromDefaultSystemConfiguration}">
	<c:set var="css" value="changedConfigurationValue"/>
</c:if>

<div align="${align}" class="${css}" title="${titlePrefix}${title}">
	${value}
</div>

