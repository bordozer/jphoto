<%@ taglib prefix="eco" uri="http://jfoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="photoStorageSynchronizationJobModel" type="admin.controllers.jobs.edit.photoStorage.PhotoStorageSynchronizationJobModel" scope="request"/>

<tags:page pageModel="${photoStorageSynchronizationJobModel.pageModel}">

	<admin:noParametersJobStartPage jobModel="${photoStorageSynchronizationJobModel}" />

</tags:page>