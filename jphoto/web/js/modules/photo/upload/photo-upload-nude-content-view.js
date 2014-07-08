define( ["backbone", "jquery", "underscore"
		], function ( Backbone, $, _ ) {

	'use strict';

	var PhotoUploadNudeContentView = Backbone.View.extend( {

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

			this.$el.html( "<input type='checkbox' name='containsNudeContent' value='true' " + ( photoContainsNude ? "checked='checked'" : "" ) + " >" );
		}
	});

	return { PhotoUploadNudeContentView:PhotoUploadNudeContentView };
} );
