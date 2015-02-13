<%@ tag import="core.services.entry.AnonymousDaysService" %>
<%@ tag import="core.services.utils.SystemVarsServiceImpl" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="core.services.utils.SystemVarsService" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="anonymousDays" required="true" type="java.util.List" %>
<%@ attribute name="anonymousPeriod" required="true" type="java.lang.Integer" %>

<%
	final SystemVarsService systemVarsService = ApplicationContextHelper.getBean( SystemVarsServiceImpl.BEAN_NAME );
%>

<c:set var="systemDateFormat" value="<%=systemVarsService.getSystemDateFormat()%>"/>
<c:set var="jsDateFormat" value="<%=systemVarsService.getJavaScriptDateFormat()%>"/>

<script type="text/javascript" src="${eco:baseUrl()}/js/lib/date.js"></script>

<style type="text/css">
	.ui-datepicker-today {
		background: #aa0000 !important;
	}

	.ui-datepicker.ui-widget-content .ui-state-default {
		background: none;
		font-size: 14px;
		text-align: center;
		/*width: 25px;*/
	}

	.ui-widget-content .anonymous-day .ui-state-default {
		background-color: #2e6e9e;
		color: #CDCDCD;
		font-weight: bold;
	}

	.ui-widget-content .not-anonymous-day .ui-state-default {
		background: silver; <%-- TODO: color! --%>
	}

	.ui-datepicker-week-col {
		text-align: center;
		color: #1d5987;
		font-weight: bold;
		font-size: 10px;
	}

	.ui-datepicker table {
		width: 100%;
		font-size: .7em;
		border-collapse: collapse;
		margin: 0 0 .4em;
	}

</style>

<div style="width: 65%; margin: 0 auto;">
	<div id="date-picker-div"></div>
</div>

<script type="text/javascript">

	var anonymousDays = [ <c:forEach var="anonymousDay" items="${anonymousDays}" varStatus="status">convertDate( '${anonymousDay.month + 1}/${anonymousDay.day}/${anonymousDay.year}' )<c:if test="${not status.last}"> ,</c:if></c:forEach> ];

	function highlightAnonymousDays( pickerDate ) {
		if ( isAnonymousDay( pickerDate ) ) {
			return [ true, 'anonymous-day', convertDateToPrint( pickerDate ) + ' ${eco:translate('is anonymous day')}' ];
		}
		return [ true, 'not-anonymous-day', convertDateToPrint( pickerDate ) + ' ${eco:translate('is not anonymous day')}' ];
	}

	function isAnonymousDay( pickerDate ) {
		return arrayIndex( convertDate( pickerDate ) ) != -1;
	}

	function arrayIndex( value ) {
		return $.inArray( value, anonymousDays );
	}

	function convertDate( date ) {
		return formatDate( new Date( date ), "${systemDateFormat}" );
	}

	function convertDateToPrint( date ) {
		return $.datepicker.formatDate( '${jsDateFormat}', new Date( date ) );
//		return 'TODO: anunymousDays.tag';
	}

</script>

<br />
<br />

<div style="font-size: 12px;">
	${eco:translate("The selected days are days of anonymous photo posting")}
	<br />
	${eco:translate("The posted in selected days photos authors names will be hidden")}
	<br />
	${eco:translate1("The names will be shown after $1 day(s) after photo uploading", anonymousPeriod)}
</div>

<div class="footerseparator"></div>