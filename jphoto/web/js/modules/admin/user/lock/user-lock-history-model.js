define( ["backbone"], function ( Backbone ) {

	var UserLockHistoryEntryModel = Backbone.Model.extend( {

	});

	var UserLockHistoryModel = Backbone.Collection.extend( {

		model: UserLockHistoryEntryModel,

		initialize:function ( options ) {
			this.url = options.baseUrl + "/rest/admin/members/" + options.userId + "/lock/history/";
		}

	 } );

	return { UserLockHistoryEntryModel:UserLockHistoryEntryModel, UserLockHistoryModel:UserLockHistoryModel};
} );
