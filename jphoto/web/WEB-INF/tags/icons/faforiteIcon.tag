<%@ tag import="ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="favoriteEntry" required="true" type="core.interfaces.Favoritable" %>
<%@ attribute name="entryType" required="true" type="core.enums.FavoriteEntryType" %>
<%@ attribute name="isEntryInUserFavorites" required="true" type="java.lang.Boolean" %>
<%@ attribute name="entryName" required="true" type="java.lang.String" %>

<c:set var="userId" value="<%=EnvironmentContext.getCurrentUserId()%>" />

<div class="bookmark-icon-div-${userId}-${favoriteEntry.id}-${entryType.id}" style="display: inline-block;"></div>

<script type="text/javascript">
	require( [ 'jquery' ], function ( $ ) {
		renderEntryIcon( ${userId}, ${favoriteEntry.id}, ${entryType.id}, ${not isEntryInUserFavorites}, $( '.bookmark-icon-div-${userId}-${favoriteEntry.id}-${entryType.id}' ) );
	});
</script>