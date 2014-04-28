<%@ page import="ui.controllers.photos.DEL_edit.PhotoEditDataModel" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="core.enums.YesNo" %>
<%@ page import="ui.controllers.photos.DEL_edit.PhotoEditWizardStep" %>
<%@ page import="ui.context.ApplicationContextHelper" %>
<%@ page import="core.general.configuration.ConfigurationKey" %>
<%@ page import="ui.services.validation.PhotoRequirement" %>
<%@ page import="ui.services.validation.DataRequirementService" %>
<%@ page import="ui.translatable.GenericTranslatableList" %>
<%@ page import="ui.context.EnvironmentContext" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="links" tagdir="/WEB-INF/tags/links" %>
<%@ taglib prefix="photo" tagdir="/WEB-INF/tags/photo" %>
<%@ taglib prefix="anonym" tagdir="/WEB-INF/tags/anonym" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:useBean id="photoEditDataModel" type="ui.controllers.photos.DEL_edit.PhotoEditDataModel" scope="request"/>

<%
	final String genresCanHaveNudeContent = StringUtils.join( photoEditDataModel.getGenresCanHaveNudeContent(), ", " );
	final String genresHaveNudeContent = StringUtils.join( photoEditDataModel.getGenresHaveNudeContent(), ", " );
%>

<c:set var="accessibleCommentAllowancesTranslatableList" value="${photoEditDataModel.accessibleCommentAllowancesTranslatableList}"/>
<c:set var="accessibleVotingAllowancesTranslatableList" value="${photoEditDataModel.accessibleVotingAllowancesTranslatableList}"/>

<c:set var="isNew" value="<%=photoEditDataModel.isNew()%>"/>

<c:set var="genresTranslated" value="<%=photoEditDataModel.getGenres()%>"/>
<c:set var="photoAuthor" value="<%=photoEditDataModel.getPhotoAuthor()%>"/>

<c:set var="photoIdControl" value="photoEditDataModel.<%=PhotoEditDataModel.PHOTO_EDIT_DATA_ID_FORM_CONTROL%>"/>
<c:set var="photoNameControl" value="<%=PhotoEditDataModel.PHOTO_EDIT_DATA_NAME_FORM_CONTROL%>"/>
<c:set var="photoGenreIdControl" value="<%=PhotoEditDataModel.PHOTO_EDIT_DATA_GENRE_ID_FORM_CONTROL%>"/>
<c:set var="photoDescriptionControl" value="<%=PhotoEditDataModel.PHOTO_EDIT_DATA_DESCRIPTION_FORM_CONTROL%>"/>
<c:set var="photoKeywordsControl" value="<%=PhotoEditDataModel.PHOTO_EDIT_DATA_KEYWORDS_FORM_CONTROL%>"/>
<c:set var="notificationEmailAboutNewPhotoCommentControl" value="<%=PhotoEditDataModel.FORM_CONTROL_NOTIFICATION_EMAIL_ABOUT_NEW_COMMENT%>"/>
<c:set var="photoBgColorControl" value="<%=PhotoEditDataModel.PHOTO_EDIT_DATA_BGCOLOR_FORM_CONTROL%>"/>

<c:set var="photoTeamMembersIdsControl" value="<%=PhotoEditDataModel.FORM_CONTROL_USER_TEAM_MEMBERS_IDS%>"/>
<c:set var="photoAlbumsIdsControl" value="<%=PhotoEditDataModel.FORM_CONTROL_PHOTO_ALBUMS_IDS%>"/>

<%
	final DataRequirementService dataRequirementService = photoEditDataModel.getDataRequirementService();
	final PhotoRequirement photoRequirement = dataRequirementService.getPhotoRequirement();
%>
<c:set var="photoAuthor" value="${photoEditDataModel.photoAuthor}"/>
<c:set var="photoAuthorId" value="${photoAuthor.id}"/>

<c:set var="nameRequirement" value="<%=photoRequirement.getNameRequirement()%>"/>
<c:set var="descriptionRequirement" value="<%=photoRequirement.getDescriptionRequirement()%>"/>
<c:set var="keywordsRequirement" value="<%=photoRequirement.getKeywordsRequirement()%>"/>

