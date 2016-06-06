package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.model.Validity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ValidityDtoMapperImpl implements MapperDto<Validity, ValidityDto> {

    private static final String PATTERN = "yyyy-MM-dd hh:mm";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    @Override
    public ValidityDto toDto(Validity model) {
        return new ValidityDto(model.getStart().format(FORMATTER), model.getEnd().format(FORMATTER));
    }

    @Override
    public Validity fromDto(ValidityDto dto) {
        return new Validity.Builder()
                .start(LocalDateTime.parse(dto.getStart(), FORMATTER))
                .end(LocalDateTime.parse(dto.getEnd(), FORMATTER))
                .build();
    }
}
