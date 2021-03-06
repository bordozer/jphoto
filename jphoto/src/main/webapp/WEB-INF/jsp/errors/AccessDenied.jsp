<%@ page import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ page import="com.bordozer.jphoto.ui.elements.PageModel" %>
<%@ page import="com.bordozer.jphoto.ui.elements.PageTitleData" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="exceptionModel" type="com.bordozer.jphoto.core.exceptions.ExceptionModel" scope="request"/>

<%
    final String title = exceptionModel.getTranslatorService().translate("Access denied page title", EnvironmentContext.getLanguage());
    final PageModel pageModel = new PageModel();
    pageModel.setPageTitleData(new PageTitleData(title, title, title));
%>

<c:set var="title" value="<%=title%>"/>
<c:set var="pageModel" value="<%=pageModel%>"/>
<c:set var="exceptionMessage" value="${exceptionModel.exceptionMessage}"/>

<c:set var="messageTranslated" value="${title}"/>

<tags:page pageModel="${pageModel}">

    <div class="errorHandlingMessage">
        <h1>${title}</h1>
            ${eco:translate( 'Sorry, but you do not have permission to perform this operation' ) }
        <h3>${exceptionMessage}</h3>

        <br/>
        <br/>

        <html:img id="accessdenied" src="icons128/accessdenied.png" width="128" height="128" alt="${messageTranslated}"/>
    </div>

</tags:page>
