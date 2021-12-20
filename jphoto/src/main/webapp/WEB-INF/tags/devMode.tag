<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isDevMode" value="true"/> <%-- TODO: read from properties --%>

<c:if test="${isDevMode}">
    <jsp:doBody/>
</c:if>
