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

<div class="centerAlign">
	<form:radiobuttons path="${dateRangeTypeIdControl}" items="${dateRangeTypes.entries}" itemValue="id" itemLabel="name" onchange="setVisibility();" delimiter="&nbsp;&nbsp;&nbsp;" htmlEscape="false"/>
</div>

<c:set var="tblWidth" value="500"/>

<div id="dateRangeDiv" <c:if test="${dateRangeTypeId != dateRangeId}">style="display: none;" </c:if> >
	<table:table border="0" width="${tblWidth}">

		<%--TODO: translate--%>
		<table:tr>
			<table:tdtext text_t="Date range: Date from" isMandatory="true"/>
			<table:tddata>
				<tags:datePicker fieldName="${dateFromControl}" fieldValue="${dateFrom}" onchange="processDateRangeChange();" />
			</table:tddata>
		</table:tr>

		<table:tr>
			<table:tdtext text_t="Date range: Date to" isMandatory="true"/>
			<table:tddata>
				<tags:datePicker fieldName="${dateToControl}" fieldValue="${dateTo}" onchange="processDateRangeChange();"/>
			</table:tddata>
		</table:tr>
	</table:table>

	<table:table border="0" width="${tblWidth}">
		<table:tr>
			<table:tdtext text_t="Date range: Time period"/>
			<table:tddata>
				<div id="${dateRangeDiffDiv}">${timePeriod} ${eco:translate('Date range: date range tag => parameters => days')}</div>
			</table:tddata>
		</table:tr>
	</table:table>
</div>

<div id="timePeriodDiv" <c:if test="${dateRangeTypeId != timePeriodId}">style="display: none;" </c:if> >

	<input type="hidden" id="${dateRangeTypeIdControl}" value="0">

	<table:table border="0" width="${tblWidth}">
		<table:tr>
			<table:tdtext text_t="Date range: Time period"/>
			<table:tddata>
				<html:input fieldId="${timePeriodControl}" fieldValue="${timePeriod}" size="3" onchange="processTimePeriodChange();"/> ${eco:translate('Date range: date range tag => parameters => days')}
			</table:tddata>
		</table:tr>
	</table:table>

	<table:table border="0" width="${tblWidth}">
		<table:tr>
			<table:tdtext text_t="Date range: Date range"/>
			<table:tddata>
				<div id="${timePeriodDateRangeDiv}">${dateFrom} - ${dateTo}</div>
			</table:tddata>
		</table:tr>
	</table:table>
</div>

<div id="currentTimeDiv" <c:if test="${dateRangeTypeId != currentTimeId}">style="display: none;" </c:if> >
	<table:table border="0" width="${tblWidth}">
		<table:tr>
			<table:td cssClass="text-centered">
				${eco:translate('Date range: Actual time will be used')}
			</table:td>
		</table:tr>
		<table:tr>
			<table:td>
				&nbsp;
			</table:td>
		</table:tr>
	</table:table>
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