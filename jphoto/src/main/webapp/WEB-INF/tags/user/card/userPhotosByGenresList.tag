<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ tag import="com.bordozer.jphoto.ui.controllers.users.card.UserCardGenreInfo" %>
<%@ tag import="com.bordozer.jphoto.utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>
<%@ attribute name="userCardGenreInfoMap" required="true" type="java.util.Map" %>

<%
    int totalPhotos = 0;
    for (final Object o : userCardGenreInfoMap.keySet()) {
        totalPhotos += ((UserCardGenreInfo) userCardGenreInfoMap.get(o)).getPhotosQty();
    }
%>

<c:set var="totalPhotos" value="<%=totalPhotos%>"/>
<c:set var="isThisCardOfLoggedUser" value="<%=UserUtils.isTheUserThatWhoIsCurrentUser( user )%>"/>
<c:set var="isCurrentUserLoggedUser" value="<%=UserUtils.isCurrentUserLoggedUser()%>"/>
<c:set var="userCanSeeUserRankVoteHistory"
       value="<%=ApplicationContextHelper.getSecurityService().userCanSeeUserRankVoteHistory( user, EnvironmentContext.getCurrentUser() )%>"/>

<div class="user-card-block">

    <js:genreRankVotingJS/>

    <table:table border="0" width="500px" oddEven="true">

        <table:separatorInfo colspan="4" height="50" title="${eco:translate('User card: Genres and ranks')}"/>

        <table:trinfo>
            <table:td>
                <b><links:userPhotos user="${user}"/></b>
            </table:td>

            <table:td cssClass="textright" width="30">
                <b title="${eco:translate('User card: Total photos')}">${totalPhotos}</b>
            </table:td>

            <table:td width="100"/>
            <table:td width="80"/>

        </table:trinfo>

        <c:forEach var="entry" items="${userCardGenreInfoMap}">

            <c:set var="genre" value="${entry.key}"/>
            <c:set var="userCardGenreInfo" value="${entry.value}"/>

            <c:set var="genreNameTranslated" value="${eco:translateGenre(genre.id)}"/>

            <table:tr>
                <table:td>
                    <div style="float: left; width: 100%;">
                        <div style="float: left; width: 100%; height: 10px;">
                            <links:photosByUserByGenre user="${user}" genre="${genre}"/>
                        </div>
                        <div style="float: left; width: 100%; height: 15px;">
                            <div style="float: left; width: 20px;">
                                <span title="${eco:translate2( "Rank in category '$1': $2", genreNameTranslated, userCardGenreInfo.userRankInGenre)}"
                                      style="font-size: 11px;">${userCardGenreInfo.userRankInGenre}</span>:
                            </div>
                            <user:userRankInGenreRenderer userRankIconContainer="${userCardGenreInfo.userRankIconContainer}"/>
                        </div>
                    </div>
                </table:td>

                <table:td cssClass="textright">
                    <span title="${eco:translate2('Photos in category \'$1\': $2', genreNameTranslated, userCardGenreInfo.photosQty)}"><b>${userCardGenreInfo.photosQty}</b></span>
                </table:td>

                <table:td cssClass="text-centered user-genre-rank-voting-${user.id}-${genre.id}">
                    <%--<html:spinningWheel16 title="${eco:translate('Getting information from server...')}"/>--%>
                    <c:if test="${not isThisCardOfLoggedUser}">
                        <user:userRankInGenreVotingArea_ByUserAndGenre user="${user}" genre="${genre}" votingModel="${userCardGenreInfo.votingModel}"/>
                    </c:if>
                </table:td>

                <table:td cssClass="text-centered">
                    <c:set var="currentPoints" value="${userCardGenreInfo.votePointsForRankInGenre}"/>
                    <c:set var="nextPoints" value="${userCardGenreInfo.votePointsToGetNextRankInGenre}"/>
                    <c:if test="${( isThisCardOfLoggedUser && currentPoints != 0 ) || userCanSeeUserRankVoteHistory}">

                        <a href="${eco:baseUrl()}/members/${user.id}/category/${genre.id}/votes/"
                           title="${eco:translate1('Click to see who voted for the rank in category $1', genreNameTranslated)}">
                            <span class='current-points-${user.id}-${genre.id}'>${currentPoints}</span>
                        </a>
                        /
                        <span title="${eco:translate3('There are $1 point(s) more is necessary to achieve rank $2 in category \'$3\'', nextPoints - currentPoints, userCardGenreInfo.userRankInGenre + 1, genreNameTranslated )}">
                                ${nextPoints}
                        </span>
                    </c:if>
                </table:td>

            </table:tr>

        </c:forEach>

    </table:table>

    <script type="text/javascript">
        var userId_card = ${user.id};
    </script>

    <%--<script data-main="${eco:baseUrl()}/js/modules/users/card/genreRankVoting/votingArea/require-config-genreRank.js" src="${eco:baseUrl()}/js/lib/front-end/require.js"></script>--%>
</div>
