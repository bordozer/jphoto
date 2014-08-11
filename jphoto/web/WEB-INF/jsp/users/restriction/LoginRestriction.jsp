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

	<div style="width: 700px;">
		${eco:translate("LoginRestriction: You are logged out because you are restricted in this right.")}
		<p>
		${eco:translate("LoginRestriction: Login restriction creator")}
		<user:userCard user="${restriction.creator}" />
		<p>
		${eco:translate("LoginRestriction: The restriction period is")}
		${eco:translate("LoginRestriction: Login restriction from")} ${eco:formatDateTimeShort(restriction.restrictionTimeFrom)}
		${eco:translate("LoginRestriction: Login restriction to")} ${eco:formatDateTimeShort(restriction.restrictionTimeTo)}
	</div>

</tags:page>