<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="colspan" type="java.lang.Integer" required="false" description="Column colspan" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="Column xss class" %>
<%@ attribute name="width" type="java.lang.String" required="false" description="Column width" %>
<%@ attribute name="align" type="java.lang.String" required="false" description="Column align" %>
<%@ attribute name="valign" type="java.lang.String" required="false" description="Column align" %>

<td
        <c:if test="${not empty colspan}">colspan="${colspan}" </c:if>
<c:if test="${not empty cssClass}">class="${cssClass}"</c:if>
        <c:if test="${not empty width}">width="${width}"</c:if>
        <c:if test="${not empty align}">align="${align}"</c:if>
        <c:if test="${not empty valign}">valign="${valign}"</c:if>
>
    <jsp:doBody/>
</td>
