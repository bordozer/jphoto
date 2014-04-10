<%@ tag import="core.services.utils.DateUtilsServiceImpl" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="uploadTime" required="true" type="java.util.Date" %>
<%@ attribute name="text" required="false" type="java.lang.String" %>

<c:set var="uploadDate" value="${eco:formatDate(uploadTime)}" />

<a href="${eco:baseUrlWithPrefix()}/photos/date/${uploadDate}/uploaded/" title="${eco:translate1('Show photos on $1', uploadDate)}">
	<c:if test="${not empty text}">${text}</c:if>
	<c:if test="${empty text}">${uploadDate}</c:if>
</a>