<c:set var="mandatoryText" value="<%=dataRequirementService.getFieldIsMandatoryText()%>"/>
<c:set var="optionalText" value="<%=dataRequirementService.getFieldIsOptionalText()%>"/>

<c:set var="genresCanHaveNudeContent" value="<%=genresCanHaveNudeContent%>"/>
<c:set var="genresHaveNudeContent" value="<%=genresHaveNudeContent%>"/>

<c:set var="yesNoValues" value="<%=GenericTranslatableList.yesNoTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>
<c:set var="editDataWizardStepId" value="<%=PhotoEditWizardStep.EDIT_PHOTO_DATA.getId()%>"/>

<c:set var="uploadAllowance" value="${photoEditDataModel.uploadAllowance}"/>
<c:set var="userCanUploadPhoto" value="${uploadAllowance.userCanUploadPhoto}"/>

<c:set var="photoNameMaxLength" value="<%=ApplicationContextHelper.getConfigurationService().getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH )%>"/>

<tags:page pageModel="${photoEditDataModel.pageModel}">

<%--<script type="text/javascript">

	require( [ 'jquery' ], function( $ ) {

		$( document ).ready( function () {

			<c:if test="${not photoEditDataModel.anonymousDay}">
				$( "#dayAnonymousDescription" ).text( "${eco:translate('Today is not anonymous posting day')}" );
				$( "#anonymousDescription" ).append( "<br />" );
				$( "#anonymousDescription" ).append( "${eco:translate('You decide if you want to upload a photo anonymously')}" );
				$( "#anonymousDescription" ).append( "<br />" );
			 </c:if>

			 <c:if test="${photoEditDataModel.anonymousDay}">
				$( "#dayAnonymousDescription" ).append( "${eco:translate('Today is anonymous posting day')}" );
				$( "#dayAnonymousDescription" ).append( "<br/>" );
			 </c:if>

			processNudeContentControl( getGenreId() );
		} );
	} );

	function performPhotoCategoryChange() {
		require( [ 'jquery', '/js/pages/DEL_photo-data-edit.js.jsp' ], function( $, photoEditDataFunction ) {
			photoEditDataFunction.performPhotoCategoryChange();
		});
	}

	function processNudeContentControl( genreId ) {
		require( [ 'jquery', '/js/pages/DEL_photo-data-edit.js.jsp' ], function( $, photoEditDataFunction ) {
			photoEditDataFunction.processNudeContentControl( genreId );
		});
	}

</script>--%>

