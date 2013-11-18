<%@ page import="admin.controllers.jobs.edit.action.PhotoActionGenerationModel" %>
<%@ page import="admin.controllers.jobs.edit.AbstractAdminJobModel" %>
<%@ page import="admin.controllers.jobs.edit.rankVoting.RankVotingJobModel" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="rankVotingJobModel" type="admin.controllers.jobs.edit.rankVoting.RankVotingJobModel" scope="request"/>

<c:set var="actionsQtyControl" value="<%=RankVotingJobModel.ACTIONS_QTY_FORM_CONTROL%>" />

<tags:page pageModel="${rankVotingJobModel.pageModel}">

	<admin:jobStart jobModel="${rankVotingJobModel}" />

	<form:form action="${eco:baseAdminUrlWithPrefix()}/jobs/${rankVotingJobModel.job.jobType.prefix}/" id="FormName" name="FormName">

		<table:table width="500" border="0">

			<table:tr>
				<table:td colspan="2">
					<admin:saveJobButton jobModel="${rankVotingJobModel}" />
				</table:td>
			</table:tr>

			<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}" />

			<table:tr>
				<table:tdtext text_t="Total actions" isMandatory="true" />
				<table:tddata>
					<form:input path="${actionsQtyControl}" size="4"/>
				</table:tddata>
			</table:tr>

			<table:separator colspan="2" />

		</table:table>

		<admin:jobFinish jobModel="${rankVotingJobModel}" />

	</form:form>

	<js:confirmAction />

	<tags:springErrorHighliting bindingResult="${rankVotingJobModel.bindingResult}"/>

</tags:page>