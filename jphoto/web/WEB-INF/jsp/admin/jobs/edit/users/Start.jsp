<%@ page import="admin.controllers.jobs.edit.users.UserGenerationModel" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="userGenerationModel" type="admin.controllers.jobs.edit.users.UserGenerationModel" scope="request"/>

<c:set var="userQtyLimitControl" value="<%=UserGenerationModel.USER_QTY_FORM_CONTROL%>"/>
<c:set var="avatarDirControl" value="<%=UserGenerationModel.AVATAR_DIR_FORM_CONTROL%>"/>

<tags:page pageModel="${userGenerationModel.pageModel}">

	<admin:jobEditData jobModel="${userGenerationModel}">

		<jsp:attribute name="jobForm">

			<table:table width="700">

				<table:tr>
					<table:td colspan="2">
						<admin:saveJobButton jobModel="${userGenerationModel}"/>
					</table:td>
				</table:tr>

				<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}"/>

				<table:tr>
					<table:tdtext text_t="Total job steps" isMandatory="true"/>
					<table:tddata>
						<form:input path="${userQtyLimitControl}" size="4"/>
					</table:tddata>
				</table:tr>

				<table:tr>
					<table:tdtext text_t="Avatar dir"/>
					<table:tddata>
						<form:input path="${avatarDirControl}" size="40"/>
						<br/>
						${eco:translate('The folder must contain <b>male</b> and <b>female</b> folders with avatars (case does matter)')}
					</table:tddata>
				</table:tr>

				<table:separator colspan="2"/>

			</table:table>

		</jsp:attribute>

	</admin:jobEditData>

</tags:page>