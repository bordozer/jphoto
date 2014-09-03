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
			this.groupSelectionClass = options.groupSelectionClass;
			this.translationDTO = options.translationDTO;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EditableListModel:EditableListModel, EditableListEntryModel: EditableListEntryModel };
} );