<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="title_t" required="false" type="java.lang.String" %>
<%@ attribute name="width" required="false" type="java.lang.Integer" %>
<%@ attribute name="footer" fragment="true" %>

<c:if test="${empty width}">
	<c:set var="width" value="400" />
</c:if>

<div class="panel panel-default block-center" style="width: ${width}px;">

	<div class="panel-heading">
		<h3 class="panel-title">${eco:translate(title_t)}</h3>
	</div>

	<div class="panel-body">
		<jsp:doBody/>
	</div>

	<div class="panel-footer">
		<jsp:invoke fragment="footer"/>
	</div>
</div>