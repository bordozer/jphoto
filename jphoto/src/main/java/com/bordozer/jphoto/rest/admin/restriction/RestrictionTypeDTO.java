package com.bordozer.jphoto.rest.admin.restriction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestrictionTypeDTO {

    private final int id;
    private final String name;

    public RestrictionTypeDTO(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
