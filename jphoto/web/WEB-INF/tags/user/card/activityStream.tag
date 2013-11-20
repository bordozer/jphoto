<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<%@ attribute name="activities" required="true" type="java.util.List" %>

<c:if test="${not empty activities}">

	<tags:paging showSummary="false"/>

	<div style="margin-left: auto; margin-right: auto; width: 600px;">
		<tags:activityStream activities="${activities}" hideUser="true" />
	</div>

	<tags:paging showSummary="true"/>

</c:if>

<c:if test="${empty activities}">
	<div style="float: left; text-align: center; width: 100%;">
		<h3>${eco:translate('There is no any activity yet')}</h3>
	</div>
</c:if>