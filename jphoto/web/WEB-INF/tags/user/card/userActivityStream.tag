<%@ tag import="ui.activity.ActivityType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="activities" required="true" type="java.util.List" %>
<%@ attribute name="filterActivityTypeId" required="true" type="java.lang.Integer" %>

<tags:activityStreamFilter activityTypeValues="<%=ActivityType.USER_ACTIVITIES%>" filterActivityTypeId="${filterActivityTypeId}"
						   url="${eco:baseUrl()}/members/${user.id}/card/activity/"/>

<div class="floatleft">

	<c:if test="${not empty activities}">

		<tags:paging showSummary="false"/>

		<table:table width="1000">
			<table:tr>
				<table:td>
					<tags:activityStream activities="${activities}" hideUser="true"/>
				</table:td>
			</table:tr>
		</table:table>

		<tags:paging showSummary="true"/>

	</c:if>

	<c:if test="${empty activities}">
		<div style="float: left; text-align: center; width: 100%;">
			<h3>${eco:translate('There is no any activity yet')}</h3>
		</div>
	</c:if>

</div>