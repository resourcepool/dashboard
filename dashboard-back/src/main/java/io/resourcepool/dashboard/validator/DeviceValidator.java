package io.resourcepool.dashboard.validator;

import io.resourcepool.dashboard.exception.ValidationException;
import io.resourcepool.dashboard.model.Device;
import org.springframework.stereotype.Component;

/**
 * Validator of an {@link io.resourcepool.dashboard.dto.BundleMetadataDto}
 *
 * @author Mickael
 */
@Component
public class DeviceValidator implements Validator<Device> {

  @Override
  public void validate(Device device) {
    if (device == null) {
      throw new ValidationException("Device can't be null");
    }

    if (device.getId() == null || device.getId().trim().isEmpty()) {
      throw new ValidationException("Device must contain an id");
    }

  }
}
