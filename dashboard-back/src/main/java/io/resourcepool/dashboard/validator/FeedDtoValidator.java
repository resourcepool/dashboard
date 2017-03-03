package io.resourcepool.dashboard.validator;

import io.resourcepool.dashboard.dto.BundleMetadataDto;
import io.resourcepool.dashboard.dto.FeedDto;
import io.resourcepool.dashboard.exception.ValidationException;
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
