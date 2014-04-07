<%@ tag import="core.services.utils.sql.PhotoSqlHelperServiceImpl" %>
<%@ tag import="core.services.photo.PhotoServiceImpl" %>
<%@ tag import="ui.controllers.photos.groupoperations.PhotoGroupOperationModel" %>
<%@ tag import="core.context.EnvironmentContext" %>
<%@ tag import="core.services.utils.DateUtilsServiceImpl" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="ui.services.breadcrumbs.BreadcrumbsPhotoGalleryService" %>
<%@ tag import="core.services.system.ConfigurationService" %>
<%@ tag import="core.general.configuration.ConfigurationKey" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="icons" tagdir="/WEB-INF/tags/icons" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<%@ attribute name="photoList" required="true" type="elements.PhotoList" %>

<%
	final ConfigurationService configurationService = ApplicationContextHelper.getConfigurationService();
%>

<c:set var="photoListTitle" value="${photoList.photoListTitle}"/>
<c:set var="totalPhotos" value="${photoList.totalPhotos}"/>
<c:set var="photosInLine" value="${photoList.photosInLine}"/>
<c:set var="showPaging" value="${photoList.showPaging}"/>

<c:set var="loggedUser" value="<%=EnvironmentContext.getCurrentUser()%>"/>

<c:set var="anonymouslyPostedPhotoText" value="<%=configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME )%>"/>

<c:set var="photoGroupOperationMenues" value="<%=photoList.getPhotoGroupOperationMenuContainer().getGroupOperationMenus()%>" />
<c:set var="isGroupOperationEnabled" value="${not empty photoGroupOperationMenues}" />

<c:set var="groupOperationForm" value="groupOperationForm" />
<c:set var="controlSelectedPhotoIds" value="<%=PhotoGroupOperationModel.FORM_CONTROL_SELECTED_PHOTO_IDS%>"/>

<c:set var="formAction" value="" />
<c:if test="${isGroupOperationEnabled}">
	<c:set var="formAction" value="${eco:baseUrlWithPrefix()}/photos/groupOperations/" />
</c:if>

