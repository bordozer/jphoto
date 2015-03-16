<%@ page import="admin.controllers.jobs.edit.photosImport.PhotosImportModel" %>
<%@ page import="admin.controllers.jobs.edit.photosImport.PhotosImportSource" %>
<%@ page import="core.general.photo.PhotoImageLocationType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="js" tagdir="/WEB-INF/tags/js" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photosImportModel" type="admin.controllers.jobs.edit.photosImport.PhotosImportModel" scope="request"/>

<c:set var="importSourceIdControl" value="<%=PhotosImportModel.FORM_CONTROL_PHOTOS_IMPORT_SOURCE_ID%>"/>

<c:set var="pictureDirFormControl" value="<%=PhotosImportModel.PICTURE_DIR_FORM_CONTROL%>"/>
<c:set var="userQtyLimitControl" value="<%=PhotosImportModel.PHOTO_QTY_LIMIT_FORM_CONTROL%>"/>
<c:set var="deletePictureAfterImportControl" value="<%=PhotosImportModel.DELETE_PICTURE_AFTER_IMPORT_CONTROL%>"/>
<c:set var="userIdControl" value="<%=PhotosImportModel.USER_ID_FORM_CONTROL%>"/>

<c:set var="remotePhotoSiteUserIdsControl" value="<%=PhotosImportModel.FORM_CONTROL_PHOTOSIGHT_USER_ID%>"/>
<c:set var="userNameControl" value="<%=PhotosImportModel.FORM_CONTROL_USER_NAME%>"/>
<c:set var="userGenderIdControl" value="<%=PhotosImportModel.USER_GENDER_ID_FORM_CONTROL%>"/>
<c:set var="userMembershipIdControl" value="<%=PhotosImportModel.USER_MEMBERSHIP_ID_FORM_CONTROL%>"/>
<c:set var="pageQtyControl" value="<%=PhotosImportModel.ACTIONS_QTY_FORM_CONTROL%>"/>
<c:set var="importCommentsControl" value="<%=PhotosImportModel.IMPORT_COMMENTS_FORM_CONTROL%>"/>
<c:set var="breakImportIfAlreadyImportedPhotoFoundControl" value="<%=PhotosImportModel.BREAK_IMPORT_IF_ALREADY_IMPORTED_PHOTO_FOUND_FORM_CONTROL%>"/>
<c:set var="delayBetweenRequestControl" value="<%=PhotosImportModel.DELAY_BETWEEN_REQUEST_FORM_CONTROL%>"/>

<c:set var="filesystemImportId" value="<%=PhotosImportSource.FILE_SYSTEM.getId()%>"/>
<c:set var="photosightImportId" value="<%=PhotosImportSource.PHOTOSIGHT.getId()%>"/>
<c:set var="photo35ImportId" value="<%=PhotosImportSource.PHOTO35.getId()%>"/>
<c:set var="naturelightImportId" value="<%=PhotosImportSource.NATURELIGHT.getId()%>"/>

<c:set var="filesystemImportDivId" value="importFormDiv_${filesystemImportId}"/>
<c:set var="photosightImportDivId" value="importFormDiv_${photosightImportId}"/>
<c:set var="baseUrl" value="${eco:baseUrl()}"/>

<c:set var="photoImageImportStrategyFile" value="<%=PhotoImageLocationType.FILE%>"/>
<c:set var="photoImageImportStrategyWeb" value="<%=PhotoImageLocationType.WEB%>"/>

<tags:page pageModel="${photosImportModel.pageModel}">

<admin:jobEditData jobModel="${photosImportModel}">

