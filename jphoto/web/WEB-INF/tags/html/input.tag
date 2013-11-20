<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="fieldId" required="true" type="java.lang.String" %>
<%@ attribute name="fieldValue" required="false" type="java.lang.String" %>
<%@ attribute name="onchange" required="false" type="java.lang.String" %>
<%@ attribute name="size" required="false" type="java.lang.String" %>
<%@ attribute name="maxLength" required="false" type="java.lang.String" %>

<input class="ui-state-default ui-corner-all" id="${fieldId}" type="text" name="${fieldId}" value="${fieldValue}"
	<c:if test="${not empty onchange}">onchange="${onchange}" </c:if>
	<c:if test="${not empty size}">size="${size}"</c:if>
	<c:if test="${not empty maxLength}">maxlength="${maxLength}"</c:if>
		/>
