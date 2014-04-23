define( [ 'jquery' ], function ( $ ) {

	var MassCheckerModel = function ( checkboxClass, imagePath ) {

		var selection = false;
		var checkerIcon = $( "#mass-selector-icon-" + checkboxClass );

		function setSelectedCheckerIcon () {
			checkerIcon.attr( 'src', imagePath + '/icons16/checkAll.png' );
		}

		function setUnselectedCheckerIcon () {
			checkerIcon.attr( 'src', imagePath + '/icons16/uncheckAll.png' );
		}

		function bindSelectAll() {
			checkerIcon.click( function() {
				checkerIcon.unbind();
				setSelection( true );
			});
		}

		function bindDeselectAll() {
			checkerIcon.click( function() {
				checkerIcon.unbind();
				setSelection( false );
			});
		}

		function selectAllCheckboxes() {
			getElements().prop( "checked", "checked" );
		}

		function deselectAllCheckboxes() {
			getElements().removeAttr( "checked" );
		}

		function setSelection( _selection, _suppressMassSelection ) {
			selection = _selection;

			if ( selection ) {
				setSelectedCheckerIcon();
				bindDeselectAll();
				if ( ! _suppressMassSelection ) {
					selectAllCheckboxes();
				}
			}

			if ( ! selection ) {
				setUnselectedCheckerIcon();
				bindSelectAll();
				if ( ! _suppressMassSelection ) {
					deselectAllCheckboxes();
				}
			}
		}

		function getElements() {
			return $( '[name*=' + checkboxClass + ']' );
		}

		return {
			getCheckboxClass: function() {
				return checkboxClass;
			},

			getElements: function() {
				return getElements();
			},

			setSelection: function( _selection, _suppressMassSelection ) {
				setSelection( _selection, _suppressMassSelection );
			}
		}
	};




	var massChecker = function() {

		var checkers = [];

		function findChecker( checkboxClass ) {
			for ( var index = 0; index < checkers.length; index++ ) {
				var checker = checkers[ index ];
				if ( checker.getCheckboxClass() == checkboxClass ) {
					return checker;
				}
			}
			return null;
		}

		function registerChecker( checkboxClass ) {
			checkers.push( checkboxClass );
		}

		return {

			registerSelected: function( checkboxClass, imagePath ) {
				var checker = new MassCheckerModel( checkboxClass, imagePath );
				checker.setSelection( true, true );
				registerChecker( checker );
			},

			registerUnselected: function( checkboxClass, imagePath ) {
				var checker = new MassCheckerModel( checkboxClass, imagePath );
				checker.setSelection( false, true );
				registerChecker( checker );
			}
		}
	}();




	return {
		getMassChecker: function() {
			return massChecker;
		}
	}
});