<jsp:attribute name="jobForm">

	<admin:saveJobButton jobModel="${photosImportModel}"/>

	<div class="panel panel-info job-box">

		<div class="panel panel-heading">
			<h3 class='panel-title'>
					${eco:translate('Job parameters')}
			</h3>
		</div>

		<div class="panel-body">

			<div class="row">

				<div class="col-lg-5 text-right">
					${eco:translate('Photo import job JSP: Import sources')}
				</div>

				<div class="col-lg-7">
					<form:radiobuttons path="${importSourceIdControl}" items="${photosImportModel.photosImportSourceTranslatableList.entries}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />" onchange="setFormsVisibility();"/>
				</div>

			</div>

			<hr/>

			<%-- FILE SYSTEM IMPORT --%>
			<div id="${filesystemImportDivId}" class="row">

				<div class="col-lg-12">

					<table:table width="100%">

						<table:tr>
							<table:tdtext text_t="Photo import job parameter: Dir" isMandatory="true"/>
							<table:tddata>
								<html:input fieldId="${pictureDirFormControl}" fieldValue="${photosImportModel.pictureDir}" size="60"/>
							</table:tddata>
						</table:tr>

						<table:tr>
							<table:td colspan="2">
								<c:set var="userPickerId" value="${not empty photosImportModel.assignAllGeneratedPhotosToUser ? photosImportModel.assignAllGeneratedPhotosToUser.id : 0}"/>
								${eco:translate('Photo import job JSP: Assign all generated photos to member')}:
								<br />
								<div class="user-picker-container" style="float: left; width: 100%;"></div>
								<script type="text/javascript">
									require( ['components/user-picker/user-picker'], function ( userPicker ) {
										userPicker( "${userIdControl}", ${userPickerId}, callbackFunction, $( '.user-picker-container' ) );
									} );

									function callbackFunction( user ) {

									}
								</script>
							</table:td>
						</table:tr>

						<table:tr>
							<table:td colspan="2">
								<tags:dateRange dateRangeTypeId="${photosImportModel.dateRangeTypeId}"
												dateFrom="${photosImportModel.dateFrom}"
												dateTo="${photosImportModel.dateTo}"
												timePeriod="${photosImportModel.timePeriod}"
										/>
							</table:td>
						</table:tr>

						<table:tr>
							<table:tdtext text_t="Photo import job JSP: Total job steps"/>
							<table:tddata>
								<tags:inputHint inputId="${userQtyLimitControl}" hintTitle_t="Photo import job JSP: Total job steps"
												hint="Photo import job JSP: Photo qty<br />Leave empty to process all images in the folder">
											<jsp:attribute name="inputField">
												<html:input fieldId="${userQtyLimitControl}" fieldValue="${photosImportModel.photoQtyLimit}" size="3"/>
											</jsp:attribute>
								</tags:inputHint>
								<br/>
								${eco:translate('Photo import job JSP: Leave empty to process all images')}
							</table:tddata>
						</table:tr>

						<table:tr>
							<table:tdtext text_t="Photo import job JSP: Delete picture after import"/>
							<table:tddata>
								<form:checkbox path="${deletePictureAfterImportControl}" value="true"/>
							</table:tddata>
						</table:tr>
					</table:table>
				</div>
			</div>
			<%-- / FILE SYSTEM IMPORT --%>

			<%-- REMOTE PHOTO SITE IMPORT --%>
			<div id="${photosightImportDivId}" class="row">

				<div class="col-lg-12">

					<div class="row">
						<div class="col-lg-12">
							${eco:translate('Photo import job JSP: Photosight user ids')}
						</div>
					</div>

					<div class="row">
						<div class="col-lg-12">
							<form:input path="${remotePhotoSiteUserIdsControl}" size="60" onchange="renderRemoteUserInfo();"/>
						</div>
					</div>

					<div class="row">
						<div class="col-lg-12">
							${eco:translate("Photo import job JSP: Use ',' as separator")}
							<div id="photosightUserInfoDiv" class="floatleft" style="display: none; margin-top: 20px;"></div>
						</div>
					</div>

					<hr />

					<div class="row">

						<div class="col-lg-12">

							<js:checkboxMassChecker checkboxClass="remote-photo-site-category-nude" initiallyChecked="${photosImportModel.remotePhotoSiteImport_importNudeContentByDefault}"/>
							${eco:translate('Photo import job JSP: Nude categories')}

							&nbsp;&nbsp;

							<js:checkboxMassChecker checkboxClass="remote-photo-site-category-no-nude" initiallyChecked="true"/>
							${eco:translate('Photo import job JSP: No nude categories')}

							<div class="remote-site-categories-container" style="margin-top: 20px;"></div>

							<script type="text/javascript">

								function renderRemoteSiteCategories() {
									require( [ 'jquery', 'modules/admin/jobs/photosImport/remoteSiteCategories/remote-site-categories'], function ( $, func ) {
										var importSourceId = $( 'input[name=' + '${importSourceIdControl}' + ']:checked' ).val();
										func( importSourceId, $( '.remote-site-categories-container' ) );
									} );
								}
							</script>
						</div>
					</div>
				</div>
			</div>

			<hr/>

			<div class="row">
				<div class="col-lg-5 text-right">
					${eco:translate('Photo import job parameter: Import comments')}
				</div>
				<div class="col-lg-7">
					<form:checkbox path="${importCommentsControl}" itemValue="true"/>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-5 text-right">
					${eco:translate('Break current photosight user_s photos import if already imported photo found')}
				</div>
				<div class="col-lg-7">
					<form:checkbox path="${breakImportIfAlreadyImportedPhotoFoundControl}" itemValue="true"/>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-5 text-right">
					${eco:translate('Photo import job parameter: Pages to process')}
				</div>
				<div class="col-lg-7">
					<form:input path="${pageQtyControl}" size="4"/>
				</div>
			</div>

			<hr/>

			<div class="row">
				<div class="col-lg-5 text-right">
					${eco:translate('Photo import job parameter: Image import strategy')}
				</div>
				<div class="col-lg-7">
					<form:radiobutton path="photoImageImportStrategyTypeId" value="${photoImageImportStrategyFile.id}"/>
					<label for="photoImageImportStrategyTypeId1">${eco:translate(photoImageImportStrategyFile.description)}</label>
					<br/>
					<form:radiobutton path="photoImageImportStrategyTypeId" value="${photoImageImportStrategyWeb.id}"/>
					<label for="photoImageImportStrategyTypeId2">${eco:translate(photoImageImportStrategyWeb.description)}</label>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-5 text-right">
					${eco:translate('Photo import job parameter: Delay between requests')}
				</div>
				<div class="col-lg-7">
					<form:input path="${delayBetweenRequestControl}" size="4"/>
				</div>
			</div>

			<hr/>

			<div class="row">
				<div class="col-lg-5 text-right">
					${eco:translate('Photo import job parameter: Gender')}
				</div>
				<div class="col-lg-7">
					<form:radiobuttons path="${userGenderIdControl}"
												   items="${photosImportModel.userGenderTranslatableList.entries}"
												   itemValue="id"
												   itemLabel="name" delimiter="<br />"
												   htmlEscape="false"
							/>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-5 text-right">
					${eco:translate('Photo import job parameter: Membership')}
				</div>
				<div class="col-lg-7">
					<form:radiobuttons path="${userMembershipIdControl}"
												   items="${photosImportModel.userMembershipTypeTranslatableList.entries}"
												   itemValue="id"
												   itemLabel="name" delimiter="<br />"
												   htmlEscape="false"
							/>
				</div>
			</div>

		</div>
		<%-- / REMOTE PHOTO SITE IMPORT --%>

		<div class="panel-footer">

		</div>

	</div> <%-- / panel --%>

	</jsp:attribute>

