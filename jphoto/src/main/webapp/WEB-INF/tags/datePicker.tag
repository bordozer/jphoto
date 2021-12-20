<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="fieldName" required="true" type="java.lang.String" %>
<%@ attribute name="fieldValue" required="true" type="java.lang.String" %>
<%@ attribute name="onchange" required="false" type="java.lang.String" %>

<c:set var="triggerName" value="${fieldName}-trigger"/>

<html:input fieldId="${fieldName}" fieldValue="${fieldValue}" onchange="${onchange}"/>
<button id="${triggerName}" name="${triggerName}" type="button">...</button>

<script type="text/javascript">

    require(['jquery', 'jquery_ui', 'jscal2', 'jscal2_lang'], function ($) {

        new Calendar({
            inputField: "${fieldName}",
            dateFormat: "%Y-%m-%d",
            trigger: "${triggerName}",
            bottomBar: true,
            selection: Calendar.dateToInt(new Date()),
            weekNumbers: true,
            onSelect: function () {
                var date = Calendar.intToDate(this.selection.get());
                this.hide();
                <c:if test="${not empty onchange}">
                ${onchange}
                </c:if>
            }
        });
    });
</script>

<%--
http://www.dynarch.com/projects/calendar/doc/
%a ? abbreviated weekday name
%A ? full weekday name
%b ? abbreviated month name
%B ? full month name
%C ? the century number
%d ? the day of the month (range 01 to 31)
%e ? the day of the month (range 1 to 31)
%H ? hour, range 00 to 23 (24h format)
%I ? hour, range 01 to 12 (12h format)
%j ? day of the year (range 001 to 366)
%k ? hour, range 0 to 23 (24h format)
%l ? hour, range 1 to 12 (12h format)
%m ? month, range 01 to 12
%o ? month, range 1 to 12
%M ? minute, range 00 to 59
%n ? a newline character
%p ? PM or AM
%P ? pm or am
%s ? UNIX time (number of seconds since 1970-01-01)
%S ? seconds, range 00 to 59
%t ? a tab character
%W ? week number
%u ? the day of the week (range 1 to 7, 1 = MON)
%w ? the day of the week (range 0 to 6, 0 = SUN)
%y ? year without the century (range 00 to 99)
%Y ? year with the century
%% ? a literal '%' character
--%>

