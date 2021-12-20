<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="activityTypeValues" required="true" type="java.util.List" %>
<%@ attribute name="filterActivityTypeId" required="true" type="java.lang.Integer" %>
<%@ attribute name="url" required="true" type="java.lang.String" %>

<ul class="nav nav-tabs">

    <li class="${filterActivityTypeId == 0 ? 'active' : ''}">
        <a href="${url}">
            <html:img32 src="jobtype/clearFilter.png" alt="${eco:translate('Reset activity filter')}"/>
        </a>
    </li>

    <c:forEach var="activityType" items="${activityTypeValues}">

        <li class="${filterActivityTypeId == activityType.id ? 'active' : ''}">
            <a href="${url}type/${activityType.id}/">
                <html:img32 src="jobtype/${activityType.icon}" alt="${eco:translate(activityType.name)}"/>
            </a>
        </li>

    </c:forEach>

</ul>