</admin:jobEditData>

<script type="text/javascript">

	require( [ 'jquery' ], function ( $ ) {

		$( document ).ready( function () {
			setFormsVisibility();

			var type = $( 'input[name=' + '${importSourceIdControl}' + ']:checked' ).val();
			if ( type == ${photosightImportId} || type == ${photo35ImportId} || type == ${naturelightImportId} ) {
				renderRemoteUserInfo();
			}
		} );
	} );

	function setFormsVisibility() {
		require( [ 'jquery' ], function ( $ ) {
			var type = $( 'input[name=' + '${importSourceIdControl}' + ']:checked' ).val();

			if ( type == ${filesystemImportId} ) {
				$( '#' + '${photosightImportDivId}' ).hide();
				$( '#' + '${filesystemImportDivId}' ).show();
			}

			if ( type == ${photosightImportId} || type == ${photo35ImportId} || type == ${naturelightImportId} ) {
				$( '#' + '${filesystemImportDivId}' ).hide();
				$( '#' + '${photosightImportDivId}' ).show();
				renderRemoteSiteCategories();
				renderRemoteUserInfo();
			}
		} );
	}

	function renderRemoteUserInfo() {
		require( [ '/admin/js/photosight.js' ], function ( photosight ) {
			var usedIdsControl = $( "#${remotePhotoSiteUserIdsControl}" );
			if ( usedIdsControl.val().trim() != '' ) {
				var photosImportSourceId = $( "[name='${importSourceIdControl}']:checked" ).val();
				var data = photosight.renderRemoteUserInfo( usedIdsControl.val(), photosImportSourceId, jsonRPC );

				$( 'input[name="${userGenderIdControl}"][value="' + data.userGenderId + '"]' ).attr( 'checked', 'checked' );
				$( 'input[name="${userMembershipIdControl}"][value="' + data.userMembershipTypeId + '"]' ).attr( 'checked', 'checked' );
			}
		} );
	}
</script>

</tags:page>