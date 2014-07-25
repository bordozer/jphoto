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

		function registerRemoteUser( remoteUserId ) {
			if ( !isRemoteUserRegistered( remoteUserId ) ) {
				remoteUsersIds.push( remoteUserId );
			}
		}

		function isRemoteUserRegistered( remoteUserId ) {
			return findRemoteUser( remoteUserId ) != null;
		}

		function findRemoteUser( remoteUserId ) {
			for ( var index = 0; index < remoteUsersIds.length; index++ ) {
				var entry = remoteUsersIds[ index ];
				if ( entry == remoteUserId ) {
					return entry;
				}
			}
			return null;
		}

		function getRemoteUsersIds( _remoteUserIds ) {
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

		function renderExistingRemoteUser( remoteUserDTO ) {

			var remoteUserName = remoteUserDTO.remoteUserName;
			var remoteUserCardUrl = remoteUserDTO.remoteUserCardUrl;

			var div = getRemoteUserInfoDiv();
			div.append( "#" + remoteUserDTO.remoteUserId + ": <a href=\"" + remoteUserCardUrl + "\" target=\"_blank\">" + remoteUserName + "</a>" + ", " + remoteUserDTO.remoteUserPhotosCount + " ${eco:translate('ROD PLURAL photos')}" );

			if ( remoteUserDTO.remoteUserExistsInTheSystem ) {
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

				var remoteUserIds = getRemoteUsersIds( ids );
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

					var remoteUserDTO = jsonRPC.ajaxService.getRemoteUserDTO( remoteUserId, photosImportSourceId );
					if ( remoteUserDTO.remoteUserFound ) {
						renderExistingRemoteUser( remoteUserDTO );
					} else {
						renderNotExistingRemotetUser( remoteUserId );
					}
					remoteUserInfoDiv.append( '<br />' );

					showRemoteUserInfoDiv();

					if ( remoteUserDTO.remoteUserExistsInTheSystem ) {
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
