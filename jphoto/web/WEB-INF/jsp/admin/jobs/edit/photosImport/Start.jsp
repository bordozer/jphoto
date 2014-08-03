<%@ page import="admin.controllers.jobs.edit.photosImport.PhotosImportModel" %>
<%@ page import="admin.controllers.jobs.edit.photosImport.PhotosImportSource" %>
<%@ page import="core.general.photo.PhotoImageImportStrategyType" %>
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
<c:set var="remotePhotoSiteCategoriesControl" value="<%=PhotosImportModel.PHOTOSIGHT_CATEGORIES_FORM_CONTROL%>"/>
<c:set var="delayBetweenRequestControl" value="<%=PhotosImportModel.DELAY_BETWEEN_REQUEST_FORM_CONTROL%>"/>

<c:set var="filesystemImportId" value="<%=PhotosImportSource.FILE_SYSTEM.getId()%>"/>
<c:set var="photosightImportId" value="<%=PhotosImportSource.PHOTOSIGHT.getId()%>"/>
<c:set var="photo35ImportId" value="<%=PhotosImportSource.PHOTO35.getId()%>"/>
<c:set var="naturelightImportId" value="<%=PhotosImportSource.NATURELIGHT.getId()%>"/>

<c:set var="filesystemImportDivId" value="importFormDiv_${filesystemImportId}"/>
<c:set var="photosightImportDivId" value="importFormDiv_${photosightImportId}"/>
<c:set var="baseUrl" value="${eco:baseUrl()}" />

<c:set var="photoImageImportStrategyFile" value="<%=PhotoImageImportStrategyType.FILE%>" />
<c:set var="photoImageImportStrategyWeb" value="<%=PhotoImageImportStrategyType.WEB%>" />

