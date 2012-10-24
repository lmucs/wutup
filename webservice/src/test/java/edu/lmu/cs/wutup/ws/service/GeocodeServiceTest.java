package edu.lmu.cs.wutup.ws.service;

import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;

public class GeocodeServiceTest {

    GeocodeServiceImpl service;

    // TODO: Finish this.
    
    @Test(expected=NoAddressProvidedException.class)
    public void creationPropagatesExistExceptions() {
        service.resolveAddressToLatLong("");
    }
    
}
