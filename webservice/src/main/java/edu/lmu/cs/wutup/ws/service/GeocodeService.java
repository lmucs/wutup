package edu.lmu.cs.wutup.ws.service;

import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.LatLong;

public interface GeocodeService {

    public LatLong resolveAddressToLatLong(String address) throws NoAddressProvidedException;

}
