package edu.lmu.cs.wutup.ws.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.servlet.FilterChain;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class CorsFilterTest {

    CorsFilter filter = new CorsFilter();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    FilterChain chain = mock(FilterChain.class);

    @Test
    public void arbitraryRequestGetsResponseWithAccessControlOriginHeader() throws Exception {
        request.setMethod("GET");
        filter.doFilterInternal(request, response, chain);
        assertThat(response.getHeader("Access-Control-Allow-Origin"), is("*"));
    }

    @Test
    public void optionsRequestHasCompleteSetOfHeaders() throws Exception {
        request.setMethod("OPTIONS");
        request.addHeader("Access-Control-Request-Method", "PUT");
        filter.doFilterInternal(request, response, chain);
        assertThat(response.getHeader("Access-Control-Allow-Origin"), is("*"));
        assertThat(response.getHeader("Access-Control-Allow-Methods"), is("GET, POST, PUT, DELETE"));
        assertThat(response.getHeader("Access-Control-Allow-Headers"), is("Content-Type"));
        assertThat(response.getHeader("Access-Control-Max-Age"), is("1800"));
    }

    @Test
    public void filterChainInvokedForSimpleRequests() throws Exception {
        request.setMethod("GET");
        filter.doFilterInternal(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void filterChainInvokedForPreflightRequests() throws Exception {
        request.setMethod("OPTIONS");
        request.addHeader("Access-Control-Request-Method", "PUT");
        filter.doFilterInternal(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void preflightRequestRequiresAccessControlRequestMethod() throws Exception {
        request.setMethod("OPTIONS");
        filter.doFilterInternal(request, response, chain);
        assertThat(response.getHeader("Access-Control-Allow-Methods"), is(not("GET, POST, PUT, DELETE")));
    }

    @Test
    public void preflightRequestRequiresOptions() throws Exception {
        request.addHeader("Access-Control-Request-Method", "PUT");
        filter.doFilterInternal(request, response, chain);
        assertThat(response.getHeader("Access-Control-Allow-Methods"), is(not("GET, POST, PUT, DELETE")));
    }
}
