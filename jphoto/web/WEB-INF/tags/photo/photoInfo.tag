<%@ tag import="java.io.File" %>
<%@ tag import="core.services.utils.ImageFileUtilsServiceImpl" %>
<%@ tag import="core.general.img.Dimension" %>
<%@ tag import="core.services.utils.ImageFileUtilsService" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="photoInfo" required="true" type="core.general.photo.PhotoInfo" %>
<%@ attribute name="votingModel" required="true" type="controllers.users.genreRank.VotingModel" %>
<%@ attribute name="hideAuthorIconsAndMenu" required="true" type="java.lang.Boolean" %>

<%
	final ImageFileUtilsService imageFileUtilsService = ApplicationContextHelper.getImageFileUtilsService();

	final File picture = photoInfo.getPhoto().getFile();
	final Dimension originalDimension = imageFileUtilsService.getImageDimension( picture );
	final Dimension resizedDimension = imageFileUtilsService.resizePhotoImage( originalDimension );
%>

<c:set var="photo" value="${photoInfo.photo}" />
<c:set var="user" value="${photoInfo.user}" />
<c:set var="genre" value="${photoInfo.genre}" />
<c:set var="photoTeam" value="${photoInfo.photoTeam}" />
<c:set var="photoTeamMembers" value="${photoTeam.photoTeamMembers}" />
<c:set var="userPhotoAlbums" value="${photoInfo.userPhotoAlbums}" />
<c:set var="isPhotoAuthorNameMustBeHidden" value="${photoInfo.photoAuthorNameMustBeHidden}" />

<c:set var="originalDimension" value="<%=originalDimension%>" />
<c:set var="resizedDimension" value="<%=resizedDimension%>" />

