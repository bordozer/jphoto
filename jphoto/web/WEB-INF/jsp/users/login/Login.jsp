<%@ page import="core.services.utils.UrlUtilsServiceImpl" %>
<%@ page import="controllers.users.login.UserLoginModel" %>
<%@ page import="core.context.ApplicationContextHelper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<jsp:useBean id="userLoginModel" type="controllers.users.login.UserLoginModel" scope="request"/>

<c:set var="loginFormAction" value="<%=ApplicationContextHelper.getUrlUtilsService().getUserLoginLink()%>"/>
<c:set var="loginControl" value="<%=UserLoginModel.LOGIN_FORM_LOGIN_CONTROL%>"/>
<c:set var="passwordControl" value="<%=UserLoginModel.LOGIN_FORM_PASSWORD_CONTROL%>"/>

<tags:page pageModel="${userLoginModel.pageModel}">

	<eco:form action="${loginFormAction}">

		<table:table width="400">

			<table:tredit>
				<table:tdtext labelFor="${loginControl}" text_t="Login" />
				<table:tddata>
					<input class="ui-state-default ui-corner-all" type="text" id="${loginControl}" name="${loginControl}" value="${userLoginModel.userlogin}" size="14"/>
				</table:tddata>
			</table:tredit>

			<table:tredit>
				<table:tdtext labelFor="${passwordControl}" text_t="Password" />
				<table:tddata>
					<input class="ui-state-default ui-corner-all" type="password" id="${passwordControl}" name="${passwordControl}" value="" size="14"/>
				</table:tddata>
			</table:tredit>

			<table:trok text_t="Login" />

		</table:table>

	</eco:form>

	<tags:springErrorHighliting bindingResult="${userLoginModel.bindingResult}"/>

</tags:page>