<tags:page pageModel="${photosImportModel.pageModel}">

	<admin:jobEditData jobModel="${photosImportModel}">

			<jsp:attribute name="jobForm">

				<table:table width="1000">

					<table:tr>
						<table:td colspan="2">
							<admin:saveJobButton jobModel="${photosImportModel}"/>
						</table:td>
					</table:tr>

					<table:separatorInfo colspan="2" title="${eco:translate('Job JSP: Job parameters')}"/>

					<table:tr>
						<table:tdtext text_t="Photo import job JSP: Import sources" isMandatory="true"/>
						<table:tddata>
							<form:radiobuttons path="${importSourceIdControl}" items="${photosImportModel.photosImportSourceTranslatableList.entries}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />"
											   onchange="setFormsVisibility();"/>
						</table:tddata>
					</table:tr>

					<table:separator colspan="2"/>

					<table:tr>
						<table:td colspan="2">

							<%-- FILE SYSTEM IMPORT --%>
							<div id="${filesystemImportDivId}" style="float: left; width: 100%; display: none;">
								<table:table width="100%">
									<table:tr>
										<table:tdtext text_t="Photo import job parameter: Dir" isMandatory="true"/>
										<table:tddata>
											<tags:inputHint inputId="${pictureDirFormControl}" hintTitle_t="Photo import job parameter: Dir" hint="Photo import job parameter: Dir">
										<jsp:attribute name="inputField">
											<html:input fieldId="${pictureDirFormControl}" fieldValue="${photosImportModel.pictureDir}" size="60"/>
										</jsp:attribute>
											</tags:inputHint>
										</table:tddata>
									</table:tr>

									<table:separator colspan="2"/>

									<table:tr>
										<table:td colspan="2">${eco:translate('Photo import job JSP: Assign all generated photos to member')}</table:td>
									</table:tr>

									<table:tr>
										<table:td colspan="2">
											<%--<user:userPicker userIdControl="${userIdControl}" user="${photosImportModel.assignAllGeneratedPhotosToUser}"/>--%>
											<c:set var="userPickerId" value="${not empty photosImportModel.assignAllGeneratedPhotosToUser ? photosImportModel.assignAllGeneratedPhotosToUser.id : 0}" />
											<div class="user-picker-container" style="float: left; width: 100%;"></div>
											<script type="text/javascript">
												require( ['components/user-picker/user-picker'], function ( userPicker ) {
													userPicker( "${userIdControl}", ${userPickerId}, callbackFunction, '${baseUrl}', $( '.user-picker-container' ) );
												} );

												function callbackFunction( user ) {
													console.log( 'callback: ', user.userId );
												}
											</script>
										</table:td>
									</table:tr>

									<table:separator colspan="2"/>

									<table:tr>
										<table:td colspan="2">
											<tags:dateRange dateRangeTypeId="${photosImportModel.dateRangeTypeId}" dateFrom="${photosImportModel.dateFrom}"
															dateTo="${photosImportModel.dateTo}" timePeriod="${photosImportModel.timePeriod}"/>
										</table:td>
									</table:tr>

									<table:separator colspan="2"/>

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
											<tags:inputHint inputId="${deletePictureAfterImportControl}" hintTitle_t="Photo import job JSP: Delete picture after import" hint="Photo import job JSP: Delete picture after import">
										<jsp:attribute name="inputField">
											<form:checkbox path="${deletePictureAfterImportControl}" value="true"/>
										</jsp:attribute>
											</tags:inputHint>
											<br/>
										</table:tddata>
									</table:tr>
								</table:table>
							</div>
							<%-- / FILE SYSTEM IMPORT --%>

						</table:td>
					</table:tr>

					<table:tr>
						<table:td colspan="2">

							<%-- REMOTE PHOTO SITE IMPORT --%>
							<div id="${photosightImportDivId}" style="float: left; width: 100%; display: none;">
								<div style="float: left; width: 100%;">

								<table:table width="100%">
									<table:tr>
										<table:tdtext text_t="Photo import job JSP: Photosight user ids" isMandatory="true"/>
										<table:td>
											<form:input path="${remotePhotoSiteUserIdsControl}" size="60" onchange="renderRemoteUserInfo();"/>
											<br/>
											${eco:translate("Photo import job JSP: Use ',' as separator")}
											<div id="photosightUserInfoDiv" class="floatleft" style="display: none;"></div>
										</table:td>
									</table:tr>

									<table:tr>
										<table:td colspan="2">
											<js:checkboxMassChecker checkboxClass="remote-photo-site-category-nude" initiallyChecked="${photosImportModel.remotePhotoSiteImport_importNudeContentByDefault}" /> ${eco:translate('Photo import job JSP: Nude categories')}
											&nbsp;&nbsp;
											<js:checkboxMassChecker checkboxClass="remote-photo-site-category-no-nude" initiallyChecked="true" /> ${eco:translate('Photo import job JSP: No nude categories')}

											<div class="remote-site-categories-container" style="float: left; width: 100%; height: auto; min-height: 120px; padding-top: 10px;"></div>

											<script type="text/javascript">

												function renderRemoteSiteCategories() {
													require( [ 'jquery', 'modules/admin/jobs/photosImport/remoteSiteCategories/remote-site-categories'], function ( $, func ) {
														var importSourceId = $( 'input[name=' + '${importSourceIdControl}' + ']:checked' ).val();
														func( importSourceId, "${baseUrl}", $( '.remote-site-categories-container' ) );
													} );
												}
											</script>
										</table:td>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Photo import job parameter: Import comments"/>
										<table:td>
											<form:checkbox path="${importCommentsControl}" itemValue="true"/>
										</table:td>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Break current photosight user's photos import if already imported photo found"/>
										<table:td>
											<form:checkbox path="${breakImportIfAlreadyImportedPhotoFoundControl}" itemValue="true"/>
										</table:td>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Photo import job parameter: Pages to process"/>
										<table:td>
											<form:input path="${pageQtyControl}" size="4"/>
										</table:td>
									</table:tr>

									<table:separator colspan="2" />

									<table:tr>
										<table:tdtext text_t="Photo import job parameter: Gender" isMandatory="true"/>
										<table:td>
											<form:radiobuttons path="${userGenderIdControl}" items="${photosImportModel.userGenderTranslatableList.entries}" itemValue="id" itemLabel="name" delimiter="<br />"
															   htmlEscape="false"/>
										</table:td>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Photo import job parameter: Membership" isMandatory="true"/>
										<table:td>
											<form:radiobuttons path="${userMembershipIdControl}" items="${photosImportModel.userMembershipTypeTranslatableList.entries}" itemValue="id" itemLabel="name" delimiter="<br />"
															   htmlEscape="false"/>
										</table:td>
									</table:tr>

									<table:separator colspan="2" />

									<table:tr>
										<table:tdtext text_t="Photo import job parameter: Delay between requests"/>
										<table:td>
											<form:input path="${delayBetweenRequestControl}" size="4"/>
										</table:td>
									</table:tr>

									<table:separator colspan="2" />

									<table:tr>
										<table:tdtext text_t="Photo import job parameter: Image import strategy"/>
										<table:td>
											<form:radiobutton path="photoImageImportStrategyTypeId" value="${photoImageImportStrategyFile.id}" /> ${eco:translate(photoImageImportStrategyFile.description)}
											<br />
											<form:radiobutton path="photoImageImportStrategyTypeId" value="${photoImageImportStrategyWeb.id}" /> ${eco:translate(photoImageImportStrategyWeb.description)}
										</table:td>
									</table:tr>

								</table:table>
								</div>

							</div>
							<%-- / REMOTE PHOTO SITE IMPORT --%>

						</table:td>
					</table:tr>

				</table:table>

			</jsp:attribute>

	</admin:jobEditData>

	<script type="text/javascript">

		require( [ 'jquery' ], function( $ ) {

			$( document ).ready( function () {
				setFormsVisibility();

				var type = $( 'input[name=' + '${importSourceIdControl}' + ']:checked' ).val();
				if ( type == ${photosightImportId} || type == ${photo35ImportId} || type == ${naturelightImportId} ) {
					renderRemoteUserInfo();
				}
			} );
		});

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
			require( [ '/admin/js/photosight.js' ], function( photosight ) {
				var usedIdsControl = $( "#${remotePhotoSiteUserIdsControl}" );
				if ( usedIdsControl.val().trim() != '' ) {
					var photosImportSourceId = $( "[name='${importSourceIdControl}']:checked" ).val();
					var data = photosight.renderRemoteUserInfo( usedIdsControl.val(), photosImportSourceId, jsonRPC );

					$( 'input[name="${userGenderIdControl}"][value="' + data.userGenderId + '"]' ).attr( 'checked', 'checked' );
					$( 'input[name="${userMembershipIdControl}"][value="' + data.userMembershipTypeId + '"]' ).attr( 'checked', 'checked' );
				}
			});
		}
	</script>

	<div class="footerseparator"></div>

</tags:page>