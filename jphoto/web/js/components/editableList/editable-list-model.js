define( [ "backbone" ], function ( Backbone ) {

	var EditableListEntryModel = Backbone.Model.extend( {

		idAttribute: 'entryId',

		openInfo: false
		, openEditor: false
		, hasUnsavedChanged: false
		, translationDTO: []

		, defaults: function() {
			return {
				openEditor: false
			};
		}
	});

	var EditableListModel = Backbone.Collection.extend( {

		userTeamMemberTypes: [],
		translationDTO: [],

		initialize: function ( options ) {
			this.translationDTO = options.translationDTO;
			this.groupSelectionClass = options.groupSelectionClass;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EditableListModel:EditableListModel, EditableListEntryModel: EditableListEntryModel };
} );