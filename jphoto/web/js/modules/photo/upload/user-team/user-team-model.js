define( ["backbone"], function ( Backbone ) {

	var UserTeamModel = Backbone.Collection.extend( {

		initialize:function ( options ) {
			this.url = options.baseUrl + "/json/user/" + options.userId + "/team/";
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	var UserTeamMemberModel = Backbone.Model.extend( {

		idAttribute: 'userId'

	});

	return { UserTeamModel:UserTeamModel, UserTeamMemberModel: UserTeamMemberModel };
} );