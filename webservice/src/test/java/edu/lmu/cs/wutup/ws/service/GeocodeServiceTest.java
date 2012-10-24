package edu.lmu.cs.wutup.ws.service;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;

public class GeocodeServiceTest {

    @Autowired
    GeocodeServiceImpl service;

    // TODO: Finish this.
    @Ignore
    @Test(expected=NoAddressProvidedException.class)
    public void geocodeBlankAddressThrowsException() {
        service.resolveAddressToLatLong("");
    }
    
}
