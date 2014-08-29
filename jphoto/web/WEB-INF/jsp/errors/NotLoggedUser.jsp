<%@ page import="ui.elements.PageTitleData" %>
<%@ page import="ui.elements.PageModel" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true"%>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="exceptionModel" type="core.exceptions.ExceptionModel" scope="request"/>

<%
	final String title = exceptionModel.getTranslatorService().translate( "Logged user only area", EnvironmentContext.getLanguage() );
	final PageModel pageModel = new PageModel();
	pageModel.setPageTitleData( new PageTitleData( title, title, title ) );
%>

<c:set var="title" value="<%=title%>" />
<c:set var="pageModel" value="<%=pageModel%>" />
<c:set var="exceptionMessage" value="${exceptionModel.exceptionMessage}" />

<tags:page pageModel="${pageModel}">

	<div class="errorHandlingMessage">
		<h1>${eco:translate( 'Logged user only area' ) }</h1>
		${eco:translate( 'Sorry, this operation can perform only logged users' ) }
		<h3>${exceptionMessage}</h3>

		<br />
		<br />

		<html:img id="accessdenied" src="icons128/accessdenied.png" width="128" height="128" alt="${title}" />
	</div>

</tags:page>
