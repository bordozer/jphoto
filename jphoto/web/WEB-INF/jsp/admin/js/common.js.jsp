<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

	function executeAjaxRequest( _url, callback ) {

		$.ajax( {
			type:'GET',
			url: _url,
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

			var url = "${eco:baseAdminUrl()}/restriction/members/" + userId + "/";
			$( '#lockUserIFrame' ).attr( 'src', url );

			$( "#lockUserDivId" )
					.dialog( 'option', 'title', "${eco:translate('Admin User Restriction: Restrict user')}" + ' ' + userName + ' ( #' + userId + ' )' )
					.dialog( 'option', 'buttons', {
													Cancel:function () {
														$( this ).dialog( "close" );
													}
												} )
					.dialog( "open" );
		},

		adminPhotoNudeContentSet: function ( photoId, callback ) {
			var url = "${eco:baseUrl()}/rest/photos/" + photoId + "/nude-content/true/";
			executeAjaxRequest( url, callback );
		},

		adminPhotoNudeContentRemove: function ( photoId, callback ) {
			var url = "${eco:baseUrl()}/rest/photos/" + photoId + "/nude-content/false/";
			executeAjaxRequest( url, callback );
		},

		movePhotoToCategory: function ( photoId, genreId, callback ) {
			var url = "${eco:baseUrl()}/rest/photos/" + photoId + "/move-to-genre/" + genreId + "/";
			executeAjaxRequest( url, callback );
		},

		generatePhotoPreview: function ( photoId, callback ) {
			var url = "${eco:baseUrl()}/rest/photos/" + photoId + "/preview/";
			executeAjaxRequest( url, callback );
		}
	}
});