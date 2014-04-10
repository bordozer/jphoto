<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="userCard" tagdir="/WEB-INF/tags/user/card" %>

<%@ attribute name="userCardModel" required="true" type="ui.controllers.users.card.UserCardModel" %>

<c:set var="user" value="${userCardModel.user}"/>

<c:set var="userStatistic" value="${userCardModel.userStatistic}"/>
<c:set var="marksByCategoryInfos" value="${userCardModel.marksByCategoryInfos}"/>

<c:set var="userPhotoAlbums" value="${userCardModel.userPhotoAlbums}"/>
<c:set var="userPhotoAlbumsQtyMap" value="${userCardModel.userPhotoAlbumsQtyMap}"/>

<c:set var="userTeam" value="${userCardModel.userTeam}"/>
<c:set var="teamMemberPhotosQtyMap" value="${userCardModel.teamMemberPhotosQtyMap}"/>

<style type="text/css">
	.vlaign {
		vertical-align: top;
	}
</style>

<c:set var="width" value="20%"/>

<table:table width="95%">

	<table:tr>

		<table:td width="${width}" cssClass="vlaign">
			<userCard:userStatistics user="${user}" userStatistic="${userStatistic}"/>
		</table:td>

		<table:td width="${width}" cssClass="vlaign">
			<userCard:userMarks user="${user}" marksByCategoryInfos="${marksByCategoryInfos}"/>
		</table:td>

		<table:td width="${width}" cssClass="vlaign">
			<userCard:userTeamShort userTeam="${userTeam}" teamMemberPhotosQtyMap="${teamMemberPhotosQtyMap}" />
		</table:td>

		<table:td width="${width}" cssClass="vlaign">
			<userCard:userPhotoAlbumShort userPhotoAlbums="${userPhotoAlbums}" userPhotoAlbumsQtyMap="${userPhotoAlbumsQtyMap}" />
		</table:td>

	</table:tr>

</table:table>