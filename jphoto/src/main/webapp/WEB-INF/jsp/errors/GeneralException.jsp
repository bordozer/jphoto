<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ page import="com.bordozer.jphoto.ui.elements.PageModel" %>
<%@ page import="com.bordozer.jphoto.ui.elements.PageTitleData" %>
<%@ page import="org.apache.commons.lang.exception.ExceptionUtils" %>
<%@ page isErrorPage="true" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<jsp:useBean id="exceptionModel" type="com.bordozer.jphoto.core.exceptions.ExceptionModel" scope="request"/>

<%
    final String title = exceptionModel.getTranslatorService().translate("General exception: Oops!", EnvironmentContext.getLanguage());

    final String[] traceElements = ExceptionUtils.getStackFrames(exception);

    for (String traceElement : traceElements) {
        System.out.println(traceElement);
    }

    /* TODO: print root cace */
//    final String rootCause = String.format("%s", traceElements.length > 1 ? traceElements[0] : "unknown");
//    traceElements[0] = String.format("<b>%s</b>", traceElements[0]);

    final PageModel pageModel = new PageModel();
    pageModel.setPageTitleData(new PageTitleData(title, title, title));
%>

<c:set var="title" value="<%=title%>"/>
<c:set var="pageModel" value="<%=pageModel%>"/>

<%--<c:set var="rootCause" value="<%=rootCause%>"/>--%>
<c:set var="traceElements" value="<%=traceElements%>"/>

<c:set var="messageOops" value="General exception: Oops!"/>

<tags:page pageModel="${pageModel}">

    <html:img id="generalExceptionImg" src="ooops1.jpg" width="170" height="120" alt="${title}"/>
    <br/>

    URL: <b>${requestScope['javax.servlet.forward.request_uri']}</b>
    <br/>
<%--    Exception: <b>${rootCause}</b>--%>
    <br/>
    <br/>

    <%--<b>${rootCause}</b>--%>
    <br/>

    <c:forEach var="traceElement" items="${traceElements}">
        ${traceElement}
        <br/>
    </c:forEach>

</tags:page>
