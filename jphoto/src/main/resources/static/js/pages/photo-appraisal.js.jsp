<%@ page import="com.bordozer.jphoto.ui.context.ApplicationContextHelper"%>
		<%@ page import="com.bordozer.jphoto.core.general.configuration.ConfigurationKey"%>
		<%@ page contentType="text/javascript" %>

<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="votingMarkQty" value="<%=ApplicationContextHelper.getConfigurationService().getInt( ConfigurationKey.PHOTO_VOTING_APPRAISAL_CATEGORIES_COUNT )%>"/>

define( [ 'jquery' ], function ( $ ) {

	var votingCategoryPreviousValues = [];

	function setAccessToVotingCategoryMark( number ) {
		var id = 'votingCategory' + number;
		var votingCategory = $( '#' + id );
		var votingCategoryMark = $( '#votingCategoryMark' + number );
		var votingCategoryMarkContainer = $( '#votingCategoryMarkContainer' + number );

		if ( votingCategory.val() == 0 ) {
			hideVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer );
			votingCategoryPreviousValues[ number ] = votingCategory.val();
			return;
		}

		var hasErrors = false;

		$( ".votingCategoryClass" ).each( function () {
			var element = $( this );
			if ( element.attr( 'id' ) != id && votingCategory.val() != 0 && element.val() == votingCategory.val() ) {
				var optionText = $( "option[value='" + votingCategory.val() + "']", votingCategory ).text();
				showUIMessage_Warning( "'" + optionText + "' ${eco:translate('is duplicated value')}" );
				hasErrors = true;
			}
		} );

		if ( !hasErrors ) {
			showVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer, number, votingCategory );
		} else {
			hideVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer );
			resetVotingCategoryToPreviousValue( votingCategory, number );
		}
	}

	function resetVotingCategoryToPreviousValue( votingCategory, number ) {
		votingCategory.val( votingCategoryPreviousValues[ number ] );
		setAccessToVotingCategoryMark( number );
	}

	function showVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer, number, votingCategory ) {
		votingCategoryMarkContainer.html( '' );
		votingCategoryMark.show();
		//				votingCategoryMark.focus(); // Do not set the focus or Opera jumps to the control otherwise
		votingCategoryPreviousValues[ number ] = votingCategory.val();
	}

	function hideVotingCategoryMark( votingCategoryMark, votingCategoryMarkContainer ) {
		votingCategoryMark.hide();
		votingCategoryMarkContainer.html( '<b>X</b>' );
	}

	function voteAndLoadResults( url ) {
		$.ajax( {
					type: 'POST',
					url: url,
					data: $( '#frmPhotoVoting' ).serialize(),
					success: function ( response ) {
						$( '#photoVotingDiv' ).html( response ); // response == voting/PhotoVoting.jsp

					},
					error: function () {
						showUIMessage_Error( '${eco:translate('Voting error')}' );
					}
				} ).done( function () {
			refreshPhotoInfo();
			noty( {
					  text: '${eco:translate('Your marks have been saved')}', type: 'success', layout: 'bottomRight', timeout: 3000
				  } );
		} );
	}

	function validateVotingCategories() {
		var hasErrors = true;

		for ( var i = 1; i <= ${votingMarkQty}; i++ ) {
			hasErrors = !doesVotingCategoryHaveValue( i );
			if ( !hasErrors ) {
				break;
			}
		}
		return hasErrors;
	}

	function doesVotingCategoryHaveValue( number ) {
		var id = 'votingCategory' + number;
		var votingCategory = $( '#' + id );
		var votingCategoryMark = $( '#votingCategoryMark' + number );

		return votingCategory.val() != 0; // || ( votingCategory.val() != 0 && votingCategoryMark.val == 0 );
	}

	return {

		initVotingCategoryValue: function ( votingCategories, i ) {
			var categoryValue = votingCategories[ i - 1 ];
			$( '#votingCategory' + i ).val( categoryValue );
			votingCategoryPreviousValues[ i ] = categoryValue;
		},

		setAccessToVotingCategoryMark: function( number ) {
			setAccessToVotingCategoryMark( number );
		},

		submitVotingForm: function( url ) {

			var hasErrors = validateVotingCategories();
			if ( hasErrors ) {
				showUIMessage_Warning( '${eco:translate('Please, select at least one category')}' );
				return false;
			}

			voteAndLoadResults( url );

			return false;
		}
	}
} );
