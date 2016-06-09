package com.excilys.shooflers.dashboard.server.validator;

import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mickael on 09/06/2016.
 */
@Component
public class BundleMedataDtoValidatorImpl implements Validator<BundleMetadataDto> {

    @Autowired
    private ValidityDtoValidatorImpl validityDtoValidator;

    @Override
    public void validate(BundleMetadataDto bundleMetadataDto) {
        if (bundleMetadataDto == null) {
            throw new ValidationException("Bundle can't be null");
        }

        if (bundleMetadataDto.getName() == null || bundleMetadataDto.getName().isEmpty()) {
            throw new ValidationException("Bundle Name can't be empty");
        }

        if (bundleMetadataDto.getValidity() != null) {
            validityDtoValidator.validate(bundleMetadataDto.getValidity());
        }
    }
}
