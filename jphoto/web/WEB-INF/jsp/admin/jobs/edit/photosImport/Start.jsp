<%@ page import="admin.controllers.jobs.edit.photosImport.PhotosImportModel" %>
<%@ page import="core.general.user.UserMembershipType" %>
<%@ page import="core.enums.UserGender" %>
<%@ page import="admin.controllers.jobs.edit.photosImport.PhotosImportSource" %>
<%@ page import="admin.controllers.jobs.edit.photosImport.strategies.photosight.PhotosightCategory" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="org.jabsorb.JSONRPCBridge" %>
<%@ page import="core.context.ApplicationContextHelper" %>
<%@ page import="core.services.ajax.AjaxService" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
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

<c:set var="photosImportSources" value="<%=PhotosImportSource.values()%>"/>
<c:set var="importSourceIdControl" value="<%=PhotosImportModel.FORM_CONTROL_PHOTOS_IMPORT_SOURCE_ID%>"/>

<c:set var="pictureDirFormControl" value="<%=PhotosImportModel.PICTURE_DIR_FORM_CONTROL%>"/>
<c:set var="userQtyLimitControl" value="<%=PhotosImportModel.PHOTO_QTY_LIMIT_FORM_CONTROL%>"/>
<c:set var="deletePictureAfterImportControl" value="<%=PhotosImportModel.DELETE_PICTURE_AFTER_IMPORT_CONTROL%>"/>
<c:set var="userIdControl" value="<%=PhotosImportModel.USER_ID_FORM_CONTROL%>"/>

<c:set var="photosightUserIdControl" value="<%=PhotosImportModel.FORM_CONTROL_PHOTOSIGHT_USER_ID%>"/>
<c:set var="userNameControl" value="<%=PhotosImportModel.FORM_CONTROL_USER_NAME%>"/>
<c:set var="userGenderIdControl" value="<%=PhotosImportModel.USER_GENDER_ID_FORM_CONTROL%>"/>
<c:set var="userMembershipIdControl" value="<%=PhotosImportModel.USER_MEMBERSHIP_ID_FORM_CONTROL%>"/>
<c:set var="pageQtyControl" value="<%=PhotosImportModel.ACTIONS_QTY_FORM_CONTROL%>"/>
<c:set var="importCommentsControl" value="<%=PhotosImportModel.IMPORT_COMMENTS_FORM_CONTROL%>"/>
<c:set var="photosightCategoriesControl" value="<%=PhotosImportModel.PHOTOSIGHT_CATEGORIES_FORM_CONTROL%>"/>
<c:set var="delayBetweenRequestControl" value="<%=PhotosImportModel.DELAY_BETWEEN_REQUEST_FORM_CONTROL%>"/>

<c:set var="userGenders" value="<%=UserGender.values()%>"/>
<c:set var="userMembershipTypes" value="<%=UserMembershipType.values()%>"/>

<c:set var="filesystemImportId" value="<%=PhotosImportSource.FILE_SYSTEM.getId()%>"/>
<c:set var="photosightImportId" value="<%=PhotosImportSource.PHOTOSIGHT.getId()%>"/>

<%
	final List<PhotosightCategory> photosightCategories = Arrays.asList( PhotosightCategory.values() );
	Collections.sort( photosightCategories, new Comparator<PhotosightCategory>() {
		@Override
		public int compare( final PhotosightCategory category1, final PhotosightCategory category12 ) {
			return category1.name().compareTo( category12.name() );
		}
	} );
%>
<%
	JSONRPCBridge.getGlobalBridge().registerObject( "ajaxService", ApplicationContextHelper.<AjaxService>getBean( AjaxService.BEAN_NAME ) );
%>
<c:set var="photosightCategories" value="<%=photosightCategories%>"/>

<c:set var="filesystemImportDivId" value="importFormDiv_${filesystemImportId}"/>
<c:set var="photosightImportDivId" value="importFormDiv_${photosightImportId}"/>

