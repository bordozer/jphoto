<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="caption_t" required="true" type="java.lang.String" %>
<%@ attribute name="onclick" type="java.lang.String" required="false" description="Button onclick" %>

<input type="reset" name="resetButton" value="${eco:translate(caption_t)}" class="ui-state-default ui-corner-all"
	<c:if test="${not empty onclick}">onclick="${onclick}"</c:if> />