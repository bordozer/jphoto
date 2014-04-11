<%@ page import="ui.activity.ActivityType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="activityStreamModel" type="ui.controllers.activity.list.ActivityStreamModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<tags:page pageModel="${activityStreamModel.pageModel}">

	<div class="floatleft">
		<tags:activityStreamFilter activityTypeValues="<%=ActivityType.SYSTEM_ACTIVITIES%>"
								   filterActivityTypeId="${activityStreamModel.filterActivityTypeId}"
								   url="${eco:baseUrlWithPrefix()}/activityStream/"/>

		<tags:paging showSummary="false"/>

		<table:table width="800">
			<table:tr>
				<table:td>
					<tags:activityStream activities="${activityStreamModel.activities}" showUserActivityLink="true"/>
				</table:td>
			</table:tr>
		</table:table>

		<tags:paging showSummary="true"/>

	</div>

	<div class="footerseparator"></div>

</tags:page>