<tags:page pageModel="${photosImportModel.pageModel}">

	<admin:jobEditData jobModel="${photosImportModel}">

			<jsp:attribute name="jobForm">

				<table:table width="1000">

					<table:tr>
						<table:td colspan="2">
							<admin:saveJobButton jobModel="${photosImportModel}"/>
						</table:td>
					</table:tr>

					<table:separatorInfo colspan="2" title="${eco:translate('Job parameters')}"/>

					<table:tr>
						<table:tdtext text_t="Import sources" isMandatory="true"/>
						<table:tddata>
							<form:radiobuttons path="${importSourceIdControl}" items="${photosImportSources}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="<br />"
											   onchange="setFormsVisibility();"/>
						</table:tddata>
					</table:tr>

					<table:separator colspan="2"/>

					<table:tr>
						<table:td colspan="2">

							<%-- FILE SYSTEM IMPORT --%>
							<div id="${filesystemImportDivId}" style="float: left; width: 100%; display: none;">
								<table:table width="100%">
									<%--TODO: translate--%>
									<table:tr>
										<table:tdtext text_t="Folder with test pictures" isMandatory="true"/>
										<table:tddata>
											<tags:inputHint inputId="${pictureDirFormControl}" hintTitle_t="Folder with test pictures" hint="Folder with test pictures">
										<jsp:attribute name="inputField">
											<html:input fieldId="${pictureDirFormControl}" fieldValue="${photosImportModel.pictureDir}" size="60"/>
										</jsp:attribute>
											</tags:inputHint>
										</table:tddata>
									</table:tr>

									<table:separator colspan="2"/>

									<table:tr>
										<table:td colspan="2">${eco:translate('Assign all generated photos to member')}?</table:td>
									</table:tr>

									<table:tr>
										<table:td colspan="2">
											<user:userPicker userIdControl="${userIdControl}" user="${photosImportModel.assignAllGeneratedPhotosToUser}"/>
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

									<%--TODO: translate--%>
									<table:tr>
										<table:tdtext text_t="Total job steps"/>
										<table:tddata>
											<tags:inputHint inputId="${userQtyLimitControl}" hintTitle_t="Total job steps"
															hint="Photo qty<br />Leave empty to process all images in the folder">
										<jsp:attribute name="inputField">
											<html:input fieldId="${userQtyLimitControl}" fieldValue="${photosImportModel.photoQtyLimit}" size="3"/>
										</jsp:attribute>
											</tags:inputHint>
											<br/>
											${eco:translate('Leave empty to process all images')}
										</table:tddata>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Delete picture after import"/>
										<table:tddata>
											<tags:inputHint inputId="${deletePictureAfterImportControl}" hintTitle_t="Delete picture after import" hint="Delete picture after import">
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

							<%-- PHOTOSIGHT IMPORT --%>
							<div id="${photosightImportDivId}" style="float: left; width: 100%; display: none;">
								<div style="float: left; width: 80%;">
								<table:table width="100%">
									<table:tr>
										<table:tdtext text_t="Photosight user ids" isMandatory="true"/>
										<table:td>
											<form:input path="${photosightUserIdControl}" size="60" onchange="showPhotosightUserInfoAsync();"/>
											<br/>
											${eco:translate("Use ',' as separator")}
											<div id="photosightUserInfoDiv" class="floatleft" style="display: none;"></div>
										</table:td>
									</table:tr>

									<%--<table:tr>
										<table:tdtext text_t="Import photos from photosight categories"/>
										<table:td>
											<js:checkBoxChecker namePrefix="photosightCategories" isChecked="true" />
											<br />
											<form:checkboxes path="${photosightCategoriesControl}" items="${photosightCategories}" itemValue="id" itemLabel="name" delimiter="<br />" />
										</table:td>
									</table:tr>--%>

									<table:tr>
										<table:tdtext text_t="Import comments"/>
										<table:td>
											<form:checkbox path="${importCommentsControl}" itemValue="true"/>
										</table:td>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Photosight import: Page qty"/>
										<table:td>
											<form:input path="${pageQtyControl}" size="4"/>
										</table:td>
									</table:tr>

									<table:separator colspan="2" />

									<table:tr>
										<table:tdtext text_t="Photosight import: Custom user name"/>
										<table:td>
											<form:input path="${userNameControl}"/>
											<br/>
											${eco:translate('Photosight user name is used if leave empty')}
										</table:td>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Gender" isMandatory="true"/>
										<table:td>
											<form:radiobuttons path="${userGenderIdControl}" items="${userGenders}" itemValue="id" itemLabel="name" delimiter="<br />"
															   htmlEscape="false"/>
										</table:td>
									</table:tr>

									<table:tr>
										<table:tdtext text_t="Membership type" isMandatory="true"/>
										<table:td>
											<form:radiobuttons path="${userMembershipIdControl}" items="${userMembershipTypes}" itemValue="id" itemLabel="name" delimiter="<br />"
															   htmlEscape="false"/>
										</table:td>
									</table:tr>

									<table:separator colspan="2" />

									<table:tr>
										<table:tdtext text_t="Delay between requests in seconds"/>
										<table:td>
											<form:input path="${delayBetweenRequestControl}" size="4"/>
										</table:td>
									</table:tr>

								</table:table>
								</div>
								<div style="float: left; width: 20%;">
									${eco:translate('Import photos from photosight categories')}
									<br />
									<js:checkBoxChecker namePrefix="photosightCategories" isChecked="true" />
									<br />
									<form:checkboxes path="${photosightCategoriesControl}" items="${photosightCategories}" itemValue="id" itemLabel="name" delimiter="<br />" />
								</div>

							</div>
							<%-- / PHOTOSIGHT IMPORT --%>

						</table:td>
					</table:tr>

				</table:table>

			</jsp:attribute>

	</admin:jobEditData>

	<script type="text/javascript">
		jQuery().ready( function () {
			setFormsVisibility();
			if ( $( "#${photosightUserIdControl}" ).val().trim() != '' ) {
				showPhotosightUserInfoAsync();
			}
		} );

		function setFormsVisibility() {
			var type = $( 'input[name=' + '${importSourceIdControl}' + ']:checked' ).val();

			if ( type == ${filesystemImportId} ) {
				$( '#${photosightImportDivId}' ).hide();
				$( '#${filesystemImportDivId}' ).show();
			}

			if ( type == ${photosightImportId} ) {
				$( '#${filesystemImportDivId}' ).hide();
				$( '#${photosightImportDivId}' ).show();
			}
		}

		var jsonRPC;
		jQuery().ready( function () {
			jsonRPC = new JSONRpcClient( "${eco:baseUrl()}/JSON-RPC" );
		} );

		var photosightUserModel = function () {

			var photosightUsersIds = [];
			var photosightUserInfoDivSelector = "#photosightUserInfoDiv";

			function resetPhotosightUser( photosightUserId ) {
				photosightUsersIds = [];
			}

			function registerPhotosightUser( photosightUserId ) {
				if ( ! isPhotosightUserRegistered( photosightUserId ) ) {
					photosightUsersIds.push( photosightUserId );
				}
			}

			function isPhotosightUserRegistered( photosightUserId ) {
				return findPhotosightUser( photosightUserId ) != null;
			}

			function findPhotosightUser( photosightUserId ) {
				for ( var index = 0; index < photosightUsersIds.length; index++ ) {
					var entry = photosightUsersIds[ index ];
					if ( entry == photosightUserId ) {
						return entry;
					}
				}
				return null;
			}

			function getPhotosightUsersIds( _photosightUserIds ) {
				var arr = _photosightUserIds.split( ',' );
				var photosightUserIds = [];
				for ( var i = 0; i < arr.length; i++ ) {
					photosightUserIds.push( arr[i].trim() );
				}
				return photosightUserIds;
			}

			function getPhotosightUserInfoDiv() {
				return $( photosightUserInfoDivSelector );
			}

			function showPhotosightUserInfoDiv() {
				getPhotosightUserInfoDiv().show();
			}

			function hidePhotosightUserInfoDiv() {
				getPhotosightUserInfoDiv().hide();
			}

			function clearPhotosightUserInfoDiv() {
				getPhotosightUserInfoDiv().html( '' );
			}

			function renderExistingPhotosightUser( photosightUserDTO ) {

				var photosightUserName = photosightUserDTO.photosightUserName;
				var photosightUserCardUrl = photosightUserDTO.photosightUserCardUrl;

				var div = getPhotosightUserInfoDiv();
				div.append( "#" + photosightUserDTO.photosightUserId
									+ ": <a href=\"" + photosightUserCardUrl + "\" target=\"_blank\">" + photosightUserName + "</a>"
									+ ", " + photosightUserDTO.photosightUserPhotosCount + " ${eco:translate('photos')}"
				);

				if ( photosightUserDTO.photosightUserExistsInTheSystem ) {
					div.append( ' ( ' + photosightUserDTO.userCardLink + ", <a href='" + photosightUserDTO.userPhotosUrl + "'>" + photosightUserDTO.photosCount + " ${eco:translate('photos')}</a> )" );
				}
			}

			function renderNotExistingPhotosightUser( photosightUserId ) {
				getPhotosightUserInfoDiv().append( "#" + photosightUserId + ": <span class='newInsertedComment'>Not found</span>" );
			}

			return {
				registerPhotosightUsers:function ( _photosightUserIds ) {

					resetPhotosightUser();

					var ids = _photosightUserIds.trim();

					if ( ids == '' ) {
						hidePhotosightUserInfoDiv();
						return;
					}

					var photosightUserIds = getPhotosightUsersIds( ids );
					for ( var i = 0; i < photosightUserIds.length; i++ ) {
						registerPhotosightUser( photosightUserIds[i] );
					}
				},
				renderPhotosightUsers:function () {
					var photosightUserInfoDiv = $( photosightUserInfoDivSelector );

					clearPhotosightUserInfoDiv();

					for ( var index = 0; index < photosightUsersIds.length; index++ ) {

						var photosightUserId = photosightUsersIds[ index ];

						var photosightUserDTO = jsonRPC.ajaxService.getPhotosightUserDTO( photosightUserId );
						if ( photosightUserDTO.photosightUserFound ) {
							renderExistingPhotosightUser( photosightUserDTO );
						} else {
							renderNotExistingPhotosightUser( photosightUserId );
						}
						photosightUserInfoDiv.append( '<br />' );

						showPhotosightUserInfoDiv();

						if ( photosightUserDTO.photosightUserExistsInTheSystem ) {
							$( 'input[name="${userGenderIdControl}"][value="' + photosightUserDTO.userGender.id + '"]' ).attr( 'checked', 'checked' );
							$( 'input[name="${userMembershipIdControl}"][value="' + photosightUserDTO.userMembershipType.id + '"]' ).attr( 'checked', 'checked' );
						}
					}
				}
			}
		}();

		function showPhotosightUserInfoAsync() {
			setTimeout( showPhotosightUserInfo, 1 );
		}

		function showPhotosightUserInfo() {
			var _photosightUserIds = $( "#${photosightUserIdControl}" ).val();
			photosightUserModel.registerPhotosightUsers( _photosightUserIds );
			photosightUserModel.renderPhotosightUsers();
		}
	</script>

	<div class="footerseparator"></div>

</tags:page>