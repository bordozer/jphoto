<%@ page import="elements.PageTitleData" %>
<%@ page import="elements.PageModel" %>
<%@ page import="utils.UserUtils" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true"%>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="exceptionModel" type="core.exceptions.ExceptionModel" scope="request"/>

<%
	final String title = exceptionModel.getTranslatorService().translate( "Nude content warning", EnvironmentContext.getLanguage() );
	final PageModel pageModel = new PageModel();
	pageModel.setPageTitleData( new PageTitleData( title, title, title ) );
%>

<c:set var="isLoggedUserMode" value="<%=UserUtils.isCurrentUserLoggedUser()%>" />

<c:set var="title" value="<%=title%>" />
<c:set var="pageModel" value="<%=pageModel%>" />

<c:set var="messageTranslated" value="${eco:translate( title ) }" />
<c:set var="redirectToIfAcceptUrl" value="${exceptionModel.exceptionUrl}" />
<c:set var="redirectToIfDeclineUrl" value="${exceptionModel.refererUrl}" />

<tags:page pageModel="${pageModel}">

	<form id="viewingNudeContentForm" method="POST" action="${eco:baseUrlWithPrefix()}/nudeContent/">
		<input type="hidden" id="redirectToIfAcceptUrl" name="redirectToIfAcceptUrl" value="${redirectToIfAcceptUrl}">
		<input type="hidden" id="redirectToIfDeclineUrl" name="redirectToIfDeclineUrl" value="${redirectToIfDeclineUrl}">

		<br />
		<br />

		<table:table width="500">

			<table:separatorInfo colspan="2" title="${eco:translate( 'Nude content warning' ) }" />

			<table:tr>
				<table:td cssClass="textcentered"><img src="${eco:imageFolderURL()}/nude_content.jpg" title="${messageTranslated}" /></table:td>
				<table:td cssClass="textcentered"><img src="${eco:imageFolderURL()}/no_nude_content.jpg" title="${messageTranslated}" /></table:td>
			</table:tr>

			<table:tr>
				<table:td cssClass="textcentered"><html:submitButton id="IConfirmShowingNudeContent" caption_t="I am over 18. Show nude content" /></table:td>
				<table:td cssClass="textcentered"><html:submitButton id="IDeclineShowingNudeContent" caption_t="Do NOT show nude content" /></table:td>
			</table:tr>

		</table:table>

	</form>

	<c:if test="${isLoggedUserMode}">
		<br />
		${eco:translate('* If you want to swith nude content on by defaule, you can do this in the settings of your account')}
	</c:if>

</tags:page>
