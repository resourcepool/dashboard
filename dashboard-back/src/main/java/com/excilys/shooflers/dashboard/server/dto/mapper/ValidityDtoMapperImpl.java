package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.model.Validity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ValidityDtoMapperImpl implements ValidityDtoMapper {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public ValidityDto toDto(Validity model) {
        return model != null ? new ValidityDto(model.getStart().format(FORMATTER), model.getEnd() == null ? null : model.getEnd().format(FORMATTER)) : null;
    }

    @Override
    public Validity fromDto(ValidityDto dto) {
        return dto != null && (dto.getStart() != null || dto.getEnd() != null) ? new Validity.Builder()
                .start(dto.getStart() == null ? null : LocalDateTime.parse(dto.getStart(), FORMATTER))
                .end(dto.getEnd() == null ? null : LocalDateTime.parse(dto.getEnd(), FORMATTER))
                .build() : null;
    }
}