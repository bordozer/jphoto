<%@ page import="elements.PageModel" %>
<%@ page import="elements.PageTitleData" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%
	final String title = ApplicationContextHelper.getTranslatorService().translate( "The page is under construction", EnvironmentContext.getLanguage() );
	final PageModel pageModel = new PageModel();
	pageModel.setPageTitleData( new PageTitleData( title, title, title ) );
%>

<c:set var="title" value="<%=title%>" />
<c:set var="pageModel" value="<%=pageModel%>" />

<tags:page pageModel="${pageModel}">

	<div class="errorHandlingMessage">
		<tags:underConstruction />
	</div>

</tags:page>