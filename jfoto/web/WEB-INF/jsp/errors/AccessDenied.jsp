<%@ page import="elements.PageTitleData" %>
<%@ page import="utils.TranslatorUtils" %>
<%@ page import="elements.PageModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true"%>

<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="exceptionModel" type="core.exceptions.ExceptionModel" scope="request"/>

<%
	final String title = TranslatorUtils.translate( "Access denied" );
	final PageModel pageModel = new PageModel();
	pageModel.setPageTitleData( new PageTitleData( title, title, title ) );
%>

<c:set var="title" value="<%=title%>" />
<c:set var="pageModel" value="<%=pageModel%>" />
<c:set var="exceptionMessage" value="${exceptionModel.exceptionMessage}" />

<c:set var="messageTranslated" value="${eco:translate( title ) }" />

<tags:page pageModel="${pageModel}">

	<div class="errorHandlingMessage">
		<h1>${eco:translate( 'Access denied' ) }</h1>
		${eco:translate( 'Sorry, but you do not have permission to perform this operation' ) }
		<h3>${exceptionMessage}</h3>

		<br />
		<br />

		<html:img id="accessdenied" src="icons128/accessdenied.png" width="128" height="128" alt="${messageTranslated}" />
	</div>

</tags:page>
