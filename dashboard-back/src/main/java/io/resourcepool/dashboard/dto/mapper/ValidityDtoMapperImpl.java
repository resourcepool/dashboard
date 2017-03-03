package io.resourcepool.dashboard.dto.mapper;

import io.resourcepool.dashboard.dto.ValidityDto;
import io.resourcepool.dashboard.model.Validity;
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
    return dto != null && ((notEmpty(dto.getStart()) || notEmpty(dto.getEnd()))) ? new Validity.Builder()
      .start(notEmpty(dto.getStart()) ? LocalDateTime.parse(dto.getStart().trim(), FORMATTER) : null)
      .end(notEmpty(dto.getEnd()) ? LocalDateTime.parse(dto.getEnd().trim(), FORMATTER) : null)
      .build() : null;
  }

  private boolean notEmpty(String s) {
    return s != null && !s.trim().isEmpty();
  }
}