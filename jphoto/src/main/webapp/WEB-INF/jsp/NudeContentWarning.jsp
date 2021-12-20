<%@ page import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ page import="com.bordozer.jphoto.ui.elements.PageModel" %>
<%@ page import="com.bordozer.jphoto.ui.elements.PageTitleData" %>
<%@ page import="com.bordozer.jphoto.utils.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="exceptionModel" type="com.bordozer.jphoto.core.exceptions.ExceptionModel" scope="request"/>

<%
    final String title = exceptionModel.getTranslatorService().translate("Nude content warning", EnvironmentContext.getLanguage());
    final PageModel pageModel = new PageModel();
    pageModel.setPageTitleData(new PageTitleData(title, title, title));
%>

<c:set var="isLoggedUserMode" value="<%=UserUtils.isCurrentUserLoggedUser()%>"/>

<c:set var="title" value="<%=title%>"/>
<c:set var="pageModel" value="<%=pageModel%>"/>

<c:set var="messageTranslated" value="title"/>
<c:set var="redirectToIfAcceptUrl" value="${exceptionModel.exceptionUrl}"/>
<c:set var="redirectToIfDeclineUrl" value="${exceptionModel.refererUrl}"/>

<tags:page pageModel="${pageModel}">

    <form id="viewingNudeContentForm" method="POST" action="${eco:baseUrl()}/nudeContent/">
        <input type="hidden" id="redirectToIfAcceptUrl" name="redirectToIfAcceptUrl" value="${redirectToIfAcceptUrl}">
        <input type="hidden" id="redirectToIfDeclineUrl" name="redirectToIfDeclineUrl" value="${redirectToIfDeclineUrl}">

        <div class="row">

            <div class="col-lg-12 text-center">

                <div class="panel panel-default" style="width: 1000px; margin-left: auto; margin-right: auto;">

                    <div class="panel-heading">
                        <h3 class="page-title">
                                ${eco:translate( 'Nude content warning' ) }
                        </h3>
                    </div>

                    <div class="panel-body">
                        <div class="col-lg-6 text-center">
                            <img src="${eco:imageFolderURL()}/nude_content.jpg" title="${messageTranslated}"/>
                            <br/>
                            <br/>
                            <html:submitButton id="IConfirmShowingNudeContent" caption_t="I am over 18. Show nude content"/>
                        </div>

                        <div class="col-lg-6 text-center">
                            <img src="${eco:imageFolderURL()}/no_nude_content.jpg" title="${messageTranslated}"/>
                            <br/>
                            <br/>
                            <html:submitButton id="IDeclineShowingNudeContent" caption_t="Do NOT show nude content"/>
                        </div>
                    </div>

                    <div class="panel-footer text-left">
                        <c:if test="${isLoggedUserMode}">
                            ${eco:translate('* If you want to switch nude content on by default, you can do this in the settings of your account')}
                        </c:if>

                        <c:if test="${not isLoggedUserMode}">
                            ${eco:translate('* This choice of NUDE visibility will be applied for all photos for your current session')}
                        </c:if>
                    </div>

                </div>

            </div>
        </div>

    </form>

</tags:page>
