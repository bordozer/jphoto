<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="photoGroupOperationModel" type="controllers.photos.groupoperations.PhotoGroupOperationModel" scope="request"/>

<tags:page pageModel="${photoGroupOperationModel.pageModel}">

	<div style="float: left; width: 80%; margin-left: 50px;">

		<h3>${eco:translate('Group operation result')}</h3>

		<c:set var="operationResults" value="${photoGroupOperationModel.operationResults}" />

		<ul>
			<c:forEach var="operationResult" items="${operationResults}">
				<c:set var="groupOperationResultType" value="${operationResult.groupOperationResultType}"/>
				<html:img12 src="groupOperations/${groupOperationResultType.icon}" alt="${groupOperationResultType.name}" /> ${operationResult.message}
				<br />
			</c:forEach>
		</ul>

		<a href="${photoGroupOperationModel.returnUrl}">${eco:translate('Return')}</a>

	</div>

	<div class="footerseparatorsmall"></div>

</tags:page>