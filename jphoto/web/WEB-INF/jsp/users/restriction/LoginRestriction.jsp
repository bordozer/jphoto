<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="loginRestrictionModel" type="ui.controllers.restriction.LoginRestrictionModel" scope="request"/>
<c:set var="restriction" value="${loginRestrictionModel.restriction}" />

<tags:page pageModel="${loginRestrictionModel.pageModel}">

	<table:table width="700px;">

		<table:tr>
			<table:td colspan="2">
				${eco:translate("LoginRestriction: You are logged out because you are restricted in this right.")}
			</table:td>
		</table:tr>

		<table:tr>
			<table:td >${eco:translate("LoginRestriction: Login restriction creator")}</table:td>
			<table:td ><user:userCard user="${restriction.creator}" /></table:td>
		</table:tr>

		<table:tr>
			<table:td colspan="2">
				${eco:translate("LoginRestriction: The restriction period is")}
			</table:td>
		</table:tr>

		<table:tr>
			<table:td >${eco:translate("LoginRestriction: Login restriction from")}</table:td>
			<table:td >${eco:formatDateTimeShort(restriction.restrictionTimeFrom)}</table:td>
		</table:tr>

		<table:tr>
			<table:td >${eco:translate("LoginRestriction: Login restriction to")}</table:td>
			<table:td >${eco:formatDateTimeShort(restriction.restrictionTimeTo)}</table:td>
		</table:tr>

	</table:table>

</tags:page>