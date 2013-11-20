<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="reindexJobModel" type="admin.controllers.jobs.edit.reindex.ReindexJobModel" scope="request"/>

<tags:page pageModel="${reindexJobModel.pageModel}">

	<admin:noParametersJobStartPage jobModel="${reindexJobModel}" />

</tags:page>