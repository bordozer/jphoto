<%@ tag import="com.bordozer.jphoto.admin.controllers.scheduler.tasks.edit.SchedulerTaskEditModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tasks" tagdir="/WEB-INF/tags/executionTask" %>

<%@ attribute name="hideStartTaskTime" type="java.lang.Boolean" required="false" %>
<%@ attribute name="hideEndTaskTime" type="java.lang.Boolean" required="false" %>
<%@ attribute name="hideEndTaskDate" type="java.lang.Boolean" required="false" %>

<%@ attribute name="startTaskDate" type="java.lang.String" required="true" %>
<%@ attribute name="startTaskTime" type="java.lang.String" required="false" %>
<%@ attribute name="endTaskDate" type="java.lang.String" required="false" %>
<%@ attribute name="endTaskTime" type="java.lang.String" required="false" %>

<c:set var="schedulerTaskTime" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_TIME_CONTROL%>"/>
<c:set var="skipMissedExecutions" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_SKIP_MISSED_EXECUTIONS_CONTROL%>"/>

<c:set var="startTaskDateControl" value="<%=SchedulerTaskEditModel.START_TASK_DATE_CONTROL%>"/>
<c:set var="endTaskTimeControl" value="<%=SchedulerTaskEditModel.END_TASK_TIME_CONTROL%>"/>
<c:set var="endTaskDateControl" value="<%=SchedulerTaskEditModel.END_TASK_DATE_CONTROL%>"/>
<c:set var="isTaskActiveControl" value="<%=SchedulerTaskEditModel.SCHEDULER_TASK_IS_ACTIVE_CONTROL%>"/>

<table:separator colspan="2"/>

<table:tr>
    <table:tdtext text_t="ExecutionTask: Start date" isMandatory="true"/>

    <table:tddata>
        <tags:datePicker fieldName="${startTaskDateControl}" fieldValue="${startTaskDate}"/>
    </table:tddata>
</table:tr>

<c:if test="${not hideStartTaskTime}">
    <table:tr>
        <table:tdtext text_t="ExecutionTask: Start time" isMandatory="true"/>

        <table:tddata>
            <html:input fieldId="${schedulerTaskTime}" size="6" fieldValue="${startTaskTime}"/> <tasks:executionTaskTime/>
        </table:tddata>
    </table:tr>
</c:if>

<c:if test="${not hideEndTaskDate}">
    <table:tr>
        <table:tdtext text_t="ExecutionTask: End date"/>

        <table:tddata>
            <tags:datePicker fieldName="${endTaskDateControl}" fieldValue="${endTaskDate}"/>
        </table:tddata>
    </table:tr>
</c:if>

<c:if test="${not hideEndTaskTime}">

    <%--<table:separator colspan="2" />--%>

    <table:tr>
        <table:tdtext text_t="ExecutionTask: End time"/>

        <table:tddata>
            <html:input fieldId="${endTaskTimeControl}" size="6" fieldValue="${endTaskTime}"/>
        </table:tddata>
    </table:tr>
</c:if>

<table:separator colspan="2"/>

<%--<table:tr>
	<table:tdtext text_t="Skip missed executions" />

	<table:tddata>
		<form:checkbox path="${skipMissedExecutions}"/>
	</table:tddata>
</table:tr>--%>

<table:tr>
    <table:tdtext text_t="ExecutionTask: Task is active" labelFor="${isTaskActiveControl}"/>

    <table:tddata>
        <form:checkbox path="${isTaskActiveControl}"/>
    </table:tddata>
</table:tr>

<table:separatorInfo colspan="2" title="${eco:translate('ExecutionTask: Custom job parameters')}"/>
