package com.excilys.shoofleurs.dashboard.rest.dtos.mappers;

import com.excilys.shoofleurs.dashboard.model.entities.Validity;

/**
 * Created by excilys on 09/06/16.
 */
public class ValidityDtoMapper {
    public static Validity toValidity(com.excilys.shoofleurs.dashboard.rest.dtos.ValidityDto validityDto) {
        Validity validity = new Validity();
        validity.setEndTime(validityDto.getEnd());
        validity.setStartTime(validityDto.getStart());
        return validity;
    }
}
