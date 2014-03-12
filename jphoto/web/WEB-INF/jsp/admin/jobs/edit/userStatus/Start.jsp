<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="userStatusRecalculationJobModel" type="admin.controllers.jobs.edit.userStatus.UserStatusRecalculationJobModel" scope="request"/>

<tags:page pageModel="${userStatusRecalculationJobModel.pageModel}">

	<admin:noParametersJobStartPage jobModel="${userStatusRecalculationJobModel}" />

</tags:page>