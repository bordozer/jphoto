<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<jsp:useBean id="votesForUserRankInGenreModel" type="com.bordozer.jphoto.ui.controllers.users.votes.VotesForUserRankInGenreModel" scope="request"/>

<tags:page pageModel="${votesForUserRankInGenreModel.pageModel}">

    <c:set var="user" value="${votesForUserRankInGenreModel.user}"/>
    <c:set var="genre" value="${votesForUserRankInGenreModel.genre}"/>

    <c:set var="userGenreRankViewEntries" value="${votesForUserRankInGenreModel.userGenreRankViewEntries}"/>

    <c:set var="separator" value="&nbsp;&nbsp;"/>
    <c:set var="colspan" value="3"/>

    <table:table width="600">

        <table:separatorInfo colspan="${colspan}"
                             title="${eco:translate2('Voting for $1 rank in category $2', eco:userCardLink(user), eco:photosByUserByGenreLink( user, genre) )}"/>

        <c:set var="prevVotingTime" value="0"/>

        <c:forEach var="userGenreRankViewEntry" items="${userGenreRankViewEntries}">

            <table:tr>

                <table:tdunderlined>
                    ${separator}${userGenreRankViewEntry.column1}
                </table:tdunderlined>

                <table:tdunderlined cssClass="text-centered">
                    <c:if test="${userGenreRankViewEntry.statusChangeEntry}">
                        <user:userRankInGenreRenderer userRankIconContainer="${userGenreRankViewEntry.userRankWhenPhotoWasUploadedIconContainer}"/>
                    </c:if>

                    <c:if test="${not userGenreRankViewEntry.statusChangeEntry}">
                        ${userGenreRankViewEntry.column2}
                    </c:if>
                </table:tdunderlined>

                <table:tdunderlined cssClass="text-centered">
                    ${userGenreRankViewEntry.column3}
                </table:tdunderlined>

            </table:tr>

        </c:forEach>

        <table:tr>
            <table:td>${separator} ${eco:translate('Total points:')}</table:td>
            <table:td cssClass="text-centered">
                <b>${votesForUserRankInGenreModel.sumPoints > 0 ? '+' : ''}${votesForUserRankInGenreModel.sumPoints}</b>
            </table:td>
            <table:td/>
        </table:tr>

        <table:separatorInfo colspan="${colspan}" title="${eco:translate('Photo category points staircase:')}"/>

        <table:tr>
            <table:td cssClass="text-centered" colspan="${colspan}">
                <tags:rankInGenrePointsMap
                        ranksInGenrePointsMap="${votesForUserRankInGenreModel.ranksInGenrePointsMap}"
                        hideTitle="true"
                        currentPoints="${votesForUserRankInGenreModel.sumPoints}"
                />
            </table:td>
        </table:tr>

    </table:table>

    <br/>

    * ${eco:translate('The rank could not correspond gotten voices due to ranks in categories are recalculated periodically.')}

    <div class="footerseparator"></div>

</tags:page>
