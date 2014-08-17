<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="title_t" required="false" type="java.lang.String" %>
<%@ attribute name="validationMessage" required="true" type="java.lang.String" %>

<div class="text-centered" style="width: 90%;">
	<div class="photo-card-operation-impossible">${eco:translate(title_t)}</div>
	<span class="restriction-message">${validationMessage}</span>
</div>
