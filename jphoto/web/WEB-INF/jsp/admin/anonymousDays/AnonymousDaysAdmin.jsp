<%@ page import="core.services.entry.AnonymousDaysService" %>
<%@ page import="core.context.ApplicationContextHelper" %>
<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ page import="core.services.utils.DateUtilsService" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="anonym" tagdir="/WEB-INF/tags/anonym" %>

<jsp:useBean id="anonymousDaysAdminModel" type="admin.controllers.anonymousDays.AnonymousDaysAdminModel" scope="request"/>

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "anonymousDaysService", ApplicationContextHelper.<AnonymousDaysService>getBean( AnonymousDaysService.BEAN_NAME ) );
	final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();
%>

<c:set var="jsDateFormat" value="<%=ApplicationContextHelper.getSystemVarsService().getJavaScriptDateFormat()%>"/>
<c:set var="currentMonth" value="<%=dateUtilsService.getMonth( dateUtilsService.getCurrentTime() )%>" />

<c:set var="anonymousDaysForYear" value="${anonymousDaysAdminModel.anonymousDaysForYear}" />
<c:set var="currentYear" value="${anonymousDaysAdminModel.currentYear}" />
<c:set var="calendarMinDate" value="${eco:formatDate(anonymousDaysAdminModel.calendarMinDate)}" />
<c:set var="calendarMaxDate" value="${eco:formatDate(anonymousDaysAdminModel.calendarMaxDate)}" />

<c:set var="monthCorrection" value="${(currentYear - anonymousDaysForYear ) * 12 }" />
<c:set var="currentAtPos" value="${currentMonth + monthCorrection}" />

<tags:page pageModel="${anonymousDaysAdminModel.pageModel}">

	<style type="text/css">
		.ui-datepicker .ui-datepicker-prev,
		.ui-datepicker .ui-datepicker-next {
			display: none;
		}

		.currentYearCss {
			border: solid 1px #6F7A9F;
			padding-left: 5px;
			padding-right: 5px;
			border-radius: 5px;
		}

		.customYearCss {
			background-color: #e6e6e6;
		}
	</style>

	<script type="text/javascript">

		$( function () {
			$( "#datepicker" ).datepicker( {
											  inline:true
											, firstDay:1
											, showOtherMonths:true
											, showWeek: true
											, showButtonPanel: true
											<%--, showCurrentAtPos: ${currentAtPos}--%>
											, numberOfMonths: [ 3, 4 ]
											, dayNamesMin:['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
											, dateFormat: "${jsDateFormat}"
											, onSelect: addAnonymousDay
											, beforeShowDay: highlightAnonymousDays
											, stepMonths: 0
											, minDate: new Date( '${calendarMinDate}' )
											, maxDate: new Date( '${calendarMaxDate}' )
										   } );
		} );

		function addAnonymousDay( pickerDate, instance ) {
			var anonymousDay = convertDate( pickerDate );

			var message;
			if ( ! isAnonymousDay( pickerDate ) ) {
				saveAnonymousDayOnServer( anonymousDay );
				anonymousDays.push( anonymousDay );

				message = "${eco:translate('was added to anonymous days')}";
			} else {
				deleteAnonymousDayOnServer( anonymousDay );
				anonymousDays.splice( arrayIndex( anonymousDay ), 1 );

				message = "${eco:translate('was removed from anonymous days')}";
			}

			notifySuccessMessage( pickerDate + ' ' + message );
		}

		function saveAnonymousDayOnServer( anonymousDay ) {
			jsonRPC.anonymousDaysService.saveAnonymousDayAjax( anonymousDay );
		}

		function deleteAnonymousDayOnServer( anonymousDay ) {
			jsonRPC.anonymousDaysService.deleteAnonymousDayAjax( anonymousDay );
		}

	</script>

	<c:set var="yearDelta" value="7" /> <%-- TODO: move to model and make constant--%>
	<c:set var="yearsBegin" value="${anonymousDaysForYear - yearDelta}"/>
	<c:if test="${yearsBegin < 2013}"> <%-- TODO: move to model and make constant--%>
		<c:set var="yearsBegin" value="2013"/>
	</c:if>

	<div style="float: left; width: 100%; text-align: center;">
		<h3>
			<c:forEach var="year" begin="${yearsBegin}" end="${anonymousDaysForYear + yearDelta}">

				<c:set var="yearCss" value="" />
				<c:if test="${year == anonymousDaysForYear}">
					<c:set var="yearCss" value="currentYearCss customYearCss" />
				</c:if>
				<c:if test="${year == currentYear}">
					<c:set var="yearCss" value="currentYearCss" />
				</c:if>

				<span class="${yearCss}"><a href="${eco:baseAdminUrlWithPrefix()}/anonymousDays/${year}/">${year}</a></span>

				&nbsp;
			</c:forEach>
		</h3>
	</div>

	<div style="float: left; width: 100%; margin-left: 10px; margin-top: 10px;">
		<anonym:anunymousDays anonymousDays="${anonymousDaysAdminModel.anonymousDays}" anonymousPeriod="${anonymousDaysAdminModel.anonymousPeriod}" />
	</div>

</tags:page>