<div style="float: left; width: 98%;">
	<c:if test="${isNew}">
		<h3>${eco:translate(photoEditDataModel.currentStep.stepDescription)}</h3>

		<photo:photoAllowance uploadAllowance="${uploadAllowance}" />
	</c:if>

	<script type="text/javascript" src="${eco:baseUrl()}/js/lib/colorpicker/jscolor.js"></script>

	<tags:inputHintForm/>

	<c:if test="${(isNew and userCanUploadPhoto) or not isNew}">

		<%--<eco:form action="${eco:baseUrl()}/photos/${photoEditDataModel.nextStep.urlPrefix}/">--%>

			<input type="hidden" id="currentStepId" name="currentStepId" value="${editDataWizardStepId}">

			<table:table width="800" border="0">

				<table:separatorInfo colspan="2" height="50" title="${eco:translate('Photo data')}"/>

				<%-- Name --%>
				<table:tredit>
					<table:tdtext text_t="Photo uploading: Name" labelFor="${photoNameControl}" isMandatory="true"/>

					<table:tddata>
						<tags:inputHint inputId="${photoNameControl}" hintTitle_t="Photo edit: name hint"
										hint="${nameRequirement}<br /><br />${mandatoryText}" focused="true">
							<jsp:attribute name="inputField">
								<html:textarea inputId="${photoNameControl}" inputValue="${photoEditDataModel.name}" title="${eco:translate('Photo name')}" hint="${eco:translate('Photo name')}" cols="51" rows="2" maxlength="${photoNameMaxLength}" />
							</jsp:attribute>
						</tags:inputHint>
						<br />
						${nameRequirement}
					</table:tddata>
				</table:tredit>
				<%-- / Name --%>

				<table:separator colspan="2" />

				<%-- Genres --%>

				<table:tredit>
					<a name="photo-category" />
					<table:tdtext text_t="Photo edit: Genre" labelFor="${photoGenreIdControl}" isMandatory="true"/>

					<table:tddata>
						<form:select path="photoEditDataModel.genreId" items="${genresTranslated}" itemLabel="nameTranslated" itemValue="genre.id" htmlEscape="false" size="24"/>
					</table:tddata>
				</table:tredit>
				<%-- / Genres --%>

				<table:separator colspan="2" />

				<%-- Nude content --%>
				<table:tredit>
					<table:tdtext text_t="Contains nude content" labelFor="containsNudeContent1"/>

					<table:tddata>
						<form:checkbox path="photoEditDataModel.containsNudeContent"/>
						<span id="nudeContentDescription" style="height: 20px;"></span>
					</table:tddata>
				</table:tredit>
				<%-- / Nude content --%>

				<table:separator colspan="2" />

				<%-- Background color --%>
				<table:tredit>
					<table:tdtext text_t="Background color" labelFor="${photoBgColorControl}"/>

					<table:tddata>
						<html:colorpicker fieldId="${photoBgColorControl}" fieldValue="${photoEditDataModel.bgColor}" />
					</table:tddata>
				</table:tredit>
				<%-- / Background colort --%>

				<table:separator colspan="2" />

				<%-- Description --%>
				<table:tredit>
					<table:tdtext text_t="Description" labelFor="${photoDescriptionControl}"/>

					<table:tddata>
						<tags:inputHint inputId="${photoDescriptionControl}" hintTitle_t="Photo uploading: description hint" hint="${descriptionRequirement}<br /><br />${optionalText}">
							<jsp:attribute name="inputField">
								<html:textarea inputId="${photoDescriptionControl}" inputValue="${photoEditDataModel.description}" title="${eco:translate('Photo description')}" hint="${eco:translate('Photo description')}" />
							</jsp:attribute>
						</tags:inputHint>
						<br />
						${descriptionRequirement}
					</table:tddata>
				</table:tredit>
				<%-- / Description --%>

				<table:separator colspan="2" />

				<%-- Keywords --%>
				<table:tredit>
					<table:tdtext text_t="Keywords" labelFor="${photoKeywordsControl}"/>

					<table:tddata>
						<tags:inputHint inputId="${photoKeywordsControl}" hintTitle_t="Photo uploading: keywords hint"
										hint="${keywordsRequirement}<br /><br />${optionalText}">
							<jsp:attribute name="inputField">
								<html:input fieldId="${photoKeywordsControl}" fieldValue="${photoEditDataModel.keywords}" size="30" maxLength="255"/>
							</jsp:attribute>
						</tags:inputHint>
						<br />
						${keywordsRequirement}
					</table:tddata>
				</table:tredit>
				<%-- / Keywords --%>

				<table:separator colspan="2" />

				<%-- Anonymous Posting --%>
				<table:tredit>
					<table:tdtext text_t="Anonymous posting" labelFor="anonymousPosting1"/>

					<table:tddata>
						<c:if test="${not isNew}">
							${eco:translate1('$1', photoEditDataModel.anonymousPosting ? 'Yes' : 'No')}
						</c:if>

						<c:if test="${isNew}">
							<form:checkbox path="photoEditDataModel.anonymousPosting" />
							<br />
							<span id="dayAnonymousDescription" style="height: 20px;"></span>
							<span id="anonymousDescription" style="height: 20px;"></span>
							<br />
							${eco:translate('You can not edit this option when photo is posted')}
							<br />
							<anonym:anonymousDaySchedule />
						</c:if>
					</table:tddata>
				</table:tredit>
				<%-- / Anonymous Posting --%>

				<table:separatorInfo colspan="2" height="50" title="${eco:translate('Commenting and voting')}"/>

				<%-- Comments allowance --%>
				<table:tredit>
					<table:tdtext text_t="Photo uploading: Comments allowance"/>

					<table:tddata>
						<form:radiobuttons path="photoEditDataModel.commentsAllowanceId" items="${accessibleCommentAllowancesTranslatableList.entries}" itemValue="id" itemLabel="name" delimiter="<br />" />
					</table:tddata>
				</table:tredit>
				<%-- / Comments allowance --%>

				<table:separator colspan="2" />

				<%-- Notification Email AboutNew Photo Comment --%>
				<table:tredit>
					<table:tdtext text_t="Sent notification email about new comments" />

					<table:tddata>
						<form:radiobuttons items="${yesNoValues}" path="photoEditDataModel.${notificationEmailAboutNewPhotoCommentControl}" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="&nbsp;&nbsp;&nbsp;&nbsp;"/>
					</table:tddata>

				</table:tredit>
				<%-- / Notification Email AboutNew Photo Comment --%>

				<table:separator colspan="2" />

				<%-- voting allowance --%>
				<table:tredit>
					<table:tdtext text_t="Photo uploading: Photo appraisal allowance"/>

					<table:tddata>
						<form:radiobuttons path="photoEditDataModel.votingAllowanceId" items="${accessibleVotingAllowancesTranslatableList.entries}" itemValue="id" itemLabel="name" delimiter="<br />" />
					</table:tddata>
				</table:tredit>
				<%-- / voting allowance --%>

				<table:separatorInfo colspan="2" height="50" title="${eco:translate('Team')}"/>

				<%-- User teams members --%>
				<table:tredit>
					<table:tdtext text_t="Photo data editing: Photo team" labelFor="photoTeamIds1"/>

					<table:tddata>
						<form:checkboxes items="${photoEditDataModel.userTeamMembers}"
										 path="photoEditDataModel.${photoTeamMembersIdsControl}"
										 itemLabel="teamMemberNameWithType" itemValue="id" delimiter="<br/>" htmlEscape="true"/>
					</table:tddata>
				</table:tredit>
				<%-- / User teams members --%>

				<table:separator colspan="2" />

				<%-- Photo's albums --%>
				<table:tredit>
					<table:tdtext text_t="Photo albums" labelFor="photoAlbumIds1"/>

					<table:tddata>
						<form:checkboxes items="${photoEditDataModel.userPhotoAlbums}"
										 path="photoEditDataModel.${photoAlbumsIdsControl}"
										 itemLabel="name" itemValue="id" delimiter="<br/>" htmlEscape="true"/>
					</table:tddata>
				</table:tredit>
				<%-- / Photo's albums --%>

				<table:trok text_t="${isNew ? 'Next >>' : 'Save'}" onclick="showUIMessage_InformationMessage_ManualClosing( 'There is more easilly to rewrite this page from scrach then trying to refactor the legasy code...' )"/>

			</table:table>

		<%--</eco:form>--%>

		<%--<script type="text/javascript">

			function isGenreSelected() {
				$( '#${photoGenreIdControl}' ).removeClass( 'invalid' );
				var genreId = $( "#${photoGenreIdControl}" ).val();

				if ( genreId == null ) {
					$( '#${photoGenreIdControl}' ).addClass( 'invalid' );
					document.location.href = document.location + '#photo-category';
					showUIMessage_Error( "${eco:translate('Photo uploading validation message: Select genre')}" );
					return false;
				}

				enableNudeContentControl();

				return true;
			}
		</script>--%>

		<tags:springErrorHighliting bindingResult="${photoEditDataModel.bindingResult}"/>

	</c:if>

	<c:if test="${isNew and not userCanUploadPhoto}">
		<div style="float: left; width: 100%; text-align: center; padding: 30px;">
			<h2>${eco:translate('You can not upload photo')}</h2>
			<html:img id="cantUploadPhoto" src="icons128/canNotUploadPhoto.png" width="128" height="128" alt="${eco:translate('You can not upload photo')}" />
			<br />
			<h3>${eco:translate2('You can upload new photo at $1 $2', eco:formatDate(photoEditDataModel.nextPhotoUploadTime), eco:formatTimeShort(photoEditDataModel.nextPhotoUploadTime))}</h3>
		</div>
	</c:if>
</div>
	<div class="footerseparator"></div>

</tags:page>