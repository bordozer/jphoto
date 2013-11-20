<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isDevMode" value="<%=ApplicationContextHelper.getSystemVarsService().isDevMode()%>"/>

<c:if test="${isDevMode}">
	<jsp:doBody />
</c:if>
