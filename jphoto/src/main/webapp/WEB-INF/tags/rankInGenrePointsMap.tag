<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="ranksInGenrePointsMap" required="true" type="java.util.Map" %>
<%@ attribute name="hideTitle" required="false" type="java.lang.Boolean" %>
<%@ attribute name="currentPoints" required="false" type="java.lang.Integer" %>

<c:if test="${not hideTitle}">
    <b>${eco:translate('Photo category points staircase:')}</b>
</c:if>

<br/>

<c:set var="isCurrentPointsShown" value="false"/>

<c:forEach var="entry" items="${ranksInGenrePointsMap}" varStatus="status">

    <c:set var="rank" value="${entry.key}"/>
    <c:set var="points" value="${entry.value}"/>

    <c:set var="isCurrentPoints" value="${points == currentPoints}"/>

    <c:if test="${(currentPoints <= points) and (not isCurrentPointsShown)}">
        <span title="${eco:translate1('Current points: $1', currentPoints)}">... <b>${currentPoints}</b> ...</span> ,
        <c:set var="isCurrentPointsShown" value="true"/>
    </c:if>

    <c:if test="${not((currentPoints == points) and (isCurrentPointsShown))}">
		<span title="${eco:translate2('There is necessary to have $1 votes to get rank $2 in a photo category', points, rank)}">
                ${points}
        </span>

        ${not status.last ? ', ' : '...'}
    </c:if>

</c:forEach>
