package com.excilys.shooflers.dashboard.server.validator;

import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Validator of an {@link ValidityDto}
 *
 * @author Mickael
 */
@Component
public class ValidityDtoValidatorImpl implements Validator<ValidityDto> {
    @Override
    public void validate(ValidityDto validityDto) {
        if (StringUtils.isBlank(validityDto.getStart()) && StringUtils.isBlank(validityDto.getEnd())) {
            return;
        }
        
        LocalDateTime startLocalDateTime;
        LocalDateTime endLocalDateTime;
        
        if (StringUtils.isBlank(validityDto.getStart())) {
            startLocalDateTime = LocalDateTime.now();
        } else {
            startLocalDateTime = LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(validityDto.getStart()));
        }
        if (StringUtils.isNotBlank(validityDto.getEnd())) {
            endLocalDateTime = LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(validityDto.getEnd()));
            if (startLocalDateTime.isAfter(endLocalDateTime)) {
                throw new ValidationException("A startDateTime can't be anterior to endDateTime");
            }
        }
    }
}
