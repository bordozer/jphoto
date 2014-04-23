<%@ page import="ui.controllers.photos.edit.PhotoEditDataModel"%><%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="photoGenreIdControl" value="<%=PhotoEditDataModel.PHOTO_EDIT_DATA_GENRE_ID_FORM_CONTROL%>"/>

define( [ 'jquery' ], function ( $ ) {

	var genresCanHaveNudeContent;
	var genresHaveNudeContent;

	<c:if test="${not empty genresCanHaveNudeContent}">
			genresCanHaveNudeContent = new Array( -1, ${genresCanHaveNudeContent} );
	</c:if>
	<c:if test="${empty genresCanHaveNudeContent}">
		genresCanHaveNudeContent = [];
	</c:if>

	<c:if test="${not empty genresHaveNudeContent}">
		genresHaveNudeContent = new Array( -1, ${genresHaveNudeContent} );
	</c:if>
	<c:if test="${empty genresHaveNudeContent}">
		genresHaveNudeContent = [];
	</c:if>



		function setAnonymousPosting() {
			<c:if test="${photoEditDataModel.anonymousDay}">
			var anonymousSettingsDTO = jsonRPC.securityService.forceAnonymousPostingAjax( ${photoAuthorId}, getGenreId() ); // TODO: JS error here! Must be moved to ajaxService
			var forcedAnonymousPosting = anonymousSettingsDTO.forcedAnonymousPosting;
			var messages = anonymousSettingsDTO.messages;

			clearAnonymousDescription();
			var list = messages.list;
			for ( var key in list ) {
				$( "#anonymousDescription" ).append( list[ key ] );
				$( "#anonymousDescription" ).append( "<br />" );
			}

			if ( forcedAnonymousPosting ) {
				forceCheckAnonymousPostingControl();
			} else {
				enableAnonymousPostingControl();
			}
			</c:if>
		}

		function setNudeControl() {
			processNudeContentControl( getGenreId() );
		}

		function getGenreId() {
			return $( "#${photoGenreIdControl}" ).val();
		}

		function forceCheckAnonymousPostingControl() {
			checkAnonymousPostingControl();
			disableAnonymousPostingControl();
		}

		function checkAnonymousPostingControl() {
			$( "#anonymousPosting1" ).attr( 'checked', 'checked' );
		}

		function uncheckAnonymousPostingControl() {
			$( "#anonymousPosting1" ).removeAttr( 'checked' );
		}

		function enableNudeContentControl() {
			$( "#containsNudeContent1" ).removeAttr( 'disabled' );
		}

		function disableNudeContentControl() {
			$( "#containsNudeContent1" ).attr( 'disabled', 'disabled' );
		}

		function enableAnonymousPostingControl() {
			$( "#anonymousPosting1" ).removeAttr( 'disabled' );
		}

		function disableAnonymousPostingControl() {
			$( "#anonymousPosting1" ).attr( 'disabled', 'disabled' );
		}

		function setNudeContentDescription( text ) {
			$( "#nudeContentDescription" ).html( text );
		}

		function clearAnonymousDescription() {
			$( "#anonymousDescription" ).text( '' );
		}

	return {

		performPhotoCategoryChange: function () {
			setNudeControl();
			setAnonymousPosting();
		},

		processNudeContentControl: function ( genreId ) {
			if ( genreHaveNudeContent( genreId ) ) {
				checkNudeContentControl();
				disableNudeContentControl();
				setNudeContentDescription( "${eco:translate('All photos in this category have nude content obviously')}" );
				return;
			}

			if ( genreCanHaveNudeContent( genreId ) ) {
				enableNudeContentControl();
				setNudeContentDescription( "${eco:translate('The photo category can contains nude content')}" );
			} else {
				disableNudeContentControl();
				uncheckNudeContentControl();
				setNudeContentDescription( "${eco:translate('The photo category can not contains nude content')}" );
			}

			function genreCanHaveNudeContent( genreId ) {
				return containsValue( genresCanHaveNudeContent, genreId );
			}

			function genreHaveNudeContent( genreId ) {
				return containsValue( genresHaveNudeContent, genreId );
			}

			function checkNudeContentControl() {
				$( "#containsNudeContent1" ).attr( 'checked', 'checked' );
			}

			function uncheckNudeContentControl() {
				$( "#containsNudeContent1" ).removeAttr( 'checked' );
			}

			function containsValue( array, value ) {
				for ( var i = 0; i < array.length; i++ ) {
					if ( array[i] == value ) {
						return true;
					}
				}
				return false;
			}
		}
	}
});