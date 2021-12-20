<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="startTaskDate" type="java.lang.String" required="true" %>
<%@ attribute name="startTaskTime" type="java.lang.String" required="true" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="tasks" tagdir="/WEB-INF/tags/executionTask" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<table:table border="0" width="100%">

    <tasks:common startTaskDate="${startTaskDate}"
                  startTaskTime="${startTaskTime}"
                  hideEndTaskDate="true"
                  hideEndTaskTime="true"
    />

    <table:tr>
        <table:td colspan="2">
            ${eco:translate('ExecutionTask: There are no additional parameters for this kind of task')}
        </table:td>
    </table:tr>

</table:table>
