<%@ tag import="com.bordozer.jphoto.ui.activity.ActivityType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>
<%@ attribute name="activities" required="true" type="java.util.List" %>
<%@ attribute name="filterActivityTypeId" required="true" type="java.lang.Integer" %>

<div class="col-lg-12">

    <div class="row">
        <tags:activityStreamFilter activityTypeValues="<%=ActivityType.USER_ACTIVITIES%>"
                                   filterActivityTypeId="${filterActivityTypeId}"
                                   url="${eco:baseUrl()}/members/${user.id}/card/activity/"
        />
    </div>

    <c:if test="${not empty activities}">

        <tags:paging showSummary="false"/>

        <div class="row">
            <tags:activityStream activities="${activities}" hideUser="true"/>
        </div>

        <tags:paging showSummary="true"/>

    </c:if>

    <c:if test="${empty activities}">
        <h3>${eco:translate('Activity stream: There is no any activity yet')}</h3>
    </c:if>
</div>
