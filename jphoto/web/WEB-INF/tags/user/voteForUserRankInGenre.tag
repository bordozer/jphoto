<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>
<%@ attribute name="votingModel" required="true" type="controllers.users.genreRank.VotingModel" %>
<%@ attribute name="jsFunctionVoteUp" required="true" type="java.lang.String" %>
<%@ attribute name="jsFunctionVoteDown" required="true" type="java.lang.String" %>

<c:set var="hasUserAlreadyVotedForThisGenre" value="${votingModel.userAlreadyVoted}"/>
<c:set var="lastVotingPoints" value="${votingModel.lastVotingPoints}"/>
<c:set var="loggedUserVotingPoints" value="${votingModel.loggedUserVotingPoints}"/>
<c:set var="userRankInGenre" value="${votingModel.userRankInGenre}"/>

<div id="user_by_genre_voting_${genre.id}">

	<c:set var="validationResult" value="${votingModel.validationResult}"/>

	<c:if test="${! votingModel.validationResult.uiVotingIsInaccessible}">
		<c:if test="${not hasUserAlreadyVotedForThisGenre}">
			<a href="#" onclick="${jsFunctionVoteDown}" title="${eco:translate3('Member\'s photos in \'$1\' are not deserving his rank $2 ( -$3)', genre.name, userRankInGenre, loggedUserVotingPoints)}">

				<html:img id="rank_down_${genre.id}" src="genre_rank_down.png" width="16" height="16"/>
			</a>

			&nbsp;&nbsp;&nbsp;

			<a href="#" onclick="${jsFunctionVoteUp}" title="${eco:translate3('Member\'s photos in \'$1\' are deserving more then his current rank $2 (+$3)', genre.name, userRankInGenre, loggedUserVotingPoints)}">

				<html:img id="rank_up_${genre.id}" src="genre_rank_up.png" width="16" height="16"/>
			</a>
		</c:if>

		<c:if test="${hasUserAlreadyVotedForThisGenre}">
			<span title="${eco:translate2('You have already voted for member\'s rank $1 in category \'$2\'', userRankInGenre, genre.name)}">
				<b>${lastVotingPoints > 0 ? '+' : ''}${lastVotingPoints}</b>
			</span>
		</c:if>
	</c:if>

	<c:if test="${votingModel.validationResult.uiVotingIsInaccessible}">
		<c:set var="delimeter" value="<br /><br />" />
		<c:set var="errorMessage" value="${eco:translate1('You can not vote for the member rank in category $1:', genre.name)}${delimeter}"/>

		<c:forEach var="validationMessage" items="${validationResult.validationMessage}">
			<c:set var="errorMessage" value="${errorMessage}${validationMessage}" />
		</c:forEach>

		<span title="${eco:translate('Not accessible')}">${eco:translate('N/A')}</span>
		<a href="#" title="${eco:translate('You can not vote for the member\'s rank in category. Click to see detailed message')}" onclick="return false;">
			<html:img12 src="icons16/help16.png" onclick="showInformationMessageNoAutoClose('${errorMessage}');" />
		</a>
	</c:if>

</div>