<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="activityStreamModel" type="controllers.activity.list.ActivityStreamModel" scope="request"/>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<tags:page pageModel="${activityStreamModel.pageModel}">

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