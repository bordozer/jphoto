<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>

<%@ attribute name="user" required="true" type="core.general.user.User" %>
<%@ attribute name="userStatistic" required="true" type="ui.controllers.users.card.UserStatistic" %>

<c:set var="cssClass" value="textright"/>
<c:set var="width" value="70%"/>

<table:table border="0" width="100%" oddEven="true">

	<table:separatorInfo colspan="2" height="50" title="${eco:translate('Statistics')}"/>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userFavoritePhotos user="${user}"/></table:td>
		<table:td>${userStatistic.favoritePhotosQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userFavoriteMembers user="${user}"/></table:td>
		<table:td>${userStatistic.favoriteMembersQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userPhotosOfFavoriteMembers user="${user}"/></table:td>
		<table:td>${userStatistic.photosOfFavoriteMembersQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userFavoriteBookmarks user="${user}"/></table:td>
		<table:td>${userStatistic.bookmarkedPhotosQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userFavoriteFriends user="${user}"/></table:td>
		<table:td>${userStatistic.friendsQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userFavoriteBlacklist user="${user}"/></table:td>
		<table:td>${userStatistic.blackListEntriesQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userNotificationPhotos user="${user}"/></table:td>
		<table:td>${userStatistic.notificationsAboutNewPhotosQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:userNotificationComment user="${user}"/></table:td>
		<table:td>${userStatistic.notificationsAboutNewCommentsQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:commentsWritten user="${user}"/></table:td>
		<table:td>${userStatistic.writtenCommentsQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:commentsReceived user="${user}"/></table:td>
		<table:td>${userStatistic.receivedCommentsQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:commentsReceivedUnread user="${user}"/></table:td>
		<table:td>${userStatistic.receivedUnreadCommentsQty}</table:td>
	</table:trinfo>

	<table:trinfo>
		<table:td cssClass="${cssClass}" width="${width}"><links:usersWhoAddedUserToFavorites user="${user}"/></table:td>
		<table:td>${userStatistic.usersQtyWhoAddedInFavoriteMembers}</table:td>
	</table:trinfo>

</table:table>