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
		},

		render: function() {

			var model = this.model;
			var modelJSON = model.toJSON();

			var div = $( "<div style='float:left; width: 100%;'></div>" );
			div.append( this.template( modelJSON ) );

			if ( model.get( 'photoId' ) == 0 ) {
				var uploadAllowanceHandlerView = new UploadAllowanceHandlerView( {
					model: model
				} );
				div.append( uploadAllowanceHandlerView.renderPhotoAllowance().$el );
			}

			if ( model.get( 'selectedCategoryId' ) > 0 ) {
				var nudeContentView = new NudeContentHandlerView( {
					model: model
				} );
				div.append( nudeContentView.renderNudeContent().$el );
			}

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

	var UploadAllowanceHandlerView = Backbone.View.extend( {

		renderPhotoAllowance: function () {

			var div = $( "<div style='float:left; width: 300px; vertical-align: top;'></div>" );

			var modelJSON = this.model.toJSON();

			_.each( modelJSON[ 'photoUploadDescriptions' ], function( allowance ) {

				var paragraphDiv = $( "<div style='float: left; width: 99%; margin-bottom: 10px;'></div>" );

				if ( ! allowance[ 'passed' ] ) {
					paragraphDiv.addClass( 'redfont' );
				}

				paragraphDiv.html( '&nbsp;&nbsp;' + allowance[ 'uploadRuleDescription' ] );

				div.append( paragraphDiv );
			});

			this.$el.html( div );

			return this;
		}
	});

	var NudeContentHandlerView = Backbone.View.extend( {

		events: {
			"change .contains-nude-content-checkbox": "onNudeContentChange"
		},

		renderNudeContent: function () {

			var div = $( "<div style='float: left; width: 100%;'></div>" );

			div.append( '<hr />' );
			div.append( "<b>Nude content:</b>" ); // TODO: translate
			div.append( "<br />" );

			var modelJSON = this.model.toJSON()[ 'nudeContentDTO' ];

			var genreCanContainsNude = modelJSON[ 'genreCanContainsNude' ];
			var genreObviouslyContainsNude = modelJSON[ 'genreObviouslyContainsNude' ];
			var photoContainsNude = this.model.get( 'photoContainsNude' );

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

			div.append( "<input type='hidden' id='_containsNudeContent' name='_containsNudeContent' value='true' >" );
			div.append( "<input type='checkbox' class='contains-nude-content-checkbox' name='containsNudeContent' value='true' " + ( photoContainsNude ? "checked='checked'" : "" ) + " >" );
			this.$el.html( div );

			return this;
		},

		nudeContentChange: function( control ) {
			var isNudeContent = control.prop( 'checked' );
			this.model.set( { photoContainsNude: isNudeContent }, { silent: true } );
			this.$( '#_containsNudeContent' ).val( isNudeContent );
		},

		onNudeContentChange: function( evt ) {
			evt.preventDefault();
			evt.stopImmediatePropagation();

			this.nudeContentChange( $( evt.target ) );
		}
	});

	return { PhotoCategoryHandlerView:PhotoCategoryHandlerView };
} );
