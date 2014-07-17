define( ["backbone"], function ( Backbone ) {

	var UserTeamMemberModel = Backbone.Model.extend( {

		idAttribute: 'userTeamMemberId',
		openForEdit: false,

		isOpenForEdit: function() {
			return this.openForEdit;
		}
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