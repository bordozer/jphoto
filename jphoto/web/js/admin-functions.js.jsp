<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

	function adminSetPhotoNudeContent( photoId, isNudeContent, jsonRPC ) {

		jsonRPC.ajaxService.setPhotoNudeContent( photoId, isNudeContent );

		// TODO: redraw photo preview
		/*require( ['modules/photo/list/photo-list'], function ( photoListEntry ) {
			var photoListId = 1;
			var element = $( '.photo-container-' + 1 +'-' + photoId );
			photoListEntry( photoId, photoListId, true, '${eco:baseUrl()}', element );
		} );*/
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

		adminPhotoNudeContentSet: function ( photoId, jsonRPC ) {
			adminSetPhotoNudeContent( photoId, true );
		},

		adminPhotoNudeContentRemove: function ( photoId, jsonRPC ) {
			adminSetPhotoNudeContent( photoId, false );
		}

	}
});