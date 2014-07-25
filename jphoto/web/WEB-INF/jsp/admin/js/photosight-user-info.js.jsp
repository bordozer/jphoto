<%@ page contentType="text/javascript" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

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

		function getRemoteUserInfoDiv() {
			return $( photosightUserInfoDivSelector );
		}

		function showRemoteUserInfoDiv() {
			getRemoteUserInfoDiv().show();
		}

		function hidePhotosightUserInfoDiv() {
			getRemoteUserInfoDiv().hide();
		}

		function clearRemoteUserInfoDiv() {
			getRemoteUserInfoDiv().html( '' );
		}

		function renderExistingPhotosightUser( remoteUserDTO ) {

			var photosightUserName = remoteUserDTO.photosightUserName;
			var photosightUserCardUrl = remoteUserDTO.photosightUserCardUrl;

			var div = getRemoteUserInfoDiv();
			div.append( "#" + remoteUserDTO.photosightUserId + ": <a href=\"" + photosightUserCardUrl + "\" target=\"_blank\">" + photosightUserName + "</a>" + ", " + remoteUserDTO.photosightUserPhotosCount + " ${eco:translate('ROD PLURAL photos')}" );

			if ( remoteUserDTO.photosightUserExistsInTheSystem ) {
				div.append( ' ( ' + remoteUserDTO.userCardLink + ", <a href='" + remoteUserDTO.userPhotosUrl + "'>" + remoteUserDTO.photosCount + " ${eco:translate('ROD PLURAL photos')}</a> )" );
			}
		}

		function renderNotExistingPhotosightUser( remoteUserId ) {
			getRemoteUserInfoDiv().append( "#" + remoteUserId + ": <span class='newInsertedComment'>${eco:translate('Remote web site import -> user info: User not found')}</span>" );
		}

		return {

			registerPhotosightUsers: function ( _photosightUserIds, photosImportSourceId ) {

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

			renderRemoteUsers: function ( jsonRPC, photosImportSourceId ) {
				var photosightUserInfoDiv = $( photosightUserInfoDivSelector );

				clearRemoteUserInfoDiv();

				var userGenderId = 0;
				var userMembershipTypeId = 0;

				for ( var index = 0; index < photosightUsersIds.length; index++ ) {

					var remoteUserId = photosightUsersIds[ index ];

					console.log( photosImportSourceId );
					var photosightUserDTO = jsonRPC.ajaxService.getPhotosightUserDTO( remoteUserId, photosImportSourceId );
					if ( photosightUserDTO.photosightUserFound ) {
						renderExistingPhotosightUser( photosightUserDTO );
					} else {
						renderNotExistingPhotosightUser( remoteUserId );
					}
					photosightUserInfoDiv.append( '<br />' );

					showRemoteUserInfoDiv();

					if ( photosightUserDTO.photosightUserExistsInTheSystem ) {
						userGenderId = photosightUserDTO.userGender.id;
						userMembershipTypeId = photosightUserDTO.userMembershipType.id;
					}
				}

				return { userGenderId: userGenderId, userMembershipTypeId: userMembershipTypeId };
			}
		}
	}();

	return {

		renderRemoteUserInfo: function ( _remoteUsersIds, photosImportSourceId, jsonRPC ) {
			photosightUserModel.registerPhotosightUsers( _remoteUsersIds );
			return photosightUserModel.renderRemoteUsers( jsonRPC, photosImportSourceId );
		}
	};
} );
