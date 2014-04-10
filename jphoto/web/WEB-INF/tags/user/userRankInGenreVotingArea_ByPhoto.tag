<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="photo" required="false" type="core.general.photo.Photo" %>
<%@ attribute name="genre" required="true" type="core.general.genre.Genre" %>
<%@ attribute name="votingModel" required="true" type="ui.controllers.users.genreRank.VotingModel" %>

<c:set var="hasUserAlreadyVotedForThisGenre" value="${votingModel.userAlreadyVoted}"/>
<c:set var="lastVotingPoints" value="${votingModel.lastVotingPoints}"/>
<c:set var="loggedUserVotingPoints" value="${votingModel.loggedUserVotingPoints}"/>
<c:set var="userRankInGenre" value="${votingModel.userRankInGenre}"/>

<user:userRankInGenreVotingArea genre="${genre}"
							 votingModel="${votingModel}"
							 jsFunctionVoteUp="voteForUserRankInGenreByPhoto( ${photo.id}, ${genre.id}, ${loggedUserVotingPoints} ); return false;"
							 jsFunctionVoteDown="voteForUserRankInGenreByPhoto( ${photo.id}, ${genre.id}, -${loggedUserVotingPoints} ); return false;"
		/>

