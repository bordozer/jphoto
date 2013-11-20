<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="membershipJobModel" type="admin.controllers.jobs.edit.membership.MembershipJobModel" scope="request"/>

<tags:page pageModel="${membershipJobModel.pageModel}">

	<admin:noParametersJobStartPage jobModel="${membershipJobModel}" />

</tags:page>