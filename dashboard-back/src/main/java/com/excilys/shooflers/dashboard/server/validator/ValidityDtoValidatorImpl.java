package com.excilys.shooflers.dashboard.server.validator;

import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Mickael
 */
@Component
public class ValidityDtoValidatorImpl implements Validator<ValidityDto> {
    @Override
    public void validate(ValidityDto validityDto) {
        // At this point, we supposed the date are well formed

        if (validityDto == null || validityDto.getEnd() == null) {
            return;
        }

        LocalDateTime startLocalDateTime;
        LocalDateTime endLocalDateTime;
        if (validityDto.getStart() == null && validityDto.getEnd() != null) {
            startLocalDateTime = LocalDateTime.now();
        } else {
            startLocalDateTime = LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(validityDto.getStart()));
        }

        endLocalDateTime = LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(validityDto.getEnd()));

        if (startLocalDateTime.isAfter(endLocalDateTime)) {
            throw new ValidationException("A startDateTime can't be anterior to endDateTime");
        }

    }
}
