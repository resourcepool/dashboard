package com.excilys.shooflers.dashboard.server.validator;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.FeedDto;
import com.excilys.shooflers.dashboard.server.exception.ValidationException;
import org.springframework.stereotype.Component;

/**
 * Validator of an {@link BundleMetadataDto}
 *
 * @author Mickael
 */
@Component
public class FeedDtoValidator implements Validator<FeedDto> {

    @Override
    public void validate(FeedDto feedDto) {
        if (feedDto == null) {
            throw new ValidationException("Feed can't be null");
        }

        if (feedDto.getName() == null || feedDto.getName().trim().isEmpty()) {
            throw new ValidationException("Feed Name can't be empty");
        }

        if (feedDto.getBundleTags() == null || feedDto.getBundleTags().isEmpty()) {
            throw new ValidationException("Feed must contain at least one bundle");
        }
    }
}
