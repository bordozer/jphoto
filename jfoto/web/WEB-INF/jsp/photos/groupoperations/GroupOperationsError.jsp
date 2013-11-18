<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="photoGroupOperationModel" type="controllers.photos.groupoperations.PhotoGroupOperationModel" scope="request"/>

<tags:page pageModel="${photoGroupOperationModel.pageModel}">

	<h3>${eco:translate('Group operation error')}</h3>
	
	${eco:translate('Click BACK')}

	<tags:springErrorHighliting bindingResult="${photoGroupOperationModel.bindingResult}" />

	<div class="footerseparatorsmall"></div>

</tags:page>