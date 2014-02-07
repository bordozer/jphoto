define( ["backbone", "jquery", "underscore", "activity/model"], function ( Backbone, $, _, Model ) {

	var TodoView = Backbone.View.extend( {

											 tagName:"li",
											 template:_.template( $( "#todo-template" ).html() ),

											 events:{
												 "click .toggle":"toggleDone"
											 },

											 initialize:function () {
												 this.listenTo( this.model, "change", this.render );
											 },

											 render:function () {
												 this.$el.html( this.template( this.model.toJSON() ) );
												 this.$el.toggleClass( "done", this.model.get( "done" ) );

												 return this;
											 },

											 toggleDone:function () {
												 this.model.toggle();
											 }
										 } );

	var TodoListView = Backbone.View.extend( {
												 el:"#todos",
												 events:{
													 "keypress .new-todo":"createNewItem"
												 },
												 initialize:function () {
													 this.newItemTitleInput = this.$( ".new-todo" );
													 this.itemList = this.$( ".todolist" );

													 this.listenTo( this.model, 'all', this.render );

													 this.model.fetch();
												 },
												 render:function () {
													 this.itemList.empty();
													 this.model.each( this.showSingleTodoItem, this );
												 },
												 showSingleTodoItem:function ( todo ) {
													 this.itemList.prepend( new TodoView( {model:todo} ).render().$el );
												 },
												 createNewItem:function ( e ) {
													 if ( e.keyCode != 13 ) {
														 return;
													 }
													 if ( !this.newItemTitleInput.val() ) {
														 return;
													 }

													 this.model.create( {title:this.newItemTitleInput.val()} );
													 this.newItemTitleInput.val( '' );
												 }
											 } );

	return { TodoView:TodoView, TodoListView:TodoListView }
} );
