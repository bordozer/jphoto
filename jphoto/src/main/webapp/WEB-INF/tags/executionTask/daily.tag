<%@ tag import="com.bordozer.jphoto.admin.controllers.scheduler.tasks.edit.SchedulerTaskEditModel" %>
<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ tag import="com.bordozer.jphoto.ui.translatable.GenericTranslatableList" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="startTaskDate" type="java.lang.String" required="false" %>
<%@ attribute name="startTaskTime" type="java.lang.String" required="true" %>

<%@ attribute name="endTaskDate" type="java.lang.String" required="true" %>
<%@ attribute name="endTaskTime" type="java.lang.String" required="true" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="tasks" tagdir="/WEB-INF/tags/executionTask" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="weekdays"
       value="<%=GenericTranslatableList.schedulerWeekdayTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="dailyTaskWeekdayIds" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_DAILY_TASK_WEEKDAY_IDS_CONTROL%>"/>
<c:set var="schedulerTaskTime" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_TIME_CONTROL%>"/>

<table:table border="0" width="100%">

    <tasks:common startTaskDate="${startTaskDate}"
                  endTaskDate="${endTaskDate}"
                  hideStartTaskTime="true"
                  hideEndTaskTime="true"
    />

    <table:tr>
        <table:tdtext text_t="ExecutionTask: Execution time" isMandatory="true"/>

        <table:tddata>
            <html:input fieldId="${schedulerTaskTime}" size="6" fieldValue="${startTaskTime}"/> <tasks:executionTaskTime/>
        </table:tddata>
    </table:tr>

    <table:tr>
        <table:tdtext text_t="ExecutionTask: Days of week" isMandatory="true"/>

        <table:tddata>
            <js:checkboxMassChecker checkboxClass="dailyTaskWeekdayIds"/>
            <br/>
            <form:checkboxes path="${dailyTaskWeekdayIds}" items="${weekdays}" itemValue="id" itemLabel="name" delimiter="<br />"
                             cssClass="dailyTaskWeekdayIds"/>
        </table:tddata>
    </table:tr>

</table:table>
