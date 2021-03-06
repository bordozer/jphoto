<%@ tag import="com.bordozer.jphoto.ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="favoriteEntry" required="true" type="com.bordozer.jphoto.core.interfaces.Favoritable" %>
<%@ attribute name="entryType" required="true" type="com.bordozer.jphoto.core.enums.FavoriteEntryType" %>
<%@ attribute name="iconIndex" required="true" type="java.lang.Integer" %>
<%@ attribute name="iconSize" required="true" type="java.lang.Integer" %>

<c:set var="userId" value="<%=EnvironmentContext.getCurrentUserId()%>"/>

<c:set var="cssClassEntry" value="bookmark-icon-div-${userId}-${favoriteEntry.id}-${entryType.id}"/>
<c:set var="cssClassEntryContainerId" value="bookmark-icon-div-${userId}-${favoriteEntry.id}-${entryType.id}-${iconIndex}"/>
<div class="${cssClassEntry} ${cssClassEntryContainerId}" style="display: inline-block;"></div>

<script type="text/javascript">
    require(['jquery'], function ($) {
        renderEntryIcon(${userId}, ${favoriteEntry.id}, ${entryType.id}, ${iconSize}, $('.${cssClassEntryContainerId}'));
    });
</script>
