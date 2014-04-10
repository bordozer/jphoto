<%@ tag import="core.services.utils.sql.PhotoSqlHelperServiceImpl" %>
<%@ tag import="core.services.utils.DateUtilsServiceImpl" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="bottomText" required="true" type="java.lang.String" %>
<%@ attribute name="photosCriteriasDescription" required="true" type="java.lang.String" %>

<div style="float: left;">
	<div class="small-text" style="width: 95%;height: auto;float: left; padding-left: 20px;">
		${photosCriteriasDescription}
	</div>

	<div style="width: 95%; float: left; padding-left: 20px; text-align: justify">
		<c:if test="${not empty bottomText}">
			<br/>
			${eco:formatPhotoCommentText(bottomText)}
		</c:if>
	</div>
</div>