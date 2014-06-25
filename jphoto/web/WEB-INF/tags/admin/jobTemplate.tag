<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="savedJobType" type="admin.jobs.enums.SavedJobType" required="true" %>

<c:set var="savedJobTypeName" value="${eco:translate(savedJobType.name)}"/>

<a href="${eco:baseAdminUrl()}/jobs/${savedJobType.prefix}/" title="${eco:translate1('Job template: $1 link title', savedJobTypeName)}">${savedJobTypeName}</a>

