define( [ "backbone" ], function ( Backbone ) {

	var EditableListEntryModel = Backbone.Model.extend( {

		idAttribute: 'entryId',

		openInfo: false
		, openEditor: false
		, hasUnsavedChanged: false
		, translationDTO: []

		, defaults: function() {
			return {
				userId: 0
				, openEditor: false
			};
		}
	});

	var EditableListModel = Backbone.Collection.extend( {

		userId: 0,
		userTeamMemberTypes: [],
		translationDTO: [],

		initialize: function ( options ) {
			this.userId = options.userId;

			this.selectedIds = options.selectedIds;
			this.groupSelectionClass = options.groupSelectionClass;
			this.translationDTO = options.translationDTO;
		},

		refresh: function() {
			this.fetch( { reset: true, cache: false } );
		}
	});

	return { EditableListModel:EditableListModel, EditableListEntryModel: EditableListEntryModel };
} );