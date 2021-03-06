<%@ tag import="com.bordozer.jphoto.utils.UserUtils" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="photo" required="true" type="com.bordozer.jphoto.core.general.photo.Photo" %>

<c:set var="isLoggedUser" value="<%=UserUtils.isCurrentUserLoggedUser()%>"/>

<c:if test="${isLoggedUser}">
    <jsp:doBody/>
</c:if>
