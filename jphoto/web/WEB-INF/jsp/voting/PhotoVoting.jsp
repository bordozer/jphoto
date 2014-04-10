<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>

<jsp:useBean id="photoVotingModel" type="ui.controllers.voting.PhotoVotingModel" scope="request"/>

<c:set var="hasError" value="<%=photoVotingModel.getBindingResult().hasErrors()%>" />

<c:if test="${photoVotingModel.showPhotoVotingForm}">
	<photo:photoVoting photo="${photoVotingModel.photo}"
					   userPhotoVotes="${photoVotingModel.userPhotoVotes}"
					   minMarkForGenre="${photoVotingModel.votingUserMinAccessibleMarkForGenre}"
					   maxMarkForGenre="${photoVotingModel.votingUserMaxAccessibleMarkForGenre}"

			/>
</c:if>

<c:if test="${hasError}">
	<tags:springErrorHighliting bindingResult="${photoVotingModel.bindingResult}"/>
</c:if>

