define( ["backbone"], function ( Backbone ) {

	var UserLockModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/admin/members/" + options.userId + "/lock/";
		}

	 } );

	return {UserLockModel:UserLockModel};
} );
