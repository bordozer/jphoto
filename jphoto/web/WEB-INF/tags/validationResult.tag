<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="title_t" required="false" type="java.lang.String" %>
<%@ attribute name="validationMessage" required="true" type="java.lang.String" %>

<div class="floatleft textcentered">
	<h3>${title_t}</h3>
	<b>${validationMessage}</b>
</div>
