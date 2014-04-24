<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

	function adminSetPhotoNudeContent( photoId, isNudeContent, callback ) {

		<%--jsonRPC.ajaxService.setPhotoNudeContent( photoId, isNudeContent );--%>

		$.ajax( {
			type:'GET',
			url: "${eco:baseUrl()}/json/photos/" + photoId + "/nude-content/" + isNudeContent + '/',
			success:function ( response ) {
				if ( callback ) {
					callback();
				}
			},
			error:function () {
				showUIMessage_Error( "${eco:translate('Error executing ajax query')}" );
			}
		} );
	}

	return {
		adminLockUser: function ( userId, userName ) {

			var url = "${eco:baseAdminUrl()}/members/" + userId + "/lock/";
			$( '#lockUserIFrame' ).attr( 'src', url );

			$( "#lockUserDivId" )
					.dialog( 'option', 'title', "${eco:translate('Lock user')}" + ' ' + userName + ' ( #' + userId + ' )' )
					.dialog( 'option', 'buttons', {
													Cancel:function () {
														$( this ).dialog( "close" );
													}
												} )
					.dialog( "open" );
		},

		adminPhotoNudeContentSet: function ( photoId, callback ) {
			adminSetPhotoNudeContent( photoId, true, callback );
		},

		adminPhotoNudeContentRemove: function ( photoId, callback ) {
			adminSetPhotoNudeContent( photoId, false, callback );
		}

	}
});