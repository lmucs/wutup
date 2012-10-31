package edu.lmu.cs.wutup.ws.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import edu.lmu.cs.wutup.ws.model.FacebookGateway;

@Service
public class FBAuthServiceImpl {
    public static String getAccessToken(String code) throws IOException {
        return FacebookGateway.acquireAccessToken(code);
    }
}
