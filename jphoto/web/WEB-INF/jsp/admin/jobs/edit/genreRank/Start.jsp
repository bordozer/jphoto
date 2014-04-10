<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="genreRankRecalculationModel" type="admin.controllers.jobs.edit.genreRank.UsersGenresRanksRecalculationModel" scope="request"/>

<tags:page pageModel="${genreRankRecalculationModel.pageModel}">

	<admin:noParametersJobStartPage jobModel="${genreRankRecalculationModel}" />

</tags:page>