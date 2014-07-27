define( ["backbone", "jquery", "underscore"
		], function ( Backbone, $, _ ) {

	'use strict';

	var RemoteSiteCategoriesView = Backbone.View.extend( {

		initialize: function() {
			this.listenTo( this.model, "add", this.render );
			this.model.fetch( {cache: false} );
		},

		render: function ( category ) {
//			var container = $( "<div style='display: inline-block; width: 120px;'></div>" );
			var el = $( "<input type='checkbox' value='" + category.get( 'remotePhotoSiteCategoryId' ) + "'> " + category.get( 'remotePhotoSiteCategoryName' ) );
			this.$el.append( el );
		}
	} );

	return { RemoteSiteCategoriesView:RemoteSiteCategoriesView };
} );