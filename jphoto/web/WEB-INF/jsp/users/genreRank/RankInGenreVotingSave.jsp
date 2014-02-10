<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="userGenreRankVotingModel" type="controllers.users.genreRank.UserGenreRankVotingModel" scope="request"/>

<c:set var="user" value="${userGenreRankVotingModel.user}" />
<c:set var="genre" value="${userGenreRankVotingModel.genre}" />
<c:set var="votingModel" value="${userGenreRankVotingModel.votingModel}" />

<user:userRankInGenreVotingArea_ByUserAndGenre user="${user}" genre="${genre}" votingModel="${votingModel}" />

<tags:springErrorHighliting bindingResult="${userGenreRankVotingModel.bindingResult}" />