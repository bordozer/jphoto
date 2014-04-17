<%@ page import="ui.elements.PageTitleData" %>
<%@ page import="ui.elements.PageModel" %>
<%@ page import="ui.controllers.portalpage.PortalPageModel" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ page import="ui.services.breadcrumbs.items.BreadcrumbsBuilder" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<jsp:useBean id="portalPageModel" type="ui.controllers.portalpage.PortalPageModel" scope="request" />

<%
	final String title = portalPageModel.getTranslatorService().translate( BreadcrumbsBuilder.BREADCRUMBS_PORTAL_PAGE, EnvironmentContext.getLanguage() );
	final PageModel pageModel = new PageModel();
	pageModel.setPageTitleData( new PageTitleData( ApplicationContextHelper.getSystemVarsService().getProjectName(), title, title ) );
%>

<c:set var="pageModel" value="<%=pageModel%>" />
<c:set var="randomBestPhoto" value="${portalPageModel.randomBestPhotoArrayIndex}" />
<c:set var="lastUploadedPhotoList" value="${portalPageModel.lastUploadedPhotoList}" />
<c:set var="theBestPhotoList" value="${portalPageModel.theBestPhotoList}" />
<c:set var="lastActivities" value="${portalPageModel.lastActivities}" />

<tags:page pageModel="${pageModel}">

	<style type="text/css">

		#makeMeScrollable
		{
			width:100%;
			height: 100px;
			position: relative;
			/*border: dotted;*/
		}

		#makeMeScrollable div.scrollableArea *
		{
			position: relative;
			float: left;
			margin: 0;
			padding: 0;
			-webkit-user-select: none;
			-khtml-user-select: none;
			-moz-user-select: none;
			-o-user-select: none;
			user-select: none;
		}

		.portalPageLastPhotos {
			text-align: center;
			float: left;
			width: 100%;
			/*border: dotted;*/
		}

		.portalPageBestPhotos {
			text-align: center;
			float: left;
			width: 510px;
			margin: 10px;
			/*border: dotted;*/
		}

	</style>

	<link rel="Stylesheet" type="text/css" href="${eco:baseUrl()}/js/lib/smoothdiv/css/smoothDivScroll.css" />
	<script type="text/javascript" src="${eco:baseUrl()}/js/lib/smoothdiv/jquery.mousewheel.min.js"></script>
	<script type="text/javascript" src="${eco:baseUrl()}/js/lib/smoothdiv/jquery.smoothDivScroll-1.2.js"></script>

	<h3>${eco:translate('Last uploaded photos')}</h3>

	<div id="portalPageLastPhotos" class="portalPageLastPhotos">
		<div id="makeMeScrollable">
			<c:forEach var="photoInfo" items="${lastUploadedPhotoList.photoInfos}">
				<c:set var="photo" value="${photoInfo.photo}" />

				<c:set var="previewImgUrl" value="${photoInfo.photoPreviewImgUrl}" />
				<c:if test="${photoInfo.photoPreviewHasToBeHiddenBecauseOfNudeContent}">
					<c:set var="previewImgUrl" value="${eco:imageFolderURL()}/nude_content.jpg" />
				</c:if>

				<links:photoCard id="${photo.id}">
					<img src="${previewImgUrl}" id="${photo.id}" alt="${photo.name}" height="100" class="lastphotos"/>
					&nbsp;&nbsp;&nbsp;&nbsp;
				</links:photoCard>
			</c:forEach>
		</div>
	</div>

	<script type="text/javascript">
		$( document ).ready( function() {
			$( "#makeMeScrollable" ).smoothDivScroll( {
														  mousewheelScrolling: true,
														  manualContinuousScrolling: true,
														  visibleHotSpotBackgrounds: "always",
														  autoScrollingMode: "onstart"
													  } );
		} );
	</script>

	<table:table border="0" width="100%">

		<table:tr>

			<table:td valign="top" width="550">

				<c:set var="isTheBestPhotosPresent" value="${not empty theBestPhotoList.photoInfos}" />

				<div class="floatleft" style="text-align: center;">

					<c:if test="${isTheBestPhotosPresent}">

						<h3>${eco:translate('The best photos')}</h3>

						<script type="text/javascript" src="${eco:baseUrl()}/js/lib/galleryview/jquery.timers-1.2.js" ></script>
						<script type="text/javascript" src="${eco:baseUrl()}/js/lib/jquery.easing.1.3.js" ></script>
						<script type="text/javascript" src="${eco:baseUrl()}/js/lib/galleryview/jquery.galleryview-3.0-dev.js" ></script>
						<link type="text/css" rel="stylesheet" href="${eco:baseUrl()}/js/lib/galleryview/jquery.galleryview-3.0-dev.css" />

						<script type="text/javascript">
							$( function () {
								$( '#myGallery' ).galleryView( {
																   enable_overlays:true,
																   frame_height:70,
																   frame_width:50,
																   panel_width:500,
																   panel_height:300,
																   panel_scale:'fit',
																   frame_opacity:0.9,
																   start_frame: ${randomBestPhoto},
																   autoplay: true
															   } );
							} );
						</script>

						<div class="portalPageBestPhotos">
							<ul id="myGallery">
								<c:forEach var="photoInfo" items="${theBestPhotoList.photoInfos}">
									<c:set var="photo" value="${photoInfo.photo}" />

									<c:set var="imgUrl" value="${photoInfo.photoImgUrl}" />
									<c:if test="${photoInfo.photoPreviewHasToBeHiddenBecauseOfNudeContent}">
										<c:set var="imgUrl" value="${eco:imageFolderURL()}/nude_content.jpg" />
									</c:if>

									<li><img src="${imgUrl}" id="photo_${photo.id}" alt="${photo.name}" class="bestphotos"/></li>
								</c:forEach>
							</ul>
						</div>

						<script type="text/javascript">
							jQuery().ready( function() {
								$( 'img.bestphotos' ).each( function() {
									this.bind( "click", function() {
										alert( 1 );
									})
								});
							});
						</script>
					</c:if>

					<c:if test="${not isTheBestPhotosPresent}">
						<div style="float: left; width: 300px; border: solid 1px #999999; height: 300px; margin-left: 37px; margin-top: 15px;">
							<br />
							<br />
							<h3>${eco:translate('The best photos are not defined')}</h3>
							<br />
							<html:img id="bestphoto" src="icons128/bestphoto.png" width="128" height="128"/>
						</div>
					</c:if>

				</div>

				<div class="floatleft" style="height: 30px; text-align: center; padding-top: 10px;">
					${eco:translate2('The photos that have got $1 marks for last $2 days', portalPageModel.bestPhotosMinMarks, portalPageModel.bestPhotosPeriod) }
				</div>

			</table:td>

			<c:set var="ratingWirth" value="20%"/>

			<table:td valign="top" width="${ratingWirth}">

				<table:table width="100%">
					<table:separatorInfo colspan="3" title="${eco:translate('Photos')}"/>
					<c:forEach var="portalPageGenre" items="${portalPageModel.portalPageGenres}">
						<table:tr>
							<table:tdunderlined><links:genrePhotos genre="${portalPageGenre.genre}" /></table:tdunderlined>
							<table:tdunderlined cssClass="textright">${portalPageGenre.total}</table:tdunderlined>
							<table:tdunderlined cssClass="textright">
								<c:if test="${portalPageGenre.today > 0}">
									+${portalPageGenre.today}&nbsp;&nbsp;
								</c:if>
							</table:tdunderlined>
						</table:tr>
					</c:forEach>
				</table:table>

			</table:td>

			<c:set var="topBestUsersQty" value="<%=PortalPageModel.TOP_BEST_USERS_QTY%>"/>

			<table:td width="${ratingWirth}" valign="top">
				<user:userRating userRatings="${portalPageModel.bestWeekUserRating}" listTitle="${eco:translate('Top authors of the week')}" />
			</table:td>

			<table:td width="${ratingWirth}" valign="top">
				<user:userRating userRatings="${portalPageModel.bestMonthUserRating}" listTitle="${eco:translate('Top authors of the month')}" />
			</table:td>

		</table:tr>

		<table:separatorInfo colspan="1" title="${eco:translate('Activity stream')}" />

		<table:tr>

			<table:td>

				<tags:activityStream activities="${lastActivities}" showUserActivityLink="true" hideTextForAdmin="true" />

				<br />

				<a href="${eco:baseUrl()}/activityStream/">${eco:translate('Full activity stream')}</a>

			</table:td>

			<table:td />

			<table:td />

			<table:td />

		</table:tr>

	</table:table>

	<div class="footerseparator"></div>

</tags:page>