<eco:form action="${formAction}" formName="${groupOperationForm}">

	<icons:favoritesJS />

	<c:set var="photoCellWidth" value="300" />

	<div class="floatleft">

		<c:if test="${showPaging}">
			<tags:paging showSummary="false"/>
		</c:if>

		<c:set var="tableWidth" value="${photoCellWidth * photosInLine}" />
		<c:set var="tdWidth" value="${photoCellWidth}" />

		<table:table border="0" width="${tableWidth}">

			<c:set var="counter" value="0"/>

			<c:if test="${not empty photoListTitle}">
				<table:separatorInfo colspan="${photosInLine}" title="${photoListTitle}" />
			</c:if>

			<c:if test="${totalPhotos == 0}">
				<tr>
					<td colspan="${photosInLine}" class="centerAlign">
						<div class="floatleft" style="margin-top: 20px; margin-bottom: 20px; font-size: 120%;">
							${eco:translate(photoList.noPhotoText)}
						</div>
					</td>
				</tr>
			</c:if>

			<c:forEach var="photoInfo" items="${photoList.photoInfos}" varStatus="status">

				<c:if test="${counter mod photosInLine == 0}">
					<tr>
				</c:if>

				<table:td align="center" cssClass="photocell" width="${tdWidth}">
					<c:if test="${not photoInfo.photoPreviewMustBeHidden}">
						<photo:photoPreview photoInfo="${photoInfo}" isGroupOperationEnabled="${isGroupOperationEnabled}" sortColumnNumber="${photoList.sortColumnNumber}"/>
					</c:if>

					<c:if test="${photoInfo.photoPreviewMustBeHidden}">
						<div class="containerPhotoMain block-border">
							<div style="padding-top: 45px;">
								${eco:translate(anonymouslyPostedPhotoText)}
								<br />
								<html:img128 src="hidden_picture.png" alt="${eco:translate('Photo list: This photo has been posted anonymously')}" />
								<br />
								${eco:translate('Photo list: The photo will be shown in the member\'s card when anonymous time pass' )}
							</div>
						</div>
					</c:if>
				</table:td>

				<c:if test="${counter < totalPhotos && counter mod photosInLine == photosInLine - 1}">
					</tr><tr>
				</c:if>

				<c:set var="counter" value="${counter + 1}"/>

			</c:forEach>

			<c:set var="totalPhotos" value="${counter}" />
			<c:set var="fullRows" value="${eco:floor(counter div photosInLine)}" />
			<c:set var="photosAreRenderedInFullRows" value="${fullRows * photosInLine}" />
			<c:set var="leftToRenderPhotos" value="${counter - photosAreRenderedInFullRows}" />
			<c:set var="emptyCellToRender" value="${photosInLine - leftToRenderPhotos}" />

			<c:if test="${emptyCellToRender < photosInLine}">
				<c:forEach var="i" begin="1" end="${emptyCellToRender}">
					<%--<td>&nbsp;
						1:${totalPhotos} <br />
						2:${photosInLine} <br />
						3:${fullRows} (${counter div photosInLine}) <br />
						4:${photosAreRenderedInFullRows} <br />
						5:${leftToRenderPhotos} <br />
						6:${emptyCellToRender}
					</td>--%>
					<td width="${tdWidth}" align="center">&nbsp;${eco:translate('Photo list: No photo in this cell')}</td>
				</c:forEach>
			</c:if>

			</tr>

			<table:tr>
				<table:td colspan="${photosInLine}">
					<div class="floatleft">
						<c:if test="${totalPhotos > 0}">
							<photo:photoAllBestLink linkToFullList="${photoList.linkToFullList}" linkToFullListText="${eco:translate(photoList.linkToFullListText)}" />
						</c:if>

						<photo:photoListBottomText bottomText="${photoList.bottomText}" photosCriteriasDescription="${photoList.photosCriteriasDescription}" />
					</div>
				</table:td>
			</table:tr>

			<table:tr>
				<table:td colspan="${photosInLine}">
					<c:if test="${showPaging}">
						<tags:paging showSummary="true"/>
					</c:if>
				</table:td>
			</table:tr>

		</table:table>

	</div>

	<js:confirmAction />

	<c:set var="controlSelectedPhotoIds" value="<%=PhotoGroupOperationModel.FORM_CONTROL_SELECTED_PHOTO_IDS%>"/>

	<c:if test="${isGroupOperationEnabled}">

		<div class="footerseparatorverysmall"></div>

		<c:set var="controlPhotoGroupOperationId" value="<%=PhotoGroupOperationModel.FORM_CONTROL_PHOTO_GROUP_OPERATION_ID%>" />

		<div style="float: left; width: 90%; padding-left: 50px;">

			<js:checkBoxChecker namePrefix="${controlSelectedPhotoIds}" />

			<label for="${controlPhotoGroupOperationId}">${eco:translate('Photo list: Group operations with selected photos')}</label>

			<select id="${controlPhotoGroupOperationId}" name="${controlPhotoGroupOperationId}">
				<option value="" selected="selected"></option>
				<c:forEach var="photoGroupOperationMenu" items="${photoGroupOperationMenues}">
					<c:set var="photoGroupOperation" value="${photoGroupOperationMenu.photoGroupOperation}"/>
					<option value="${photoGroupOperation.id}">${eco:translate(photoGroupOperation.name)}</option>
				</c:forEach>
			</select>

			<html:submitButton id="ok" caption_t="Photo list: Do group operations" onclick="return submitForm();" />
		</div>

		<script type="text/javascript">

			function submitForm() {
				var groupOperationSelect = $( '#${controlPhotoGroupOperationId}' );
				if ( groupOperationSelect.val() == '' || groupOperationSelect.val() == -1 ) {
					showErrorMessage( '${eco:translate("Photo list: Please, select group operation first")}' );
					return false;
				}

				var controlSelectedPhotoIds = $( '#${controlSelectedPhotoIds}:checked' );
				if ( controlSelectedPhotoIds.length == 0 ) {
					showErrorMessage( '${eco:translate("Photo list: Please, select at least one photo first")}' );
					return false;
				}

				var form = $( '#${groupOperationForm}' );
				form.submit();

				return true;
			}

		</script>

		<div class="footerseparatorsmall"></div>
	</c:if>

</eco:form>