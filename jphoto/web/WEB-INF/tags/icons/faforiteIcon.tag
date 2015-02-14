<%@ tag import="ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="favoriteEntry" required="true" type="core.interfaces.Favoritable" %>
<%@ attribute name="entryType" required="true" type="core.enums.FavoriteEntryType" %>
<%@ attribute name="iconSize" required="false" type="java.lang.Integer" %>

<c:set var="userId" value="<%=EnvironmentContext.getCurrentUserId()%>" />

<c:set var="cssClass" value="bookmark-icon-div-${userId}-${favoriteEntry.id}-${entryType.id}" />
<div class="${cssClass}" style="display: inline-block;"></div>

<script type="text/javascript">
	require( [ 'jquery' ], function ( $ ) {
		renderEntryIcon( ${userId}, ${favoriteEntry.id}, ${entryType.id}, ${iconSize}, $( '.${cssClass}' ) );
	});
</script>