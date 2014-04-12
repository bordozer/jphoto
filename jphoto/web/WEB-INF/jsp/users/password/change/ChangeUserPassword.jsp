<%@ page import="ui.controllers.users.password.change.ChangeUserPasswordModel" %>
<%@ page import="ui.services.validation.UserRequirement" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="changeUserPasswordModel" type="ui.controllers.users.password.change.ChangeUserPasswordModel" scope="request" />

<c:set var="user" value="${changeUserPasswordModel.user}"/>

<c:set var="oldPasswordControl" value="<%=ChangeUserPasswordModel.FORM_CONTROL_OLD_PASSWORD%>"/>
<c:set var="newPasswordControl" value="<%=ChangeUserPasswordModel.FORM_CONTROL_NEW_PASSWORD%>"/>
<c:set var="newPasswordConfirmationControl" value="<%=ChangeUserPasswordModel.FORM_CONTROL_NEW_PASSWORD_CONFIRMATION%>"/>

<c:set var="passwordRequirement" value="<%=changeUserPasswordModel.getDataRequirementService().getUserRequirement().getPasswordRequirement( false )%>"/> <%-- TODO: set to true--%>

<tags:page pageModel="${changeUserPasswordModel.pageModel}">

	<form:form modelAttribute="changeUserPasswordModel" method="POST" action="${eco:baseUrl()}/members/${user.id}/password/">

		<table:table width="800">

			<table:separatorInfo colspan="2" title="${eco:translate('Old password')}"/>

			<table:tr>
				<table:tdtext text_t="Old password" isMandatory="true" />
				<table:tddata>
					<tags:inputHint inputId="${oldPasswordControl}" hintTitle_t="Old password">
						<jsp:attribute name="inputField">
							<html:password fieldId="${oldPasswordControl}" fieldValue="" />
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tr>

			<table:separatorInfo colspan="2" title="${eco:translate('New password')}"/>

			<table:tr>
				<table:tdtext text_t="New password" isMandatory="true" />
				<table:tddata>
					<tags:inputHint inputId="${newPasswordControl}" hintTitle_t="Password" hint="${passwordRequirement}">
						<jsp:attribute name="inputField">
							<html:password fieldId="${newPasswordControl}" fieldValue="" />
							<br />
							${passwordRequirement}
						</jsp:attribute>
					</tags:inputHint>
				</table:tddata>
			</table:tr>

			<table:tr>
				<table:tdtext text_t="New password confirmation" isMandatory="true" />
				<table:tddata>
					<html:password fieldId="${newPasswordConfirmationControl}" fieldValue="" />
				</table:tddata>
			</table:tr>

			<table:trok text_t="Change password" />

		</table:table>

	</form:form>

	<tags:springErrorHighliting bindingResult="${changeUserPasswordModel.bindingResult}" />

</tags:page>