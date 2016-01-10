package com.offbynull.portmapper.io.internalmessages;

import com.offbynull.portmapper.common.Bus;
import org.apache.commons.lang3.Validate;

public final class GetLocalIpAddressesIoRequest implements IoRequest {
    private Bus responseBus;

    public GetLocalIpAddressesIoRequest(Bus responseBus) {
        Validate.notNull(responseBus);

        this.responseBus = responseBus;
    }

    public Bus getResponseBus() {
        return responseBus;
    }
}