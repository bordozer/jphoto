<%@ page import="ui.activity.ActivityType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="photoActivityStreamModel" type="ui.controllers.photos.activity.PhotoActivityStreamModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<tags:page pageModel="${photoActivityStreamModel.pageModel}">

	<div class="floatleft">

		<tags:activityStreamFilter activityTypeValues="<%=ActivityType.PHOTO_ACTIVITIES%>" filterActivityTypeId="${photoActivityStreamModel.filterActivityTypeId}"
								   url="${eco:baseUrl()}/photo/${photoActivityStreamModel.photo.id}/activity/"/>

		<tags:paging showSummary="false"/>

		<table:table width="1000">
			<table:tr>
				<table:td>
					<tags:activityStream activities="${photoActivityStreamModel.activities}" showUserActivityLink="true"/>
				</table:td>
			</table:tr>
		</table:table>

		<tags:paging showSummary="true"/>

	</div>

	<div class="footerseparator"></div>

</tags:page>