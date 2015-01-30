<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="id" type="java.lang.String" required="false" description="Id of element" %>
<%@ attribute name="thead" fragment="true" required="false" description="table header" %>
<%@ attribute name="width" type="java.lang.String" required="false" description="table width" %>
<%@ attribute name="border" type="java.lang.String" required="false" description="table border" %>
<%@ attribute name="oddEven" type="java.lang.Boolean" required="false" description="odd and even" %>

<c:if test="${empty border}">
	<c:set var="border" value="0"/>
</c:if>

<table class="table table-bordered table-striped table-hover <c:if test="${not empty oddEven}">oddEven</c:if> " id="${id}" cellpadding="0" cellspacing="2" <c:if test="${not empty width}">width="${width}"</c:if> border="${border}">

	<c:if test="${not empty(thead)}">
		<thead class="">
		<jsp:invoke fragment="thead"/>
		</thead>
	</c:if>

	<tbody>
		<jsp:doBody/>
	</tbody>

</table>