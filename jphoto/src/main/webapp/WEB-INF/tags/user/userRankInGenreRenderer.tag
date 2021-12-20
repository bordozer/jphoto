<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="userRankIconContainer" required="true" type="com.bordozer.jphoto.ui.userRankIcons.UserRankIconContainer" %>

<c:forEach var="rankIcon" items="${userRankIconContainer.rankIcons}">
    <html:img8 src="${rankIcon.icon}" alt="${rankIcon.title}"/>
</c:forEach>