<div id="photoInfoDiv">
	<div class="votingDiv block-background block-border">

		<js:genreRankVotingJS />

		<table:table border="0" width="99%>">

			<table:separatorInfo colspan="2" title="${eco:translate('Photo detailes')}" />

			<table:tr>
				<table:td width="170">${eco:translate("Name")}</table:td>
				<table:td>${eco:escapeHtml(photo.name)}</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Author")}</table:td>
				<table:td>
					<c:if test="${not isPhotoAuthorNameMustBeHidden}">
						<user:userCard user="${user}"/>
						<c:if test="${not hideAuthorIconsAndMenu}">
							<br />
							<icons:userIcons user="${user}"
											 hideIconSendPrivateMessage="true"
											 hideIconToBlackList="true"
									/>
							<tags:entryMenu entryMenu="${photoInfo.photoAuthorMenu}" />
						</c:if>
					</c:if>

					<c:if test="${isPhotoAuthorNameMustBeHidden}">
						<span title="${eco:translate('The photo is posted anonymously')}">${photoInfo.photoAuthorAnonymousName}</span>
					</c:if>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td width="90">${eco:translate("Upload time")}</table:td>
				<table:td>
					<links:photosOnDate uploadTime="${photo.uploadTime}" />
					${eco:formatTimeShort(photo.uploadTime)}
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Genre")}</table:td>
				<table:td><links:genrePhotos genre="${photoInfo.genre}"/></table:td>
			</table:tr>

			<table:separator colspan="2" />

			<table:tr>
				<table:td>${eco:translate("Original dimension")}</table:td>
				<table:td>${originalDimension.width} x ${originalDimension.height} ${eco:translate("px")}</table:td>
			</table:tr>

			<c:if test="${resizedDimension.width != originalDimension.width || resizedDimension.height != originalDimension.height}">
				<table:tr>
					<table:td>${eco:translate("Shown dimension")}</table:td>
					<table:td>${resizedDimension.width} x ${resizedDimension.height} ${eco:translate("px")}</table:td>
				</table:tr>
			</c:if>

			<table:tr>
				<table:td>${eco:translate("File size")}</table:td>
				<table:td>${eco:fileSizeToKb(photo.fileSize)} ${eco:translate('Kb')}</table:td>
			</table:tr>

			<c:if test="${not isPhotoAuthorNameMustBeHidden && not empty photoTeamMembers}">
				<table:separator colspan="2" />

				<table:tr>
					<table:td>${eco:translate("Photo team")}</table:td>
					<table:td>
						<c:forEach var="photoTeamMember" items="${photoTeamMembers}">
							<icons:teamMemberType teamMemberType="${photoTeamMember.userTeamMember.teamMemberType}" />
							<links:userTeamMemberCard userTeamMember="${photoTeamMember.userTeamMember}" />
							<br />
						</c:forEach>
					</table:td>
				</table:tr>
			</c:if>

			<c:if test="${not isPhotoAuthorNameMustBeHidden && not empty userPhotoAlbums}">
				<table:separator colspan="2" />

				<table:tr>
					<table:td>${eco:translate("Photo albums")}</table:td>
					<table:td>
						<c:forEach var="userPhotoAlbum" items="${userPhotoAlbums}">
							<links:userPhotoAlbumPhotos userPhotoAlbum="${userPhotoAlbum}" />
							<br />
						</c:forEach>
					</table:td>
				</table:tr>
			</c:if>

			<table:separator colspan="2" />

			<table:tr>
				<table:td colspan="2">
					${eco:translate("Author\'s rank in category ")}

					<c:if test="${not isPhotoAuthorNameMustBeHidden}">
						${eco:photosByUserByGenreLink(user, genre)}
					</c:if>

					<c:if test="${isPhotoAuthorNameMustBeHidden}">
						<links:genrePhotos genre="${genre}" />
					</c:if>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("When photo was uploaded")}</table:td>
				<table:td>
					<user:userRankInGenre user="${photoInfo.user}" genre="${photoInfo.genre}" rank="${photo.userGenreRank}"/>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Current")}</table:td>
				<table:td>
					<user:userRankInGenreCurrent user="${photoInfo.user}" genre="${photoInfo.genre}" rank="${photoInfo.photoAuthorRankInGenre}" userHasEnoughPhotos="${photoInfo.photoAuthorHasEnoughPhotosInGenre}"/>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Vote for the rank")}</table:td>
				<table:td>
					<user:voteForUserRankInGenreByPhoto photo="${photo}" genre="${genre}" votingModel="${votingModel}" />
				</table:td>
			</table:tr>

			<table:separator colspan="2" />

			<table:tr>
				<table:td>${eco:translate("Total marks")}</table:td>
				<table:td>
					<links:photoMarkList photo="${photo}">${photoInfo.totalMarks}</links:photoMarkList>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Marks by categories")}</table:td>
				<table:td>
					<c:forEach var="marksByCategoryInfo" items="${photoInfo.marksByCategoryInfos}" >
						${marksByCategoryInfo.photoVotingCategory.name}:
						<span title="${eco:translate('Number of users who voted for the photo in this category')}">${marksByCategoryInfo.quantity}</span>
						/
						<span title="${eco:translate('Summary photo\'s mark in this category')}">${marksByCategoryInfo.sumMark}</span>
						<br />
					</c:forEach>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Today\'s rating position")}</table:td>
				<table:td>${photoInfo.photoRatingPosition > 0 ? photoInfo.photoRatingPosition : '-'}</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Photo\'s awards")}</table:td>
				<table:td>
					<c:forEach var="photoAward" items="${photoInfo.photoAwards}" varStatus="status">

						${photoAward.awardKey.nameTranslated}

						<c:if test="${not status.end}">
							<br />
						</c:if>
					</c:forEach>
				</table:td>
			</table:tr>

			<table:separator colspan="2" />

			<table:tr>
				<table:td>${eco:translate("Previews")}</table:td>
				<table:td>
					<links:photoPreviewsList photoInfo="${photoInfo}"/>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td>${eco:translate("Comments")}</table:td>
				<table:td>
					${photoInfo.commentsCount}
				</table:td>
			</table:tr>

			<table:tr>
				<table:td></table:td>
				<table:td cssClass="textsentered"><a href="${eco:baseUrlWithPrefix()}/photo/${photo.id}/activity/">${eco:translate("Photo activity stream")}</a></table:td>
			</table:tr>

		</table:table>
	</div>
</div>