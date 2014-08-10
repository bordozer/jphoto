package core.services.dao;

import core.enums.RestrictionType;
import core.general.restriction.EntryRestriction;

import java.util.List;

public interface RestrictionDao extends BaseEntityDao<EntryRestriction> {

	List<EntryRestriction> loadRestrictions( final int entryId, final RestrictionType restrictionType );
}
