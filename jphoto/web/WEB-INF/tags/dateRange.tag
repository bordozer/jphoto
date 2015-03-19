<%@ tag import="admin.jobs.enums.DateRangeType" %>
<%@ tag import="core.services.utils.SystemVarsServiceImpl" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="admin.controllers.jobs.edit.DateRangableModel" %>
<%@ tag import="ui.translatable.GenericTranslatableList" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="dateRangeTypeId" required="true" type="java.lang.Integer" %>
<%@ attribute name="dateFrom" required="true" type="java.lang.String" %>
<%@ attribute name="dateTo" required="true" type="java.lang.String" %>
<%@ attribute name="timePeriod" required="true" type="java.lang.Integer" %>

<c:set var="dateRangeTypeIdControl" value="<%=DateRangableModel.DATE_RANGE_TYPE_ID_FORM_CONTROL%>" />
<c:set var="dateFromControl" value="<%=DateRangableModel.DATE_FROM_FORM_CONTROL%>" />
<c:set var="dateToControl" value="<%=DateRangableModel.DATE_TO_FORM_CONTROL%>" />
<c:set var="timePeriodControl" value="<%=DateRangableModel.TIME_PERIOD_FORM_CONTROL%>" />

<c:set var="dateRangeTypes" value="<%=GenericTranslatableList.dateRangeTypeTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() )%>" />
<c:set var="dateRangeId" value="<%=DateRangeType.DATE_RANGE.getId()%>" />
<c:set var="timePeriodId" value="<%=DateRangeType.TIME_PERIOD.getId()%>" />
<c:set var="currentTimeId" value="<%=DateRangeType.CURRENT_TIME.getId()%>" />

<c:set var="timePeriodDateRangeDiv" value="timePeriodDateRangeDiv" />
<c:set var="dateRangeDiffDiv" value="dateRangeDiffDiv" />

<div class="panel panel-default">

	<div class="panel panel-heading">
		<h3 class='panel-title text-center'>
			${eco:translate('Job JSP: Date range component title')}
		</h3>
	</div>

	<div class="panel-body">

		<div class="row">
			<div class="col-lg-12 text-center">
				<form:radiobuttons path="${dateRangeTypeIdControl}" items="${dateRangeTypes.entries}" itemValue="id" itemLabel="name" onchange="setVisibility();" delimiter="&nbsp;&nbsp;&nbsp;" htmlEscape="false"/>
			</div>
		</div>

		<hr />

		<div id='dateRangeDiv' class="row" <c:if test="${dateRangeTypeId != dateRangeId}">style="display: none;" </c:if> >
			<div class="col-lg-12">

				<div class="row">
					<div class="col-lg-5 text-right">
						${eco:translate('Date range: Date from')}
					</div>
					<div class="col-lg-7">
						<tags:datePicker fieldName="${dateFromControl}" fieldValue="${dateFrom}" onchange="processDateRangeChange();" />
					</div>
				</div>

				<div class="row">
					<div class="col-lg-5 text-right">
						${eco:translate('Date range: Date to')}
					</div>
					<div class="col-lg-7">
						<tags:datePicker fieldName="${dateToControl}" fieldValue="${dateTo}" onchange="processDateRangeChange();"/>
					</div>
				</div>

				<div class="row">
					<div class="col-lg-5 text-right">
						${eco:translate('Date range: Time period')}
					</div>
					<div class="col-lg-7">
						<div id="${dateRangeDiffDiv}">${timePeriod} ${eco:translate('Date range: date range tag => parameters => days')}</div>
					</div>
				</div>

			</div>
		</div>



		<div id='timePeriodDiv' class="row" <c:if test="${dateRangeTypeId != timePeriodId}">style="display: none;" </c:if> >

			<input type="hidden" id="${dateRangeTypeIdControl}" value="0">

			<div class="col-lg-12">

				<div class="row">
					<div class="col-lg-5 text-right">
						${eco:translate('Date range: Time period')}
					</div>
					<div class="col-lg-7">
						<html:input fieldId="${timePeriodControl}" fieldValue="${timePeriod}" size="3" onchange="processTimePeriodChange();"/> ${eco:translate('Date range: date range tag => parameters => days')}
					</div>
				</div>

				<div class="row">
					<div class="col-lg-5 text-right">
						${eco:translate('Date range: Date range')}
					</div>
					<div class="col-lg-7">
						<div id="${timePeriodDateRangeDiv}">${dateFrom} - ${dateTo}</div>
					</div>
				</div>

			</div>
		</div>



		<div id='currentTimeDiv' class="row" <c:if test="${dateRangeTypeId != currentTimeId}">style="display: none;" </c:if> >

			<div class="col-lg-12">

				<div class="row">
					<div class="col-lg-12 text-center">
						${eco:translate('Date range: Actual time will be used')}
					</div>
				</div>

			</div>
		</div>

	</div>

	<div class="panel-footer">

	</div>

</div>

<c:set var="jsDateFormat" value="<%=ApplicationContextHelper.getSystemVarsService().getJavaScriptDateFormat()%>"/>

<script type="text/javascript">
	function setVisibility() {
		var type = $('input[name=' + '${dateRangeTypeIdControl}' + ']:checked' ).val();

		if ( type == ${dateRangeId} ) {
			$( '#currentTimeDiv' ).hide();
			$( '#timePeriodDiv' ).hide();
			$( '#dateRangeDiv' ).show();
			processDateRangeChange();
		}

		if ( type == ${timePeriodId} ) {
			$( '#currentTimeDiv' ).hide();
			$( '#dateRangeDiv' ).hide();
			$( '#timePeriodDiv' ).show();
			processTimePeriodChange();
		}

		if ( type == ${currentTimeId} ) {
			$( '#dateRangeDiv' ).hide();
			$( '#timePeriodDiv' ).hide();
			$( '#currentTimeDiv' ).show();
		}
	}

	function processDateRangeChange() {
		var dateFromVal = $( "#${dateFromControl}" ).val();
		var dateToVal = $( "#${dateToControl}" ).val();

		var dateFrom = new Date( dateFromVal );
		var dateTo = new Date( dateToVal );

		var millisecondsPerDay = 1000 * 60 * 60 * 24;
		var dateDiffInDays = Math.round( ( dateTo.getTime() - dateFrom.getTime() ) / millisecondsPerDay );

		$( "#${timePeriodControl}" ).val( dateDiffInDays );
		$( "#${dateRangeDiffDiv}" ).text( dateDiffInDays );
	}

	function processTimePeriodChange() {
		var timePeriodInDays = $( "#${timePeriodControl}" ).val();

		var now = new Date();
		var dateFrom = new Date( now - ( timePeriodInDays ) * 1000 * 60 * 60 * 24 );
		var dateTo = new Date( now );

		var dateFromFormatted = $.datepicker.formatDate( '${jsDateFormat}', dateFrom );
		var dateToFormatted = $.datepicker.formatDate( '${jsDateFormat}', dateTo );

		$( "#${dateFromControl}" ).val( dateFromFormatted );
		$( "#${dateToControl}" ).val( dateToFormatted );
		$( "#${timePeriodDateRangeDiv}" ).text( dateFromFormatted + ' - ' + dateToFormatted );
	}

</script>