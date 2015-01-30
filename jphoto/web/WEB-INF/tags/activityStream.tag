<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="activities" required="true" type="java.util.List" %>
<%@ attribute name="hideUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showUserActivityLink" required="false" type="java.lang.Boolean" %>
<%@ attribute name="hideTextForAdmin" required="false" type="java.lang.Boolean" %>

<table:table width="70%;">

	<c:forEach var="activity" items="${activities}">

		<c:if test="${not empty activity}">

			<table:tr>

				<table:td width="100" cssClass="small-text text-centered">
					${eco:formatDate(activity.activityTime)}
					<br/>
					${eco:formatTime(activity.activityTime)}
				</table:td>

				<table:td cssClass="text-centered" width="30">${activity.displayActivityIcon}</table:td>

				<table:tdicon><html:img16 src="jobtype/${activity.activityType.icon}" alt="${activity.activityType.name}"/></table:tdicon>

				<table:td>

					<c:if test="${not hideUser}">
						${eco:translatableMessage(activity.displayActivityUserLink)}
					</c:if>

					${eco:translateActivityStreamEntry(activity)}

					<c:if test="${not hideTextForAdmin}">
						<div style="float: left; width: 95%;">
							${eco:translateActivityStreamEntryForAdmin(activity)}
						</div>
					</c:if>

				</table:td>

				<c:if test="${showUserActivityLink and activity.displayActivityUserId > 0}">
					<table:td cssClass="text-centered" width="20">
						<a href="${eco:baseUrl()}/members/${activity.displayActivityUserId}/card/activity/"
						   title="${eco:translate('Show full member activity stream')}">${eco:translate('Show full member activity stream (short)')}</a>
					</table:td>
				</c:if>

			</table:tr>

		</c:if>

	</c:forEach>

</table:table>