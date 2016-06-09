package com.excilys.shoofleurs.dashboard.rest.dtos.mappers;

import com.excilys.shoofleurs.dashboard.model.entities.Validity;
import com.excilys.shoofleurs.dashboard.rest.dtos.ValidityDto;

/**
 * Created by excilys on 09/06/16.
 */
public class ValidityDtoMapper {
    public static Validity toValidity(ValidityDto validityDto) {
        Validity validity = new Validity();
        validity.setEndTime(validityDto.getEndTime());
        validity.setStartTime(validityDto.getStartTime());
        return validity;
    }
}
