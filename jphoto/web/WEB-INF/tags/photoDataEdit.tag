<%@ tag import="core.general.configuration.ConfigurationKey" %>
<%@ tag import="ui.context.ApplicationContextHelper" %>
<%@ tag import="ui.context.EnvironmentContext" %>
<%@ tag import="ui.translatable.GenericTranslatableList" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib prefix="anonym" tagdir="/WEB-INF/tags/anonym" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="photoEditDataModel" type="ui.controllers.photos.edit.PhotoEditDataModel" %>

<c:set var="photoNameMaxLength" value="<%=ApplicationContextHelper.getConfigurationService().getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH )%>"/>
<c:set var="yesNoValues" value="<%=GenericTranslatableList.yesNoTranslatableList( EnvironmentContext.getLanguage(), ApplicationContextHelper.getTranslatorService() ).getEntries()%>"/>

<form:form modelAttribute="photoEditDataModel" method="post" action="${eco:baseUrl()}/photos/save/">

	<table:table width="1000px" border="0">

		<table:separatorInfo colspan="2" title="${eco:translate('Photo uploading: Photo data section header')}" />

		<table:tredit>

			<table:tdtext text_t="Photo uploading: Name" labelFor="photoName" />
			<table:tddata>
				<form:input path="photoName" maxlength="${photoNameMaxLength}" />
			</table:tddata>

		</table:tredit>

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Description" labelFor="photoDescription"/>

			<table:tddata>
				<form:textarea path="photoDescription" cols="50" rows="5" />
			</table:tddata>
		</table:tredit>

		<table:separator colspan="2" />

		<table:tredit>

			<table:tredit>
				<table:tdtext text_t="Photo uploading: Genre" labelFor="selectedGenreId" isMandatory="true"/>

				<table:tddata>

					<div class="photo-category-handler-container justify-font" style="float: left; margin-left: 10px; width: 100%">
						<html:spinningWheel16 title="${eco:translate('Photo data: Photo categories is being loaded')}" />
					</div>

					<script type="text/javascript">

						photoCategoryHandler( ${photoEditDataModel.photoAuthor.id}, ${photoEditDataModel.photo.id}, ${photoEditDataModel.selectedGenreId} );

						function photoCategoryHandler( authorId, photoId, categoryId ) {
							require( ['modules/photo/upload/category/photo-category-handler'], function ( categoryHandler ) {
								categoryHandler( authorId, photoId, categoryId, '${photoEditDataModel.photo.fileSize}', $( '.photo-category-handler-container' ) );
							} );
						}
					</script>

				</table:tddata>
			</table:tredit>

		</table:tredit>

		<table:separator colspan="2" />

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Keywords" labelFor="photoKeywords"/>

			<table:tddata>
				<form:input path="photoKeywords" size="50"/>
				<br />
				${eco:translate("Use comma ',' as separator")}
			</table:tddata>
		</table:tredit>

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Background"/>

			<table:tddata>
				<link rel='stylesheet' href='${eco:baseUrl()}/js/lib/spectrum/spectrum.css' />

				<input type="hidden" id="bgColor" name="bgColor" value="${photoEditDataModel.bgColor}"/>
				<input type="text" class="color-picker" style="display: none;"/>

				<script type="text/javascript">
					require( [ 'jquery', 'spectrum'], function ( $, spectrum ) {
						$( '.color-picker' ).spectrum( {
							color: "${photoEditDataModel.bgColor}"
							, change: function( color ) {
								$( '#bgColor' ).val( color.toHexString() );
							}
						});
					} );
				</script>
			</table:tddata>
		</table:tredit>


		<table:separator colspan="2" />

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Anonymous posting" labelFor="anonymousPosting1"/>

			<table:tddata>
				<form:checkbox path="anonymousPosting"/>
				<br />
				<anonym:anonymousDaySchedule />
			</table:tddata>
		</table:tredit>

		<table:separatorInfo colspan="2" height="50" title="${eco:translate('Photo uploading: Comments and appraisal allowances')}"/>

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Comments allowance"/>

			<table:tddata>
				<form:radiobuttons path="commentsAllowanceId" items="${photoEditDataModel.accessibleCommentAllowancesTranslatableList.entries}" itemValue="id" itemLabel="name" delimiter="<br />" />
			</table:tddata>
		</table:tredit>

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Sent notification email about new comments" />

			<table:tddata>
				<form:radiobuttons items="${yesNoValues}" path="sendNotificationEmailAboutNewPhotoComment" itemValue="id" itemLabel="name" htmlEscape="false" delimiter="&nbsp;&nbsp;&nbsp;&nbsp;"/>
			</table:tddata>

		</table:tredit>

		<table:separator colspan="2" />

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Photo appraisal allowance"/>

			<table:tddata>
				<form:radiobuttons path="votingAllowanceId" items="${photoEditDataModel.accessibleVotingAllowancesTranslatableList.entries}" itemValue="id" itemLabel="name" delimiter="<br />" />
			</table:tddata>
		</table:tredit>

		<table:separatorInfo colspan="2" height="50" title="${eco:translate('Photo uploading: Photo team header')}"/>

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Photo team" labelFor="photoTeamIds1"/>

			<table:tddata>
				<user:userTeam userId="${photoEditDataModel.photoAuthor.id}" selectedByDefaultUserMembersIds="${photoEditDataModel.userTeamMemberIds}" />
			</table:tddata>
		</table:tredit>

		<table:separatorInfo colspan="2" height="50" title="${eco:translate('Photo uploading: Photo albums header')}"/>

		<table:tredit>
			<table:tdtext text_t="Photo uploading: Photo albums" labelFor="photoAlbumIds1"/>

			<table:tddata>
				<form:checkboxes items="${photoEditDataModel.userPhotoAlbums}" path="photoAlbumIds" itemLabel="name" itemValue="id" delimiter="<br/>" htmlEscape="true"/>
			</table:tddata>
		</table:tredit>

		<%--<table:trok text_t="Photo uploading: Save button" onclick="alert( ';)' ); return false;"/>--%>
		<table:trok text_t="Photo uploading: Save button" />

	</table:table>

</form:form>

<%--<tags:springErrorHighliting bindingResult="${photoEditDataModel.bindingResult}"/>--%>
