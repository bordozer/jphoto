<%@ tag import="com.bordozer.jphoto.core.services.system.ConfigurationService" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>
<%@ attribute name="genre" required="true" type="com.bordozer.jphoto.core.general.genre.Genre" %>
<%@ attribute name="votingModel" required="true" type="com.bordozer.jphoto.ui.controllers.users.genreRank.VotingModel" %>

<c:set var="hasUserAlreadyVotedForThisGenre" value="${votingModel.userAlreadyVoted}"/>
<c:set var="lastVotingPoints" value="${votingModel.lastVotingPoints}"/>
<c:set var="loggedUserVotingPoints" value="${votingModel.loggedUserVotingPoints}"/>
<c:set var="userRankInGenre" value="${votingModel.userRankInGenre}"/>

<user:userRankInGenreVotingArea genre="${genre}"
                                votingModel="${votingModel}"
                                jsFunctionVoteUp="voteForUserRankInGenre( ${user.id}, ${genre.id}, ${loggedUserVotingPoints} ); return false;"
                                jsFunctionVoteDown="voteForUserRankInGenre( ${user.id}, ${genre.id}, -${loggedUserVotingPoints} ); return false;"
/>

