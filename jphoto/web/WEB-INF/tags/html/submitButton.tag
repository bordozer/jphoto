<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="id" type="java.lang.String" required="true" description="Button ID" %>
<%@ attribute name="caption_t" required="true" type="java.lang.String" %>
<%@ attribute name="onclick" type="java.lang.String" required="false" description="Button onclick" %>
<%@ attribute name="doNotTranslate" type="java.lang.Boolean" required="false" %>

<c:set var="buttonTitle" value="${eco:translate(caption_t)}" />
<c:if test="${doNotTranslate}">
	<c:set var="buttonTitle" value="${caption_t}" />
</c:if>

<button type="submit" id="${id}" name="${id}" class="ui-state-default ui-corner-all"
	<c:if test="${not empty onclick}">onclick="${onclick}"</c:if> title="${buttonTitle}" >
	${buttonTitle}
</button>