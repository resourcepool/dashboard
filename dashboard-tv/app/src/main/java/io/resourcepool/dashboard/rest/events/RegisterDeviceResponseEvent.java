package io.resourcepool.dashboard.rest.events;

import io.resourcepool.dashboard.rest.dtos.Device;

/**
 * Created by loicortola on 04/03/2017.
 */

public class RegisterDeviceResponseEvent {
    private final Device device;

    public RegisterDeviceResponseEvent(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }
}
