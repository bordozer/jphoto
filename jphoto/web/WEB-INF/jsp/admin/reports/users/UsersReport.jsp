<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userReportModel" type="admin.controllers.reports.users.UserReportModel" scope="request"/>

<tags:page pageModel="${userReportModel.pageModel}">

	<script type="text/javascript" src="<c:url value="/common/js/lib/jQuery-Visualize-master/js/visualize.jQuery.js" /> "></script>
	<link rel="stylesheet" href="<c:url value="/common/js/lib/jQuery-Visualize-master/css/basic.css" /> " type="text/css"/>
	<link rel="stylesheet" href="<c:url value="/common/js/lib/jQuery-Visualize-master/css/visualize.css" /> " type="text/css"/>
	<%--<link rel="stylesheet" href="<c:url value="/common/js/lib/jQuery-Visualize-master/css/visualize-dark.css" /> " type="text/css"/>--%>
	<%--<link rel="stylesheet" href="<c:url value="/common/js/lib/jQuery-Visualize-master/css/visualize-light.css" /> " type="text/css"/>--%>

	<c:set var="registrationsMap" value="${userReportModel.registrationsMap}"/>

	<div class="floatleft" style="padding: 50px;">
		<table id="userRegistrationGraph">
			<caption>${eco:translate('User registrations')}</caption>
			<thead>
			<tr>
				<c:set var="counter" value="0"/>
				<c:forEach var="entry" items="${registrationsMap}" varStatus="status">
					<c:set var="date" value="${entry.key}"/>

					<c:set var="showTrace" value="${counter == 5}"/>
					<c:if test="${showTrace}">
						<th scope="col">${eco:formatDate(date)}</th>
						<c:set var="counter" value="0"/>
					</c:if>

					<c:if test="${not showTrace}">
						<th scope="col"></th>
					</c:if>

					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</tr>
			</thead>
			<tbody>
			<tr>
				<c:forEach var="entry" items="${registrationsMap}">
					<c:set var="date" value="${entry.key}"/>
					<c:set var="registrationData" value="${entry.value}"/>

					<td>${registrationData.users}</td>
				</c:forEach>
			</tr>
			</tbody>
		</table>
	</div>

	<script type="text/javascript">

		jQuery().ready( function () {
			$( "#userRegistrationGraph" ).hide();
			var chartOptions = { type:'line', width:1300, height:300, lineWeight:2 };
			$( '#userRegistrationGraph' ).visualize( chartOptions );
		} );


	</script>

	<%--<c:forEach var="entry" items="${registrationsMap}">
		<c:set var="date" value="${entry.key}"/>
		<c:set var="registrationData" value="${entry.value}"/>
	</c:forEach>--%>


</tags:page>