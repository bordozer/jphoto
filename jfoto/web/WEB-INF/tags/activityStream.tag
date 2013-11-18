<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="activities" required="true" type="java.util.List" %>
<%@ attribute name="hideUser" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showUserActivityLink" required="false" type="java.lang.Boolean" %>

<table:table oddEven="true" width="100%">

	<c:forEach var="activity" items="${activities}">

		<table:tr>
			<table:td width="100" cssClass="small-text textcentered">
				${eco:formatDate(activity.activityTime)}
				<br />
				${eco:formatTime(activity.activityTime)}
			</table:td>

			<table:td cssClass="textcentered" width="30">${activity.displayActivityPicture}</table:td>

			<table:tdicon><html:img16 src="jobtype/${activity.activityType.icon}" alt="${activity.activityType.name}"/></table:tdicon>

			<table:td>

				<c:if test="${not hideUser}">
					${activity.displayActivityUserLink}
				</c:if>

				${activity.activityDescription}
			</table:td>

			<c:if test="${showUserActivityLink and activity.displayActivityUserId > 0}">
				<table:td cssClass="textcentered" width="20">
					<a href="${eco:baseUrlWithPrefix()}/members/${activity.displayActivityUserId}/card/activity/" title="${eco:translate('Show member activity stream')}">${eco:translate('all')}</a>
				</table:td>
			</c:if>

		</table:tr>

	</c:forEach>

</table:table>