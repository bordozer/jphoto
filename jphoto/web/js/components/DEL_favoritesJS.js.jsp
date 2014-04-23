<%@ page contentType="text/javascript" %>
<%@ page import="core.enums.FavoriteEntryType" %>
<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="favoriteImagesFolder" value="<%=FavoriteEntryType.FAVORITES_IMAGE_FOLDER%>"/>

define( [ 'jquery' ], function ( $ ) {

	function FavoriteEntry( userId, favoriteTypeId, favoriteEntryId, favoriteEntryName, favoriteEntryAddIcon, favoriteEntryRemoveIcon, favoriteEntryTypeName, favoriteEntryAddText, favoriteEntryRemoveText ) {
		this.userId = userId;
		this.favoriteTypeId = favoriteTypeId;
		this.favoriteEntryId = favoriteEntryId;

		this.favoriteEntryName = favoriteEntryName;
		this.favoriteEntryAddIcon = favoriteEntryAddIcon;
		this.favoriteEntryRemoveIcon = favoriteEntryRemoveIcon;
		this.favoriteEntryTypeName = favoriteEntryTypeName;

		this.favoriteEntryAddText = favoriteEntryAddText;
		this.favoriteEntryRemoveText = favoriteEntryRemoveText;

		this.favoriteEntryIcons = [];

		this.isIdentical = function ( favoriteEntry ) {
			return this.favoriteTypeId == favoriteEntry.getFavoriteTypeId() && this.favoriteEntryId == favoriteEntry.getFavoriteEntryId();
		};

		this.equals = function ( favoriteEntry ) {
			if ( !favoriteEntry instanceof FavoriteEntry ) {
				return false;
			}
			return this.isIdentical( favoriteEntry );
		};

		this.getUserId = function () {
			return this.userId
		};

		this.getFavoriteTypeId = function () {
			return this.favoriteTypeId
		};

		this.getFavoriteEntryId = function () {
			return this.favoriteEntryId
		};

		this.addFavoriteEntryIcon = function ( favoriteIconId ) {
			return this.favoriteEntryIcons.push( favoriteIconId );
		};

		this.getFavoriteEntryIcons = function () {
			return this.favoriteEntryIcons;
		};

		this.getFavoriteEntryName = function () {
			return this.favoriteEntryName;
		};

		this.getFavoriteEntryAddIcon = function () {
			return this.favoriteEntryAddIcon;
		};

		this.getFavoriteEntryRemoveIcon = function () {
			return this.favoriteEntryRemoveIcon;
		};

		this.getFavoriteEntryTypeName = function () {
			return this.favoriteEntryTypeName;
		};

		this.getFavoriteEntryAddText = function () {
			return this.favoriteEntryAddText;
		};

		this.getFavoriteEntryRemoveText = function () {
			return this.favoriteEntryRemoveText;
		};
	}

	var favoriteEntryModel = function () {

		var favoriteEntries = [];

		function findFavoriteEntry( favoriteEntry ) {
			for ( var index = 0; index < favoriteEntries.length; index++ ) {
				var entry = favoriteEntries[ index ];
				if ( entry.equals( favoriteEntry ) ) {
					return entry;
				}
			}
			return null;
		}

		function isFavoriteEntryRegistered( favoriteEntry ) {
			return findFavoriteEntry( favoriteEntry ) != null;
		}

		function registerFavoriteEntry( favoriteEntry ) {
			favoriteEntries.push( favoriteEntry );
		}

		function processFavoriteEntryAction( favoriteEntry, isAddingFuncCalled ) {

			var addFunc = function () {
				favoriteEntryModel.addEntryToFavorites( favoriteEntry )
			};

			var removeFunc = function () {
				favoriteEntryModel.removeEntryToFavorites( favoriteEntry );
			};

			var entry = findFavoriteEntry( favoriteEntry );
			var favoriteEntryIcons = entry.getFavoriteEntryIcons();

			var isEntryInFavorites = jsonRPC.ajaxService.isEntryInFavoritesAjax( favoriteEntry.getUserId(), favoriteEntry.getFavoriteEntryId(), favoriteEntry.getFavoriteTypeId() );

			var isValid = validateAndShowErrorMessage( favoriteEntry, isEntryInFavorites, isAddingFuncCalled );


			if ( isValid ) {
				var isSavedSuccessfully = saveToDB( isEntryInFavorites, favoriteEntry );
				if ( isSavedSuccessfully ) {
					isEntryInFavorites = !isEntryInFavorites;

					var m1 = ' ${eco:translate('add/remove bookmark: has been added to')} ';
					if ( !isEntryInFavorites ) {
						m1 = ' ${eco:translate('add/remove bookmark: has been removed from')} ';
					}
					var message = favoriteEntry.getFavoriteEntryName() + m1 + favoriteEntry.getFavoriteEntryTypeName();

					notifySuccessMessage( message );
				}
			}

			$( favoriteEntryIcons ).each( function () {
				refreshFavoriteIcon( favoriteEntry, isEntryInFavorites, this );
				rebindOnClick( isEntryInFavorites, this, addFunc, removeFunc );
			} );
		}

		return {
			registerFavoriteEntry: function ( favoriteEntry ) {
				if ( !isFavoriteEntryRegistered( favoriteEntry ) ) {
					registerFavoriteEntry( favoriteEntry );
				}
			},

			registerFavoriteIcon: function ( favoriteEntry, favoriteIconId ) {
				var entry = findFavoriteEntry( favoriteEntry );
				entry.addFavoriteEntryIcon( favoriteIconId )
			},

			addEntryToFavorites: function ( favoriteEntry ) {
				processFavoriteEntryAction( favoriteEntry, true );
			},

			removeEntryToFavorites: function ( favoriteEntry ) {
				processFavoriteEntryAction( favoriteEntry, false );
			}
		}

	}();

	function saveToDB( isEntryInFavorites, favoriteEntry ) {
		var ajaxResultDTO;
		if ( isEntryInFavorites ) {
			ajaxResultDTO = jsonRPC.ajaxService.removeEntryFromFavoritesAjax( favoriteEntry.getUserId(), favoriteEntry.getFavoriteEntryId(), favoriteEntry.getFavoriteTypeId() );
		} else {
			ajaxResultDTO = jsonRPC.ajaxService.addEntryToFavoritesAjax( favoriteEntry.getUserId(), favoriteEntry.getFavoriteEntryId(), favoriteEntry.getFavoriteTypeId() );
		}

		if ( !ajaxResultDTO.successful ) {
			showErrorMessage( ajaxResultDTO.message );
			return false;
		}

		return true;
	}

	function validateAndShowErrorMessage( favoriteEntry, isEntryInFavorites, isAddingFuncCalled ) {

		if ( isAddingFuncCalled && isEntryInFavorites ) {
			showWarningMessage( "'" + favoriteEntry.getFavoriteEntryName() + "' ${eco:translate("add/remove bookmark: is already in")} " + favoriteEntry.getFavoriteEntryTypeName() );
			return false;
		}

		var isRemoving = !isAddingFuncCalled;
		if ( isRemoving && !isEntryInFavorites ) {
			showWarningMessage( "'" + favoriteEntry.getFavoriteEntryName() + "' ${eco:translate("add/remove bookmark: is NOT in")} " + favoriteEntry.getFavoriteEntryTypeName() );
			return false;
		}

		return true;
	}

	function rebindOnClick( isEntryInFavorites, favoriteIconId, addFunc, removeFunc ) {
		var selector = '#' + favoriteIconId;

		if ( isEntryInFavorites ) {
			$( selector ).unbind( "click" );
			$( selector ).bind( "click", removeFunc );
		} else {
			$( selector ).unbind( "click" );
			$( selector ).bind( "click", addFunc );
		}
	}

	function refreshFavoriteIcon( favoriteEntry, isEntryInFavorites, favoriteIconId ) {
		var image;

		var operation = "";
		if ( isEntryInFavorites ) {
			image = favoriteEntry.getFavoriteEntryRemoveIcon();
			operation = favoriteEntry.getFavoriteEntryRemoveText();
		} else {
			image = favoriteEntry.getFavoriteEntryAddIcon();
			operation = favoriteEntry.getFavoriteEntryAddText();
		}

		var imagePath = "${eco:imageFolderURL()}/${favoriteImagesFolder}/" + image;
		var title = favoriteEntry.getFavoriteEntryName() + ": " + operation;

		var selector = '#' + favoriteIconId;
		$( selector ).attr( 'src', imagePath );
		$( selector ).attr( 'alt', title );
		$( selector ).attr( 'title', title );
	}

	function registerFavoriteEntry( currentUserId, entryTypeId, favoriteEntryId, entryName, addIcon, removeIcon, entryTypeName, addText, removeText, favoriteIconId ) {
		var favoriteEntry = new FavoriteEntry( currentUserId, entryTypeId, favoriteEntryId, entryName, addIcon, removeIcon, entryTypeName, addText, removeText );

		favoriteEntryModel.registerFavoriteEntry( favoriteEntry );
		favoriteEntryModel.registerFavoriteIcon( favoriteEntry, favoriteIconId );

		return favoriteEntry;
	}
});