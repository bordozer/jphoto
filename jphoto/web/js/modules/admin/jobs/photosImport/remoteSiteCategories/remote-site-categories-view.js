define( ["backbone", "jquery", "underscore"
		], function ( Backbone, $, _ ) {

	'use strict';

	var RemoteSiteCategoriesView = Backbone.View.extend( {

		initialize: function() {
			this.$el.html( '' );

			this.listenTo( this.model, "add", this.render );
			this.model.fetch( { cache: false } );
		},

		render: function ( category ) {
			var container = $( "<div style='display: inline-block; width: 200px;'></div>" );

			container.append( "<input name='remotePhotoSiteCategories' checked='" + ( category.get( 'checked' ) ? 'checked' : '' ) + "' type='checkbox' value='" + category.get( 'remotePhotoSiteCategoryId' ) + "'>" );
			container.append( category.get( 'remotePhotoSiteCategoryName' ) );

			this.$el.append( container );
		}
	} );

	return { RemoteSiteCategoriesView:RemoteSiteCategoriesView };
} );