<%@ page import="admin.controllers.jobs.edit.users.UserGenerationModel" %>
<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="jobModelUsers" type="admin.controllers.jobs.edit.users.UserGenerationModel" scope="request"/>

<c:set var="userQtyLimitControl" value="<%=UserGenerationModel.USER_QTY_FORM_CONTROL%>" />
<c:set var="avatarDirControl" value="<%=UserGenerationModel.AVATAR_DIR_FORM_CONTROL%>" />

<tags:page pageModel="${jobModelUsers.pageModel}">

	<admin:jobStart jobModel="${jobModelUsers}" />

	<eco:form action="${eco:baseAdminUrlWithPrefix()}/jobs/${jobModelUsers.job.jobType.prefix}/">

		<table:table width="500">

			<table:tr>
				<table:td colspan="2">
					<admin:saveJobButton jobModel="${jobModelUsers}" />
				</table:td>
			</table:tr>

			<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}" />

			<table:tr>
				<table:tdtext text_t="Total actions" isMandatory="true" />
				<table:tddata>
					<form:input path="jobModelUsers.${userQtyLimitControl}" size="4"/>
				</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="Avatar dir" />
				<table:tddata>
					<form:input path="jobModelUsers.${avatarDirControl}" size="40"/>
					<br />
					${eco:translate('The folder must contain <b>male</b> and <b>female</b> folders with avatars (case does matter)')}
				</table:tddata>
			</table:tr>

			<table:separator colspan="2" />

		</table:table>

		<admin:jobFinish jobModel="${jobModelUsers}" />

	</eco:form>

	<js:confirmAction />

	<tags:springErrorHighliting bindingResult="${jobModelUsers.bindingResult}"/>

</tags:page>