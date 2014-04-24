<%@ page contentType="text/javascript" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery', 'noty', 'toastmessage' ], function ( $ ) {


	var photosightUserModel = function () {

		var photosightUsersIds = [];
		var photosightUserInfoDivSelector = "#photosightUserInfoDiv";

		function resetPhotosightUser( photosightUserId ) {
			photosightUsersIds = [];
		}

		function registerPhotosightUser( photosightUserId ) {
			if ( !isPhotosightUserRegistered( photosightUserId ) ) {
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
			div.append( "#" + photosightUserDTO.photosightUserId + ": <a href=\"" + photosightUserCardUrl + "\" target=\"_blank\">" + photosightUserName + "</a>" + ", " + photosightUserDTO.photosightUserPhotosCount + " ${eco:translate('photos')}" );

			if ( photosightUserDTO.photosightUserExistsInTheSystem ) {
				div.append( ' ( ' + photosightUserDTO.userCardLink + ", <a href='" + photosightUserDTO.userPhotosUrl + "'>" + photosightUserDTO.photosCount + " ${eco:translate('photos')}</a> )" );
			}
		}

		function renderNotExistingPhotosightUser( photosightUserId ) {
			getPhotosightUserInfoDiv().append( "#" + photosightUserId + ": <span class='newInsertedComment'>${eco:translate('Not found')}</span>" );
		}

		return {

			registerPhotosightUsers: function ( _photosightUserIds ) {

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

			renderPhotosightUsers: function () {
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

					<%--if ( photosightUserDTO.photosightUserExistsInTheSystem ) {
						$( 'input[name="${userGenderIdControl}"][value="' + photosightUserDTO.userGender.id + '"]' ).attr( 'checked', 'checked' );
						$( 'input[name="${userMembershipIdControl}"][value="' + photosightUserDTO.userMembershipType.id + '"]' ).attr( 'checked', 'checked' );
					}--%>
				}
			}
		}
	}();

	return {

		renderPhotosightUserInfo: function ( _photosightUserIds, jsonRPC ) {
			photosightUserModel.registerPhotosightUsers( _photosightUserIds );
			photosightUserModel.renderPhotosightUsers( jsonRPC );
		}
	};
} );
