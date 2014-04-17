define( ["backbone"], function ( Backbone ) {

	var UserLockHistoryModel = Backbone.Model.extend( {

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/admin/members/" + options.userId + "/lock/";
		}

	 } );

	return {UserLockHistoryModel:UserLockHistoryModel};
} );
