<%@ tag import="controllers.users.card.UserCardGenreInfo" %>
<%@ tag import="utils.UserUtils" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="genreInfoMap" required="true" type="java.util.Map" %>

<%
	int totalPhotos = 0;
	for ( final Object o : genreInfoMap.keySet() ) {
		totalPhotos += (( UserCardGenreInfo ) genreInfoMap.get( o )).getPhotosQty();
	}
%>

<c:set var="totalPhotos" value="<%=totalPhotos%>"/>
<c:set var="isThisCardOfLoggedUser" value="<%=UserUtils.isUserEqualsToCurrentUser( user )%>"/>
<c:set var="isCurrentUserLoggedUser" value="<%=UserUtils.isCurrentUserLoggedUser()%>"/>
<c:set var="userCanSeeUserRankVoteHistory" value="<%=ApplicationContextHelper.getSecurityService().userCanSeeUserRankVoteHistory( user, EnvironmentContext.getCurrentUser() )%>"/>

<div id="userPhotosByGenreDiv">

	<js:genreRankVotingJS />

	<table:table border="0" width="100%" oddEven="true">

		<table:separatorInfo colspan="4" height="50" title="${eco:translate('Photos and ranks')}"/>

		<table:trinfo>
			<table:td cssClass="titlecolumn">
				<b><links:userPhotos user="${user}"/></b>
			</table:td>

			<table:td cssClass="textright">
				<b title="${eco:translate('Total photos')}">${totalPhotos}</b>
			</table:td>

			<table:td/>
			<table:td/>

		</table:trinfo>

		<c:forEach var="entry" items="${genreInfoMap}">

			<c:set var="genre" value="${entry.key}"/>
			<c:set var="cardGenreInfo" value="${entry.value}"/>

			<c:set var="votingModel" value="${cardGenreInfo.votingModel}"/>

			<table:tr>
				<table:td cssClass="titlecolumn">
					<div style="float: left; width: 100%; height: 25px;">
						<div style="float: left; width: 100%; height: 10px;">
							<links:photosByUserByGenre user="${user}" genre="${genre}"/>
						</div>
						<div style="float: left; width: 100%; height: 3px;">
							<user:userRankInGenreCurrent user="${user}"
												  genre="${genre}"
												  rank="${votingModel.userRankInGenre}"
												  userHasEnoughPhotos="${votingModel.userHasEnoughPhotosInGenre}"
									/>
						</div>
					</div>
				</table:td>

				<table:td cssClass="textright">
					<span title="${eco:translate2('Photos in category \'$1\': $2', genre.name, cardGenreInfo.photosQty)}"><b>${cardGenreInfo.photosQty}</b></span>
				</table:td>

				<table:td cssClass="textcentered user-genre-rank-voting-${user.id}-${genre.id}">
					<%--<html:spinningWheel16 title="${eco:translate('Getting information from server...')}"/>--%>
					<c:if test="${not isThisCardOfLoggedUser}">
						<user:voteForUserRankInGenreByUserAndGenre user="${user}" genre="${genre}" votingModel="${cardGenreInfo.votingModel}" />
					</c:if>
				</table:td>

				<table:td>
					<c:if test="${( isThisCardOfLoggedUser && cardGenreInfo.votePointsForRankInGenre != 0 ) || userCanSeeUserRankVoteHistory}">
						<c:set var="currentPoints" value="${cardGenreInfo.votePointsForRankInGenre}" />
						<c:set var="nextPoints" value="${cardGenreInfo.votePointsToGetNextRankInGenre}" />

						<a href="${eco:baseUrlWithPrefix()}/members/${user.id}/category/${genre.id}/votes/" title="${eco:translate1('Click to see who voted for the rank in category $1', genre.name)}">
							<span class='current-points-${user.id}-${genre.id}'>${currentPoints}</span>
						</a>
						/
						<span title="${eco:translate3('There are $1 point(s) more is necessary to achieve rank $2 in category \'$3\'', nextPoints - currentPoints, votingModel.userRankInGenre + 1, genre.name )}">${nextPoints}</span>
					</c:if>
				</table:td>
			</table:tr>

		</c:forEach>

	</table:table>

	<script type="text/javascript">
		var userId_card = ${user.id};
	</script>

	<%--<script data-main="<c:url value="/common/js/modules/users/card/genreRankVoting/votingArea/require-config-genreRank.js"/>" src="<c:url value="/common/js/lib/front-end/require.js"/>"></script>--%>
</div>