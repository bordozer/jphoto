<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="core.enums.FavoriteEntryType" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="favoriteEntry" required="true" type="core.interfaces.Favoritable" %>
<%@ attribute name="entryType" required="true" type="core.enums.FavoriteEntryType" %>
<%@ attribute name="isEntryInUserFavorites" required="true" type="java.lang.Boolean" %>
<%@ attribute name="entryName" required="true" type="java.lang.String" %>

<c:set var="currentUser" value="<%=EnvironmentContext.getCurrentUser()%>"/>
<c:set var="favoriteImagesFolrer" value="<%=FavoriteEntryType.FAVORITES_IMAGE_FOLDER%>"/>
<c:set var="unique" value="<%=ApplicationContextHelper.getDateUtilsService().getCurrentTime().getTime()%>"/>
<c:set var="jsId" value="${entryType.id}_${favoriteEntry.id}_${unique}"/>
<c:set var="favoriteIconId" value="favoriteIconId_${jsId}"/>

<c:set var="imageTitle" value=""/>
<c:set var="image" value=""/>
<c:set var="jsFunction" value=""/>

<c:if test="${not isEntryInUserFavorites}">
	<c:set var="image" value="${entryType.addIcon}"/>
	<c:set var="imageTitle" value="${entryName}: ${entryType.addText}"/>
	<c:set var="jsFunction" value="addEntryToFavorites"/>
</c:if>

<c:if test="${isEntryInUserFavorites}">
	<c:set var="image" value="${entryType.removeIcon}"/>
	<c:set var="imageTitle" value="${entryName}: ${entryType.removeText}"/>
	<c:set var="jsFunction" value="removeEntryToFavorites"/>
</c:if>

<html:img id="${favoriteIconId}" src="${favoriteImagesFolrer}/${image}" width="16" height="16" alt="${imageTitle}"/>

<script type="text/javascript">

	jQuery().ready( function() {

		var favoriteEntry = registerFavoriteEntry( ${currentUser.id}, ${entryType.id}, ${favoriteEntry.id}, '${entryName}'
				, '${entryType.addIcon}', '${entryType.removeIcon}', '${entryType.nameTranslated}', '${entryType.addText}', '${entryType.removeText}', '${favoriteIconId}' );

		$( '#${favoriteIconId}' ).bind( "click", function() {
			favoriteEntryModel.${jsFunction}( favoriteEntry );
		} );
	});

</script>

