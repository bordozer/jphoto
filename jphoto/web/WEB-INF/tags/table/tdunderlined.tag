<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="cssClass" type="java.lang.String" required="false" description="Additional CSS class" %>
<%@ attribute name="colspan" type="java.lang.Integer" required="false" description="Column colspan" %>
<%@ attribute name="width" type="java.lang.String" required="false" description="Column width" %>

<td class="trunderlined ${cssClass}" <c:if test="${not empty colspan}">colspan="${colspan}"</c:if> <c:if test="${not empty width}">width="${width}"</c:if> >
	<jsp:doBody/>
</td>