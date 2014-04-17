define( ["backbone"], function ( Backbone ) {

	var UserLockHistoryEntryModel = Backbone.Model.extend( {

	});

	var UserLockHistoryModel = Backbone.Model.extend( {

		model: UserLockHistoryEntryModel,

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/admin/members/" + options.userId + "/lock/history/";
		}

	 } );

	return { UserLockHistoryEntryModel:UserLockHistoryEntryModel, UserLockHistoryModel:UserLockHistoryModel};
} );
