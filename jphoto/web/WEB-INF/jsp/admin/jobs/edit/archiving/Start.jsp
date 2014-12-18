<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="archivingJobJobModel" type="admin.controllers.jobs.edit.archiving.ArchivingJobJobModel" scope="request"/>

<tags:page pageModel="${archivingJobJobModel.pageModel}">

	<admin:noParametersJobStartPage jobModel="${archivingJobJobModel}" />

</tags:page>