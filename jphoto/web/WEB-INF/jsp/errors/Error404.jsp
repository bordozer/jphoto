<%@ page import="elements.PageTitleData" %>
<%@ page import="utils.TranslatorUtils" %>
<%@ page import="elements.PageModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true"%>

<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="exceptionModel" type="core.exceptions.ExceptionModel" scope="request"/>

<%
	final String title = TranslatorUtils.translate( "Error 404 - Page not found" );
	final PageModel pageModel = new PageModel();
	pageModel.setPageTitleData( new PageTitleData( title, title, title ) );
%>

<c:set var="title" value="<%=title%>" />
<c:set var="pageModel" value="<%=pageModel%>" />

<c:set var="messageTranslated" value="${eco:translate( title ) }" />

<tags:page pageModel="${pageModel}">

	<div style="text-align: center">

		<h3>${eco:translate( 'Sorry, but page not found on the server' ) }</h3>
		<br />
		'<b>${requestScope['javax.servlet.forward.request_uri']}</b>'
		<br />
		<br />

		<html:img id="pageIsNotFoundImgId" src="404.jpeg" width="229" height="220" alt="${messageTranslated}" />

	</div>

</tags:page>
