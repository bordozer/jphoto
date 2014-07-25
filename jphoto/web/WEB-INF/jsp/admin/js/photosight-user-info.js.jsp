<%@ page contentType="text/javascript" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

define( [ 'jquery' ], function ( $ ) {

	var remoteUserModel = function () {

		var remoteUsersIds = [];
		var remoteUserInfoDivSelector = "#photosightUserInfoDiv";

		function resetRemoteUser() {
			remoteUsersIds = [];
		}

		function registerRemoteUser( photosightUserId ) {
			if ( !isPhotosightUserRegistered( photosightUserId ) ) {
				remoteUsersIds.push( photosightUserId );
			}
		}

		function isPhotosightUserRegistered( photosightUserId ) {
			return findPhotosightUser( photosightUserId ) != null;
		}

		function findPhotosightUser( photosightUserId ) {
			for ( var index = 0; index < remoteUsersIds.length; index++ ) {
				var entry = remoteUsersIds[ index ];
				if ( entry == photosightUserId ) {
					return entry;
				}
			}
			return null;
		}

		function getremoteUsersIds( _remoteUserIds ) {
			var arr = _remoteUserIds.split( ',' );
			var remoteUserIds = [];
			for ( var i = 0; i < arr.length; i++ ) {
				remoteUserIds.push( arr[i].trim() );
			}
			return remoteUserIds;
		}

		function getRemoteUserInfoDiv() {
			return $( remoteUserInfoDivSelector );
		}

		function showRemoteUserInfoDiv() {
			getRemoteUserInfoDiv().show();
		}

		function hideRemoteUserInfoDiv() {
			getRemoteUserInfoDiv().hide();
		}

		function clearRemoteUserInfoDiv() {
			getRemoteUserInfoDiv().html( '' );
		}

		function renderExistingRemotetUser( remoteUserDTO ) {

			var remoteUserName = remoteUserDTO.photosightUserName;
			var remoteUserCardUrl = remoteUserDTO.photosightUserCardUrl;

			var div = getRemoteUserInfoDiv();
			div.append( "#" + remoteUserDTO.photosightUserId + ": <a href=\"" + remoteUserCardUrl + "\" target=\"_blank\">" + remoteUserName + "</a>" + ", " + remoteUserDTO.photosightUserPhotosCount + " ${eco:translate('ROD PLURAL photos')}" );

			if ( remoteUserDTO.photosightUserExistsInTheSystem ) {
				div.append( ' ( ' + remoteUserDTO.userCardLink + ", <a href='" + remoteUserDTO.userPhotosUrl + "'>" + remoteUserDTO.photosCount + " ${eco:translate('ROD PLURAL photos')}</a> )" );
			}
		}

		function renderNotExistingRemotetUser( remoteUserId ) {
			getRemoteUserInfoDiv().append( "#" + remoteUserId + ": <span class='newInsertedComment'>${eco:translate('Remote web site import -> user info: User not found')}</span>" );
		}

		return {

			registerRemoteUsers: function ( _remoteUserIds ) {

				resetRemoteUser();

				var ids = _remoteUserIds.trim();

				if ( ids == '' ) {
					hideRemoteUserInfoDiv();
					return;
				}

				var remoteUserIds = getremoteUsersIds( ids );
				for ( var i = 0; i < remoteUserIds.length; i++ ) {
					registerRemoteUser( remoteUserIds[i] );
				}
			},

			renderRemoteUsers: function ( jsonRPC, photosImportSourceId ) {
				var remoteUserInfoDiv = $( remoteUserInfoDivSelector );

				clearRemoteUserInfoDiv();

				var userGenderId = 0;
				var userMembershipTypeId = 0;

				for ( var index = 0; index < remoteUsersIds.length; index++ ) {

					var remoteUserId = remoteUsersIds[ index ];

					console.log( photosImportSourceId );
					var remoteUserDTO = jsonRPC.ajaxService.getPhotosightUserDTO( remoteUserId, photosImportSourceId );
					if ( remoteUserDTO.photosightUserFound ) {
						renderExistingRemotetUser( remoteUserDTO );
					} else {
						renderNotExistingRemotetUser( remoteUserId );
					}
					remoteUserInfoDiv.append( '<br />' );

					showRemoteUserInfoDiv();

					if ( remoteUserDTO.photosightUserExistsInTheSystem ) {
						userGenderId = remoteUserDTO.userGender.id;
						userMembershipTypeId = remoteUserDTO.userMembershipType.id;
					}
				}

				return { userGenderId: userGenderId, userMembershipTypeId: userMembershipTypeId };
			}
		}
	}();

	return {

		renderRemoteUserInfo: function ( _remoteUsersIds, photosImportSourceId, jsonRPC ) {
			remoteUserModel.registerRemoteUsers( _remoteUsersIds );
			return remoteUserModel.renderRemoteUsers( jsonRPC, photosImportSourceId );
		}
	};
} );
