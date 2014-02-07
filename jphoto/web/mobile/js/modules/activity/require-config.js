require( ["../../require-base-config"], function ( config ) {
	require( ["activity/view", "activity/model", "jquery"], function ( View, Model, $ ) {

		var model = new Model.Activities();
		model.fetch();

		var view = new View.ActivitiesView( {model:model} );
		$( "body" ).append( view.$el ); // view.$el - base jquery element
		view.render();

		console.log( model );
	} );
} );
