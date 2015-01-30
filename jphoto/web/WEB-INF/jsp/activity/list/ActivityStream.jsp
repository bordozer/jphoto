<%@ page import="ui.activity.ActivityType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="activityStreamModel" type="ui.controllers.activity.list.ActivityStreamModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<tags:page pageModel="${activityStreamModel.pageModel}">

	<tags:activityStreamFilter activityTypeValues="<%=ActivityType.SYSTEM_ACTIVITIES%>"
							   filterActivityTypeId="${activityStreamModel.filterActivityTypeId}"
							   url="${eco:baseUrl()}/activityStream/"/>

	<tags:paging showSummary="false"/>

	<tags:activityStream activities="${activityStreamModel.activities}" showUserActivityLink="true"/>

	<tags:paging showSummary="true"/>

</tags:page>