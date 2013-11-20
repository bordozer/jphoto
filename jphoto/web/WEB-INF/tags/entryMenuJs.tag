<%@ tag import="org.jabsorb.JSONRPCBridge" %>
<%@ tag import="core.context.ApplicationContextHelper" %>
<%@ tag import="core.services.ajax.AjaxService" %>
<%@ tag import="core.general.menus.EntryMenuType" %>
<%@ tag import="core.general.menus.comment.ComplaintReasonType" %>
<%@ tag import="core.services.utils.UrlUtilsService" %>
<%@ taglib prefix="eco" uri="http://jphoto.dev" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>

<c:set var="customDescriptionTextId" value="customDescriptionTextId" />
<c:set var="sendComplaintDivId" value="sendCoplaintDivId" />

<%
	JSONRPCBridge.getGlobalBridge().registerObject( "ajaxService", ApplicationContextHelper.<AjaxService>getBean( AjaxService.BEAN_NAME ) );
%>

<c:set var="complaintEntities" value="<%=EntryMenuType.values()%>"/>
<c:set var="complaintReasonTypes" value="<%=ComplaintReasonType.values()%>"/>

<div id="${sendComplaintDivId}" title="..." style="display: none;">
	<html:textarea inputId="${customDescriptionTextId}" title="${eco:translate('Message')}" hint="${eco:translate('Admin message text')}" rows="7" cols="50" />
</div>

<script type="text/javascript">

	var complaintEntities = {<c:forEach var="complaintEntity" items="${complaintEntities}" varStatus="status"> '${complaintEntity.id}': "${complaintEntity.nameTranslated}" <c:if test="${not status.last}">, </c:if> </c:forEach> };
	var complaintReasonTypes = {<c:forEach var="complaintReasonType" items="${complaintReasonTypes}" varStatus="status"> '${complaintReasonType.id}': "${complaintReasonType.nameTranslated}" <c:if test="${not status.last}">, </c:if> </c:forEach> };

	$( function () {
			$( "#${sendComplaintDivId}" ).dialog( {
											height:300
											, width:600
											, modal:true
											, autoOpen:false
										 } );
		} );

	function getEntityNameById( typeId, entities ) {
		for ( var entity in entities ) {
			if ( entity[0] == typeId ) {
				return entities[entity];
			}
		}
		return "TODO";
	}

	function sendComplaintMessage( complaintEntityTypeId, entryId, fromUserId, complaintReasonTypeId ) {
			$( '#${customDescriptionTextId}' ).val( '' );

		$( "#${sendComplaintDivId}" )
					.dialog( 'option', 'title', "${eco:translate('Send complaint message:')}" + getEntityNameById( complaintEntityTypeId, complaintEntities ) + ' => ' + getEntityNameById( complaintReasonTypeId, complaintReasonTypes ) + ', ID=' + entryId )
					.dialog( 'option', 'buttons', {
													Cancel:function () {
														$( this ).dialog( "close" );
													},
													"${eco:translate('Send message')}": function() {
														var complaintDTO = new ComplaintMessageDTO( complaintEntityTypeId, entryId, fromUserId, complaintReasonTypeId, $( '#${customDescriptionTextId}' ).val() );

														var ajaxResultDTO = jsonRPC.ajaxService.sendComplaintMessageAjax( complaintDTO );

														if ( ! ajaxResultDTO.successful ) {
															showErrorMessage( ajaxResultDTO.message );
															return false;
														}

														notifySuccessMessage( "${eco:translate('The message has been sent')}" );

														$( this ).dialog( "close" );
													}
												} )
					.dialog( "open" );
		}

		function ComplaintMessageDTO( complaintEntityTypeId, entryId, fromUserId, complaintReasonTypeId, customDescription ) {
			this.complaintEntityTypeId = complaintEntityTypeId;
			this.entryId = entryId;
			this.fromUserId = fromUserId;
			this.complaintReasonTypeId = complaintReasonTypeId;
			this.customDescription = customDescription;

			this.getComplaintEntityTypeId = function () {
				return this.complaintEntityTypeId;
			};

			this.getEntryId = function () {
				return this.entryId;
			};

			this.getFromUserId = function () {
				return this.fromUserId;
			};

			this.getComplaintReasonTypeId = function () {
				return this.complaintReasonTypeId;
			};

			this.getCustomDescription = function () {
				return this.customDescription;
			};
		}

	function goToMemberPhotos( memberId ) {
		document.location.href = '${eco:baseUrlWithPrefix()}/photos/members/' + memberId + '/';
	}

	function goToMemberPhotosByGenre( memberId, genreId ) {
		document.location.href = '${eco:baseUrlWithPrefix()}/photos/members/' + memberId + '/genre/' + genreId + '/';
	}

	function goToMemberPhotosByTeamMember( memberId, teamMemberId ) {
		document.location.href = '${eco:baseUrlWithPrefix()}/members/' + memberId + '/team/' + teamMemberId + '/';
	}

	function goToMemberPhotosByAlbum( memberId, albumId ) {
		document.location.href = '${eco:baseUrlWithPrefix()}/members/' + memberId + '/albums/' + albumId + '/';
	}

	function editPhotoData( photoId ) {
		document.location.href = '${eco:baseUrlWithPrefix()}/photos/' + photoId + '/edit/';
	}

	function deletePhoto( photoId ) {
		if ( confirmDeletion( '${eco:translate("Delete photo?")}' ) ) {
			document.location.href = '${eco:baseUrlWithPrefix()}/photos/' + photoId + '/delete/';
		}
	}
</script>