<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="userCard" tagdir="/WEB-INF/tags/user/card" %>
<%@ taglib prefix="userCardTabs" tagdir="/WEB-INF/tags/user/card/tabs" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user/card" %>

<jsp:useBean id="userCardModel" type="com.bordozer.jphoto.ui.controllers.users.card.UserCardModel" scope="request"/>

<c:set var="user" value="${userCardModel.user}"/>
<c:set var="selectedUserCardTab" value="${userCardModel.selectedUserCardTab}"/>
<c:set var="editingUserDataIsAccessible" value="${userCardModel.editingUserDataIsAccessible}"/>

<tags:page pageModel="${userCardModel.pageModel}">

    <tags:contextMenuJs/>

    <div class="panel">
        <div class="panel-body">

            <div class="row row-bottom-padding-10">
                <userCard:userCardTab user="${user}"
                                      userCardTabDTOs="${userCardModel.userCardTabDTOs}"
                                      selectedTab="${userCardModel.selectedUserCardTab}"
                                      albums="${userCardModel.userPhotoAlbums}"
                />
            </div>

            <div class="row row-bottom-padding-10">

                <c:if test="${selectedUserCardTab == 'PERSONAL_DATA'}">

                    <div class="col-lg-2">
                        <user:userCardAvatar user="${user}" userAvatar="${userCardModel.userAvatar}" isEditable="${editingUserDataIsAccessible}"/>
                    </div>

                    <div class="col-lg-4">
                        <user:userInfo user="${user}" lastUserActivityTime="${userCardModel.lastUserActivityTime}" entryMenu="${userCardModel.entryMenu}"
                                       isEditable="${editingUserDataIsAccessible}"/>
                    </div>

                    <div class="col-lg-6">
                        <div class="row">
                            <div class="row">
                                    ${eco:translate('User card: Member self description')}:
                            </div>
                            <div class="row">
                                    ${eco:formatPhotoCommentText(user.selfDescription)}
                            </div>
                        </div>
                    </div>

                </c:if>


                <c:if test="${selectedUserCardTab == 'PHOTOS_OVERVIEW'}">
                    <userCardTabs:photosOverview user="${user}" userPhotosByGenres="${userCardModel.userPhotosByGenres}"
                                                 photoLists="${userCardModel.photoLists}"/>
                </c:if>


                <c:if test="${selectedUserCardTab == 'STATISTICS'}">
                    <userCardTabs:statistics userCardModel="${userCardModel}"/>
                </c:if>


                <c:if test="${selectedUserCardTab == 'TEAM'}">
                    <userCard:customPhotoLists photoLists="${userCardModel.userTeamMemberPhotoLists}"/>
                </c:if>


                <c:if test="${selectedUserCardTab == 'ALBUMS'}">
                    <c:set var="userPhotosCountByAlbums" value="${userCardModel.userPhotosCountByAlbums}"/>
                    ${eco:translate('User photo albums')}:
                    <select id="userAlbums" onchange="scrollToAlbum();">
                        <option value="0"></option>
                        <c:forEach var="userPhotoAlbum" items="${userCardModel.userPhotoAlbums}">
                            <option value="${userPhotoAlbum.id}">${eco:escapeHtml(userPhotoAlbum.name)}</option>
                        </c:forEach>
                    </select>
                    <script type="text/javascript">
                        function scrollToAlbum() {
                            this.document.location.href = "#" + $('#userAlbums').find(":selected").val();
                        }
                    </script>

                    <userCard:customPhotoLists photoLists="${userCardModel.userPhotoAlbumsPhotoLists}"/>
                </c:if>


                <c:if test="${selectedUserCardTab == 'ACTIVITY_STREAM'}">
                    <userCard:userActivityStream user="${user}" activities="${userCardModel.userLastActivities}"
                                                 filterActivityTypeId="${userCardModel.filterActivityTypeId}"/>
                </c:if>

            </div>
        </div>
    </div>

</tags:page>
