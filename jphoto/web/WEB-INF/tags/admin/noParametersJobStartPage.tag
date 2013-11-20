<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<%@ attribute name="jobModel" type="admin.controllers.jobs.edit.AbstractAdminJobModel" required="true" %>

<admin:jobStart jobModel="${jobModel}"/>

<eco:form action="${eco:baseAdminUrlWithPrefix()}/jobs/${jobModel.job.jobType.prefix}/">

	<table:table width="500" border="0">

		<table:tr>
			<table:td colspan="2">
				<admin:saveJobButton jobModel="${jobModel}"/>
			</table:td>
		</table:tr>

		<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}"/>

		<table:tr>
			<table:td colspan="2">${eco:translate('The job has no parameters')}</table:td>
		</table:tr>

		<table:separator colspan="2"/>

	</table:table>

	<admin:jobFinish jobModel="${jobModel}"/>

</eco:form>

<js:confirmAction/>

<tags:springErrorHighliting bindingResult="${jobModel.bindingResult}"/>