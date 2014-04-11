<%@ tag import="ui.activity.ActivityType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="activityTypeValues" required="true" type="java.util.List" %>
<%@ attribute name="filterActivityTypeId" required="true" type="java.lang.Integer" %>
<%@ attribute name="url" required="true" type="java.lang.String" %>

<c:set var="separator" value="&nbsp;&nbsp;&nbsp;"/>

<div style="float: left; width: 100%; text-align: center;">

	<c:set var="css" value=""/>
	<c:if test="${filterActivityTypeId == 0}">
		<c:set var="css" value="block-background block-border block-shadow"/>
	</c:if>

	<a href="${url}">
		<html:img32 src="jobtype/clearFilter.png" alt="${eco:translate('Reset activity filter')}" cssClass="${css}"/>
	</a>

	${separator}

	<c:forEach var="activityType" items="${activityTypeValues}">

		<c:set var="css" value=""/>
		<c:if test="${activityType.id == filterActivityTypeId}">
			<c:set var="css" value="block-background block-border block-shadow"/>
		</c:if>

		<a href="${url}type/${activityType.id}/">
			<html:img32 src="jobtype/${activityType.icon}" alt="${eco:translate(activityType.name)}" cssClass="${css}"/>
		</a>

		${separator}

	</c:forEach>
</div>