<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="userCard" tagdir="/WEB-INF/tags/user/card" %>

<%@ attribute name="userCardModel" required="true" type="com.bordozer.jphoto.ui.controllers.users.card.UserCardModel" %>

<c:set var="user" value="${userCardModel.user}"/>

<c:set var="userStatistic" value="${userCardModel.userStatistic}"/>
<c:set var="marksByCategoryInfos" value="${userCardModel.marksByCategoryInfos}"/>

<c:set var="userPhotoAlbums" value="${userCardModel.userPhotoAlbums}"/>
<c:set var="userPhotoAlbumsQtyMap" value="${userCardModel.userPhotoAlbumsQtyMap}"/>

<c:set var="userTeam" value="${userCardModel.userTeam}"/>
<c:set var="teamMemberPhotosQtyMap" value="${userCardModel.teamMemberPhotosQtyMap}"/>

<div class="col-lg-5">
    <userCard:userStatistics user="${user}" userStatistic="${userStatistic}"/>
</div>

<div class="col-lg-4">
    <userCard:userMarks user="${user}" marksByCategoryInfos="${marksByCategoryInfos}"/>
</div>

<div class="col-lg-3">
    <userCard:userTeamShort userTeam="${userTeam}" teamMemberPhotosQtyMap="${teamMemberPhotosQtyMap}"/>

    <userCard:userPhotoAlbumShort userPhotoAlbums="${userPhotoAlbums}" userPhotoAlbumsQtyMap="${userPhotoAlbumsQtyMap}"/>
</div>
