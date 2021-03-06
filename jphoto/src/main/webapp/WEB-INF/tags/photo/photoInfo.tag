<%@ tag import="com.bordozer.jphoto.core.general.img.Dimension" %>
<%@ tag import="com.bordozer.jphoto.ui.context.ApplicationContextHelper" %>
<%@ tag import="com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType" %>
<%@ tag import="com.bordozer.jphoto.core.services.utils.ImageFileUtilsService" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="photoInfo" required="true" type="com.bordozer.jphoto.core.general.photo.PhotoInfo" %>
<%@ attribute name="votingModel" required="true" type="com.bordozer.jphoto.ui.controllers.users.genreRank.VotingModel" %>

<%
    final ImageFileUtilsService imageFileUtilsService = ApplicationContextHelper.getImageFileUtilsService();

    final Dimension originalDimension = photoInfo.getPhoto().getImageDimension();
    final Dimension shownDimension = imageFileUtilsService.resizePhotoImage(originalDimension);
%>

<c:set var="photo" value="${photoInfo.photo}"/>
<c:set var="user" value="${photoInfo.user}"/>
<c:set var="genre" value="${photoInfo.genre}"/>
<c:set var="photoTeam" value="${photoInfo.photoTeam}"/>
<c:set var="photoTeamMembers" value="${photoTeam.photoTeamMembers}"/>
<c:set var="userPhotoAlbums" value="${photoInfo.userPhotoAlbums}"/>
<c:set var="isPhotoAuthorNameMustBeHidden" value="${photoInfo.photoAuthorNameMustBeHidden}"/>

<c:set var="originalDimension" value="<%=originalDimension%>"/>
<c:set var="resizedDimension" value="<%=shownDimension%>"/>

<%--<div id="photoInfoDiv">--%>

<%--<div class="votingDiv">--%>

<js:genreRankVotingJS/>

