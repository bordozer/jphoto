<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="linkToFullList" required="true" type="java.lang.String" %>
<%@ attribute name="linkToFullListText" required="true" type="java.lang.String" %>

<c:if test="${not empty linkToFullList}">

    <c:set var="text" value="${linkToFullListText}"/>

    <a href="${linkToFullList}" title="${eco:translate('Show all photos of the list')}">
            ${text}
    </a>

</c:if>
