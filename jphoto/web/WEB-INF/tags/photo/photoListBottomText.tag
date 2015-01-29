<%@ tag import="core.services.utils.sql.PhotoQueryServiceImpl" %>
<%@ tag import="core.services.utils.DateUtilsServiceImpl" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="bottomText" required="true" type="java.lang.String" %>
<%@ attribute name="photosCriteriasDescription" required="true" type="java.lang.String" %>

<div>
	${photosCriteriasDescription}
</div>

<div>
	<c:if test="${not empty bottomText}">
		${bottomText}
	</c:if>
</div>