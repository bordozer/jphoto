<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="inputId" required="true" type="java.lang.String" %>
<%@ attribute name="inputValue" required="true" type="java.lang.String" %>
<%@ attribute name="isChecked" required="false" type="java.lang.Boolean" %>
<%@ attribute name="onClick" required="false" type="java.lang.String" %>

<input id="${inputId}" name="${inputId}" type="checkbox" value="${inputValue}" ${isChecked ? 'checked' : ''}
       <c:if test="${not empty onClick}">onclick="${onClick}"</c:if> class="ui-widget-content ui-corner-all"/>
