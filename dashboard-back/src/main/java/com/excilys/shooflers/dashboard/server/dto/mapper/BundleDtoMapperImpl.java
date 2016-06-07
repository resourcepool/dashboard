package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dto.ValidityDto;
import com.excilys.shooflers.dashboard.server.model.Validity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BundleDtoMapperImpl implements MapperDto<Validity, ValidityDto> {



    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

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
