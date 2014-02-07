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
													   return "http://192.168.69.77:8083/jphoto/app/activityStream/mobile/";
												   }
											   } );

	return { Todo:Todo, TodoList:TodoList };
} );
