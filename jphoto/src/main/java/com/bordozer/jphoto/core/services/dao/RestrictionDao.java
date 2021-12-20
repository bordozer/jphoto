package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.enums.RestrictionType;
import com.bordozer.jphoto.core.general.restriction.EntryRestriction;

import java.util.List;

public interface RestrictionDao extends BaseEntityDao<EntryRestriction> {

    List<EntryRestriction> loadRestrictions(final int entryId, final RestrictionType restrictionType);

    List<EntryRestriction> loadAll();

    List<EntryRestriction> load(final List<RestrictionType> defaultTypes);
}
