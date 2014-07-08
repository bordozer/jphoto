define( ["backbone", "jquery", "underscore"
		], function ( Backbone, $, _ ) {

	'use strict';

	var PhotoUploadNudeContentView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
		},

		render:function () {
			var modelJSON = this.model.toJSON();

			var canContainsNude = modelJSON[ 'canContainsNude' ];
			var containsNude = modelJSON[ 'containsNude' ];

			if ( ! canContainsNude ) {
				this.$el.html( "<input type='hidden' name='containsNudeContent' value='false' >" );
				this.$el.html( modelJSON[ 'noTranslated' ] );
				return;
			}

			if ( containsNude ) {
				this.$el.html( modelJSON[ 'yesTranslated' ] );
				return;
			}

			this.$el.html( "<input type='checkbox' name='containsNudeContent' value='true' " + ( containsNude ? "checked='checked'" : "" ) + " >" );
		}
	});

	return { PhotoUploadNudeContentView:PhotoUploadNudeContentView };
} );
