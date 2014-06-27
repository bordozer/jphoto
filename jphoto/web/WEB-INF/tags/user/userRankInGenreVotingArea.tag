<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>
<%@ attribute name="votingModel" required="true" type="ui.controllers.users.genreRank.VotingModel" %>
<%@ attribute name="jsFunctionVoteUp" required="true" type="java.lang.String" %>
<%@ attribute name="jsFunctionVoteDown" required="true" type="java.lang.String" %>

<c:set var="hasUserAlreadyVotedForThisGenre" value="${votingModel.userAlreadyVoted}"/>
<c:set var="lastVotingPoints" value="${votingModel.lastVotingPoints}"/>
<c:set var="loggedUserVotingPoints" value="${votingModel.loggedUserVotingPoints}"/>
<c:set var="userRankInGenre" value="${votingModel.userRankInGenre}"/>

<c:set var="genreNameTranslated" value="${eco:translateGenre(genre.id)}"/>

<div id="user_by_genre_voting_${genre.id}">

	<c:if test="${! votingModel.validationResult.uiVotingIsInaccessible}">
		<c:if test="${not hasUserAlreadyVotedForThisGenre}">
			-${loggedUserVotingPoints}
			<a href="#" onclick="${jsFunctionVoteDown}" title="${eco:translate3('The member does not deserve his rank $1 (-$2) in category \'$3\'', userRankInGenre, loggedUserVotingPoints, genreNameTranslated)}">
				<html:img id="rank_down_${genre.id}" src="genre_rank_down.png" width="16" height="16"/>
			</a>

			&nbsp;

			<a href="#" onclick="${jsFunctionVoteUp}" title="${eco:translate3('The member deserves higher rank than his current one $1 (+$2) in category \'$3\'', userRankInGenre, loggedUserVotingPoints, genreNameTranslated)}">
				<html:img id="rank_up_${genre.id}" src="genre_rank_up.png" width="16" height="16"/>
			</a>
			+${loggedUserVotingPoints}
		</c:if>

		<c:if test="${hasUserAlreadyVotedForThisGenre}">
			<span title="${eco:translate2('You have already voted when member\'s rank is $1 in category \'$2\'', userRankInGenre, genreNameTranslated)}">
				<b>${lastVotingPoints > 0 ? '+' : ''}${lastVotingPoints}</b>
			</span>
		</c:if>
	</c:if>

	<c:if test="${votingModel.validationResult.uiVotingIsInaccessible}">

		<c:set var="errorMessage" value="${eco:translate1('You can not vote for the member rank in category $1:', genreNameTranslated)}<br /><br />${votingModel.validationResult.validationMessage}"/>

		<span title="${eco:translate('userRankInGenreVotingArea: Not accessible')}">${eco:translate('userRankInGenreVotingArea: N/A')}</span>
		<a href="#" title="${eco:translate('You can not vote for the member\'s rank in category. Click to see detailed message')}" onclick="return false;">
			<html:img12 src="icons16/help16.png" onclick="showUIMessage_InformationMessage_ManualClosing('${errorMessage}');" />
		</a>

	</c:if>

</div>