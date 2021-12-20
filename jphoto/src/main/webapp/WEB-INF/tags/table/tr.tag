<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="cssClass" type="java.lang.String" required="false" description="Column xss class" %>

<tr <c:if test="${not empty cssClass}">class="${cssClass}"</c:if>>
    <jsp:doBody/>
</tr>
