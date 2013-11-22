<%@ page import="core.general.activity.ActivityType" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="activityStreamModel" type="controllers.activity.list.ActivityStreamModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<c:set var="separator" value="&nbsp;&nbsp;&nbsp;"/>

<tags:page pageModel="${activityStreamModel.pageModel}">

	<c:set var="activityTypeValues" value="<%=ActivityType.values()%>"/>

	<div style="float: left; width: 100%; text-align: center;">

		<c:set var="css" value=""/>
		<c:if test="${activityStreamModel.filterActivityTypeId == 0}">
			<c:set var="css" value="block-background block-border block-shadow"/>
		</c:if>

		<a href="${eco:baseUrlWithPrefix()}/activityStream/">
			<html:img32 src="jobtype/clearFilter.png" alt="${eco:translate('Reset activity filter')}" cssClass="${css}" />
		</a>

		${separator}

		<c:forEach var="activityType" items="${activityTypeValues}">

			<c:set var="css" value=""/>
			<c:if test="${activityType.id == activityStreamModel.filterActivityTypeId}">
				<c:set var="css" value="block-background block-border block-shadow"/>
			</c:if>

			<a href="${eco:baseUrlWithPrefix()}/activityStream/type/${activityType.id}/">
				<html:img32 src="jobtype/${activityType.icon}" alt="${eco:translate1('Activity filter: $1', activityType.nameTranslated)}" cssClass="${css}" />
			</a>

			${separator}

		</c:forEach>
	</div>

	<tags:paging showSummary="false"/>

	<table:table width="800">
		<table:tr>
			<table:td>
				<tags:activityStream activities="${activityStreamModel.activities}" showUserActivityLink="true" />
			</table:td>
		</table:tr>
	</table:table>


	<tags:paging showSummary="true"/>

	<div class="footerseparator" ></div>

</tags:page>