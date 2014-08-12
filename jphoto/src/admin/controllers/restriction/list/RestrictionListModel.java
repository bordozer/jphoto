package admin.controllers.restriction.list;

import core.general.base.AbstractGeneralPageModel;
import ui.translatable.GenericTranslatableEntry;

import java.util.List;

public class RestrictionListModel extends AbstractGeneralPageModel {

	private List<GenericTranslatableEntry> restrictions;

	public void setRestrictions( final List<GenericTranslatableEntry> restrictions ) {
		this.restrictions = restrictions;
	}

	public List<GenericTranslatableEntry> getRestrictions() {
		return restrictions;
	}
}
