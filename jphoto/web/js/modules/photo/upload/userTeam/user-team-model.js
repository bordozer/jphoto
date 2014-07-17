define( ["backbone"], function ( Backbone ) {

	var UserTeamMemberModel = Backbone.Model.extend( {

		idAttribute: 'userTeamMemberId',
		openInfo: false,
		openEditor: false,
		hasUnsavedChanged: false
	});

	var UserTeamModel = Backbone.Collection.extend( {

		model: UserTeamMemberModel,

		initialize: function ( options ) {
			this.url = options.baseUrl + "/rest/users/" + options.userId + "/team/";
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	return { UserTeamModel:UserTeamModel, UserTeamMemberModel: UserTeamMemberModel };
} );