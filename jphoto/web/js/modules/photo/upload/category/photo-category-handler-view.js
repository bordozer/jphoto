define( ["backbone", "jquery", "underscore"
		 , "text!modules/photo/upload/category/templates/photo-category-template.html"
		], function ( Backbone, $, _, template ) {

	'use strict';

	var PhotoCategoryHandlerView = Backbone.View.extend( {

		template:_.template( template ),

		events: {
			"change .photo-data-category-id": "onCategoryChange"
		},

		initialize: function() {
			this.listenTo( this.model, "sync", this.render );
//			this.listenTo( this.model, "change", this.render );
		},

		render: function() {

			var model = this.model;
			var modelJSON = model.toJSON();

			var div = $( "<div style='display: inline; width: 30%;'></div>" );
			div.append( this.template( modelJSON ) );

			var nudeContentView = new NudeContentHandlerView( {
				model: model
			} );
			div.append( nudeContentView.renderNudeContent().$el.html() );

			this.$el.html( div );
		},

		categoryChange: function( categoryId ) {
			this.bindModel( categoryId );
			this.model.save();
		},

		bindModel: function( categoryId ) {
			this.model.set( { selectedCategoryId: categoryId } );
		},

		onCategoryChange: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.categoryChange( $( "option:selected", evt.target ).val() );
		}
	});

	var NudeContentHandlerView = Backbone.View.extend( {

		events: {
			"change #containsNudeContent": "onNudeContentChange"
		},

		renderNudeContent: function () {
			var div = $( "<div style='float: left; width: 100%;'></div>" );
			div.append( "Nude content:" ); // TODO: translate
			div.append( "<br />" );

			var modelJSON = this.model.toJSON()[ 'nudeContentDTO' ];

			var genreCanContainsNude = modelJSON[ 'genreCanContainsNude' ];
			var genreObviouslyContainsNude = modelJSON[ 'genreObviouslyContainsNude' ];
			var photoContainsNude = modelJSON[ 'photoContainsNude' ];

			if ( ! genreCanContainsNude ) {
				div.append( "<input type='hidden' name='containsNudeContent' value='false' >" );
				div.append( modelJSON[ 'noTranslated' ] );
				this.$el.html( div );
				return this;
			}

			if ( genreObviouslyContainsNude ) {
				div.append( modelJSON[ 'yesTranslated' ] );
				this.$el.html( div );
				return this;
			}

			div.append( "<input type='checkbox' id='containsNudeContent' name='containsNudeContent' value='true' " + ( photoContainsNude ? "checked='checked'" : "" ) + " >" );
			this.$el.html( div );

			return this;
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
			this.model.set( { photoContainsNude: control.prop( 'checked' ) }, { silent: true } );
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

	return { PhotoCategoryHandlerView:PhotoCategoryHandlerView };
} );