<div class="panel panel-default">

    <div class="panel-heading">
        <h3 class="panel-title text-center">${eco:translate('Photo info: Photo details')}</h3>
    </div>

    <div class="panel-body small">
        <table:table>

            <table:tr>
                <table:td width="190">${eco:translate("Photo info: Photo name")}</table:td>
                <table:td>${eco:escapeHtml(photo.name)}</table:td>
            </table:tr>

            <table:tr>
                <table:td>${eco:translate("Photo info: Uploaded by")}</table:td>
                <table:td>
                    <c:if test="${not isPhotoAuthorNameMustBeHidden}">
                        <user:userCard user="${user}"/>
                        <icons:userIcons user="${user}"
                                         hideIconSendPrivateMessage="true"
                        />
                        <tags:contextMenu entryId="${user.id}" entryMenuType="<%=EntryMenuType.USER%>"/>
                    </c:if>

                    <c:if test="${isPhotoAuthorNameMustBeHidden}">
                        <span title="${eco:translate('Photo info: The photo is posted anonymously')}">${photoInfo.photoAuthorAnonymousName}</span>
                    </c:if>
                </table:td>
            </table:tr>

            <table:tr>
                <table:td width="90">${eco:translate("Photo info: Upload time")}</table:td>
                <table:td>
                    <links:photosOnDate uploadTime="${photo.uploadTime}"/>
                    ${eco:formatTimeShort(photo.uploadTime)}
                </table:td>
            </table:tr>

            <table:tr>
                <table:td>${eco:translate("Photo info: Photo category")}</table:td>
                <table:td><links:genrePhotos genre="${photoInfo.genre}"/></table:td>
            </table:tr>

            <c:if test="${not empty photo.photoImportData}">
                <table:separator colspan="2"/>
                <table:tr>
                    <table:td colspan="2">
                        ${eco:translate("Photo info: The photo is taken from remote source")} ${photoInfo.remoteSourceLink}
                        ${eco:translate("Photo info: The photo remote user link")} ${photoInfo.remoteUserLink}
                        <br/>
                        ${eco:translate("Photo info: The photo remote photo link")}: '${photoInfo.remotePhotoLink}'
                    </table:td>
                </table:tr>
            </c:if>

            <table:separator colspan="2"/>

            <table:tr>
                <table:td>${eco:translate("Photo info: Original size")}</table:td>
                <table:td>${originalDimension.width} x ${originalDimension.height} ${eco:translate("px")}</table:td>
            </table:tr>

            <c:if test="${resizedDimension.width != originalDimension.width || resizedDimension.height != originalDimension.height}">
                <table:tr>
                    <table:td>${eco:translate("Photo info: Shown size")}</table:td>
                    <table:td>${resizedDimension.width} x ${resizedDimension.height} ${eco:translate("px")}</table:td>
                </table:tr>
            </c:if>

            <table:tr>
                <table:td>${eco:translate("Photo info: File size")}</table:td>
                <table:td>${eco:fileSizeToKb(photo.fileSize)} ${eco:translate('Kb')}</table:td>
            </table:tr>

            <c:if test="${not isPhotoAuthorNameMustBeHidden && not empty photoTeamMembers}">
                <table:separator colspan="2"/>

                <table:tr>
                    <table:td>${eco:translate("Photo info: Photo team")}</table:td>
                    <table:td>
                        <c:forEach var="photoTeamMember" items="${photoTeamMembers}">
                            <icons:teamMemberType teamMemberType="${photoTeamMember.userTeamMember.teamMemberType}"/>
                            <links:userTeamMemberCard userTeamMember="${photoTeamMember.userTeamMember}"/>
                            <br/>
                        </c:forEach>
                    </table:td>
                </table:tr>
            </c:if>

            <c:if test="${not isPhotoAuthorNameMustBeHidden && not empty userPhotoAlbums}">
                <table:separator colspan="2"/>

                <table:tr>
                    <table:td>${eco:translate("Photo info: Photo albums")}</table:td>
                    <table:td>
                        <c:forEach var="userPhotoAlbum" items="${userPhotoAlbums}">
                            <links:userPhotoAlbumPhotos userPhotoAlbum="${userPhotoAlbum}"/>
                            <br/>
                        </c:forEach>
                    </table:td>
                </table:tr>
            </c:if>

            <table:separator colspan="2"/>

            <table:tr>
                <table:td colspan="2">
                    ${eco:translate("Photo info: Author\'s rank in category")}

                    <c:if test="${not isPhotoAuthorNameMustBeHidden}">
                        ${eco:photosByUserByGenreLink(user, genre)}
                    </c:if>

                    <c:if test="${isPhotoAuthorNameMustBeHidden}">
                        <links:genrePhotos genre="${genre}"/>
                    </c:if>
                </table:td>
            </table:tr>

            <table:tr>
                <table:td>
                    <li>${eco:translate("Photo info: When photo was uploaded")}</li>
                </table:td>
                <table:td>
                    <user:userRankInGenreRenderer userRankIconContainer="${photoInfo.userRankWhenPhotoWasUploadedIconContainer}"/>
                </table:td>
            </table:tr>

            <table:tr>
                <table:td>
                    <li>${eco:translate("Photo info: Current rank")}</li>
                </table:td>
                <table:td>
                    <user:userRankInGenreRenderer userRankIconContainer="${photoInfo.userRankIconContainer}"/>
                </table:td>
            </table:tr>

            <table:tr>
                <table:td>${eco:translate("Photo info: Vote for the rank")}</table:td>
                <table:td>
                    <user:userRankInGenreVotingArea_ByPhoto photo="${photo}" genre="${genre}" votingModel="${votingModel}"/>
                </table:td>
            </table:tr>

            <table:separator colspan="2"/>

            <table:tr>
                <table:td>${eco:translate("Photo info: Total marks")}</table:td>
                <table:td>
                    <links:photoMarkList photo="${photo}">${photoInfo.totalMarks}</links:photoMarkList>
                </table:td>
            </table:tr>

            <table:tr>
                <table:td>${eco:translate("Photo info: Marks by categories")}</table:td>
                <table:td>
                    <c:forEach var="marksByCategoryInfo" items="${photoInfo.marksByCategoryInfos}">
                        <strong>${eco:translateVotingCategory(marksByCategoryInfo.photoVotingCategory.id)}</strong>:
                        <span title="${eco:translate('Photo info: Summary photo\'s mark in this category')}">${eco:translate('PLURAL marks')}: ${marksByCategoryInfo.sumMark},</span>
                        <span title="${eco:translate('Photo info: Number of users who voted for the photo in this category')}">${eco:translate('PLURAL voices')}: ${marksByCategoryInfo.quantity}</span>
                        <br/>
                    </c:forEach>
                </table:td>
            </table:tr>

            <table:tr>
                <table:td>${eco:translate("Photo info: Today\'s rating position")}</table:td>
                <table:td>${photoInfo.photoRatingPosition > 0 ? photoInfo.photoRatingPosition : '-'}</table:td>
            </table:tr>

            <table:tr>
                <table:td>${eco:translate("Photo info: Photo\'s awards")}</table:td>
                <table:td>
                    <c:forEach var="photoAward" items="${photoInfo.photoAwards}" varStatus="status">

                        ${eco:translate(photoAward.awardKey.name)}

                        <c:if test="${not status.end}">
                            <br/>
                        </c:if>
                    </c:forEach>
                </table:td>
            </table:tr>

            <table:separator colspan="2"/>

            <table:tr>
                <table:td>${eco:translate("Photo info: Previews count")}</table:td>
                <table:td>
                    <links:photoPreviewsList photoInfo="${photoInfo}"/>
                </table:td>
            </table:tr>

            <table:tr>
                <table:td>${eco:translate("Photo info: Comments count")}</table:td>
                <table:td>
                    ${photoInfo.commentsCount}
                </table:td>
            </table:tr>

            <table:tr>
                <table:td/>
                <table:td cssClass="textsentered"><a
                        href="${eco:baseUrl()}/photo/${photo.id}/activity/">${eco:translate("Photo info: Photo activity stream")}</a></table:td>
            </table:tr>

        </table:table>
    </div>
</div>
<%--</div>--%>
