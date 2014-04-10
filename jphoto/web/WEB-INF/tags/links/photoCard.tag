<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="id" required="true" type="java.lang.Integer" %>
<%@ attribute name="title" required="false" type="java.lang.String" %>

<c:set var="link" value="<%=ApplicationContextHelper.getUrlUtilsService().getPhotoCardLink( id )%>" />

<a class="photo-link" href ="${link}" <c:if test="${not empty title}">title="${title}" </c:if> ><jsp:doBody/></a>