define( ["backbone", "jquery", "underscore", 'jquery_ui'
		, "components/user-picker/user-picker-model"
		, "text!components/user-picker/template/template.html"
		], function ( Backbone, $, _, ui, Model, Template ) {

	'use strict';

	var UserPickerView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
			this.model.fetch( { cache: false } );
		},

		render: function () {
			var selector = '.user-picker-filter';

			var model = this.model;
			var modelJSON = this.model.toJSON();

			var userPickerDTOs = this.model.get( 'userDTOs' );
//			console.log( userPickerDTOs );

			var obj = $( selector ).autocomplete( {
				width: 300,
				max: 10,
				delay: 100,
				minLength: 0, // TODO: set min
				autoFocus: true,
				cacheLength: 1,
				scroll: true,
				highlight: false,
				source: function ( request, response ) {
					var searchString = $( selector ).val();
					console.log( 'search string: ', searchString );

					response( $.map( userPickerDTOs, function ( item ) {
						var img = "<img src='" + item.userAvatarUrl + "' />";
						return {
							label: item.userName + ", " + item.userGender,
							value: item.userName,
							userId: item.userId,
							userCardLink: item.userCardLink,
							userAvatarUrl: item.userAvatarUrl,
							userNameEscaped: item.userNameEscaped,
							userName: item.userName
						   }
					   } ) )
				   },
				select: function ( event, ui ) {
					console.log( 'Selected: ', $( selector ).val( ui.item.userId ) );
					/*$( userIdControl ).val( ui.item.userId );
					$( '#foundMemberCardLinkDiv' ).html( '${foundMemberText} ' + ui.item.userCardLink );
					$( '#foundMemberResetDiv' ).html( "<img src=\"${resetFoundMemberImg}\" alt=\"${resetFoundMemberHint}\"  title=\"${resetFoundMemberHint}\" width=\"16\" height=\"16\" onclick=\"resetFoundMember();\" />" );
					$( '#memberSearchAvatar' ).attr( 'src', ui.item.userAvatarUrl );
					if( model.get( 'callback' ) ) {
						callback( ui.item );
					}*/
				   }
			} ).data( "ui-autocomplete" );

			obj._renderItem = function ( ul, item ) {
						return $( "<li></li>" )
								.data( "ui-autocomplete-item", item )
								.append( "<a>" + "<img src='" + item.userAvatarUrl + "' height='70' /> " + item.userNameEscaped + "</a>" ).appendTo( ul );
					};

			$( selector ).bind( 'focus', function () {
				$( this ).autocomplete( "search" );
			} );
		}
	});

	return { UserPickerView: UserPickerView };
});