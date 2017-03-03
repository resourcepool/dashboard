package io.resourcepool.dashboard.rest.dtos.mappers;

import io.resourcepool.dashboard.model.entities.Validity;
import io.resourcepool.dashboard.rest.dtos.ValidityDto;


public class ValidityDtoMapper {
    public static Validity toValidity(ValidityDto validityDto) {
        if (validityDto == null) {
            return null;
        }

        Validity validity = new Validity();
        validity.setEndTime(validityDto.getEnd());
        validity.setStartTime(validityDto.getStart());
        return validity;
    }
}
