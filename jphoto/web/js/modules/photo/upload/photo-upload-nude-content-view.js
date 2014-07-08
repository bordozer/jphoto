define( ["backbone", "jquery", "underscore"
		], function ( Backbone, $, _ ) {

	'use strict';

	var PhotoUploadNudeContentView = Backbone.View.extend( {

		events: {
			"change #containsNudeContent": "onNudeContentChange"
		},

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			var genreCanContainsNude = modelJSON[ 'genreCanContainsNude' ];
			var genreObviouslyContainsNude = modelJSON[ 'genreObviouslyContainsNude' ];
			var photoContainsNude = modelJSON[ 'photoContainsNude' ];

			if ( ! genreCanContainsNude ) {
				this.$el.html( "<input type='hidden' name='containsNudeContent' value='false' >" );
				this.$el.html( modelJSON[ 'noTranslated' ] );
				return;
			}

			if ( genreObviouslyContainsNude ) {
				this.$el.html( modelJSON[ 'yesTranslated' ] );
				return;
			}

			this.$el.html( "<input type='checkbox' id='containsNudeContent' name='containsNudeContent' value='true' " + ( photoContainsNude ? "checked='checked'" : "" ) + " >" );
		},

		nudeContentChange: function( control ) {
			this.bindModel( control );
			this.save();
		},

		onNudeContentChange: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.nudeContentChange( $( evt.target ) );
		},

		bindModel: function( control ) {
			this.model.set( { photoContainsNude: control.prop( 'checked' ) } );
		},

		save: function() {
			this.model.save()
				.done( _.bind( this.onSave, this ) )
				.fail( _.bind( this.onSaveError, this ) );
		},

		onSave: function() {},

		onSaveError: function() {
			showUIMessage_Error( 'Server update error' );
		}
	});

	return { PhotoUploadNudeContentView:PhotoUploadNudeContentView };
} );
