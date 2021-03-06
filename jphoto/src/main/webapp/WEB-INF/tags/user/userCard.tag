<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="com.bordozer.jphoto.core.general.user.User" %>

<c:set var="nameEscaped" value="${eco:escapeHtml(user.name)}"/>

<links:userCard id="${user.id}" title="${eco:translate1('EntityLinkUtilsService: $1: user card link title', nameEscaped)}">
    ${nameEscaped}
</links:userCard>
