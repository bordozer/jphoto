<%@ page import="com.bordozer.jphoto.core.exceptions.notFound.NotFoundExceptionEntryType" %>
<%@ page import="com.bordozer.jphoto.core.services.translator.TranslatorService" %>
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
    final NotFoundExceptionEntryType entryType = exceptionModel.getNotFoundExceptionEntryType();
    final TranslatorService translatorService = exceptionModel.getTranslatorService();
    final String titleTranslated = translatorService.translate("$1 not found", EnvironmentContext.getLanguage(), translatorService.translate(entryType.getName(), EnvironmentContext.getLanguage()));

    final PageModel pageModel = new PageModel();
    pageModel.setPageTitleData(new PageTitleData(titleTranslated, titleTranslated, titleTranslated));
%>
<c:set var="titleTranslated" value="<%=titleTranslated%>"/>
<c:set var="pageModel" value="<%=pageModel%>"/>
<c:set var="exceptionMessage" value="${exceptionModel.exceptionMessage}"/>

<tags:page pageModel="${pageModel}">

    <div class="errorHandlingMessage">
        <h1>${titleTranslated}</h1>
            ${exceptionMessage}

        <br/>
        <br/>

        <html:img id="entryNotFound" src="entryNotFound.png" width="256" height="256" alt="${titleTranslated}"/>
    </div>

</tags:page>
