define( ["backbone"], function ( Backbone ) {

	var Todo = Backbone.Model.extend( {
										  defaults:function () {
											  return {
												  title:"empty todo...",
												  done:false
											  }
										  },
										  toggle:function () {
											  this.save( {done:!this.get( "done" )} );
										  }
									  } );

	var TodoList = Backbone.Collection.extend( {
												   model:Todo,
												   url:function () {
													   return "http://localhost:8080/rest/" + "todos";
												   }
											   } );

	return { Todo:Todo, TodoList:TodoList };
} );
