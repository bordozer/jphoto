<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>
<%@ attribute name="marksByCategoryInfos" required="true" type="java.util.List" %>

<table:table border="0" width="100%" oddEven="true">

    <table:separatorInfo colspan="3" height="50" title="${eco:translate('User Statistics: Tab: Last set marks')}"/>

    <c:forEach var="marksByCategoryInfo" items="${marksByCategoryInfos}">
        <table:trinfo>

            <table:tdright>
                <links:photosByUserByVotingCategory user="${user}" votingCategory="${marksByCategoryInfo.photoVotingCategory}"/>
            </table:tdright>

            <table:td cssClass="textright">
                <c:set var="photoVotingCategoryName" value="${eco:translateVotingCategory(marksByCategoryInfo.photoVotingCategory.id)}"/>
                <span title="${eco:translate3('User Statistics: $1 marked $2 photo(s) as $3'
				, user.nameEscaped, marksByCategoryInfo.quantity, photoVotingCategoryName)}">${marksByCategoryInfo.quantity} ${eco:translate('ROD PLURAL photos')}</span>
            </table:td>

            <table:td cssClass="textright">
                <span title="${eco:translate2('User Statistics: Summary marks set for $1: $2', photoVotingCategoryName, marksByCategoryInfo.sumMark)}">${marksByCategoryInfo.sumMark} ${eco:translate('ROD PLURAL marks')}</span>
            </table:td>

        </table:trinfo>
    </c:forEach>

</table:table>
