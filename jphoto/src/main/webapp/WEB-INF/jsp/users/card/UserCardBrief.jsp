<%@ page import="com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user/card" %>
<%@ taglib prefix="userCardTabs" tagdir="/WEB-INF/tags/user/card/tabs" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>

<jsp:useBean id="userCardModel" type="com.bordozer.jphoto.ui.controllers.users.card.UserCardModel" scope="request"/>

<c:set var="user" value="${userCardModel.user}"/>
<c:set var="editingUserDataIsAccessible" value="${userCardModel.editingUserDataIsAccessible}"/>

<tags:page pageModel="${userCardModel.pageModel}">

    <tags:contextMenuJs/>

    <div class="panel">

        <div class="panel-body">

            <div class="row row-bottom-padding-10">
                <user:userCardTab
                        user="${user}"
                        userCardTabDTOs="${userCardModel.userCardTabDTOs}"
                        selectedTab="${userCardModel.selectedUserCardTab}"
                        albums="${userCardModel.userPhotosCountByAlbums}"
                />
            </div>

            <div class="row row-bottom-padding-10">

                <div class="col-lg-4">
                    <div class="panel panel-default">

                        <div class="panel-heading">
                            <h3 class="panel-title text-center">
                                    ${eco:escapeHtml(user.name)}
                            </h3>
                        </div>

                        <div class="panel-body text-center">
                            <user:userCardAvatar user="${user}" userAvatar="${userCardModel.userAvatar}" isEditable="${editingUserDataIsAccessible}"/>
                        </div>

                        <div class="panel-footer text-center">
                            <icons:userIcons user="${user}" hideIconSendPrivateMessage="true" iconSize="32"/>
                            <tags:contextMenu entryId="${user.id}" entryMenuType="<%=EntryMenuType.USER%>"/>
                        </div>

                    </div>

                </div>

                <div class="col-lg-4">
                    <user:userPhotosByGenresList user="${user}" userCardGenreInfoMap="${userCardModel.userCardGenreInfoMap}"/>
                </div>

            </div>

            <div class="row">
                <photo:photoListsRender photoLists="${userCardModel.photoLists}"/>
            </div>

        </div>

    </div>

    <tags:devMode>
        <input type="hidden" id="dev_mode_user_card" value="${user.id}">
    </tags:devMode>

</tags:page>
