<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isDevMode" value="<%=ApplicationContextHelper.getSystemVarsService().isDevMode()%>"/>

<c:if test="${isDevMode}">
	<jsp:doBody />
</c:if>
