define( ["backbone"], function ( Backbone ) {

	var UserTeamModel = Backbone.Collection.extend( {

		initialize:function ( options ) {
			this.url = options.baseUrl + "/rest/users/" + options.userId + "/team/";
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	var UserTeamMemberModel = Backbone.Model.extend( {

		idAttribute: 'userTeamMemberId'

	});

	return { UserTeamModel:UserTeamModel, UserTeamMemberModel: UserTeamMemberModel };
} );