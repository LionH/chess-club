package org.chesscorp.club.filters;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Test some features of the CORS filter behavior.
 */

public class CorsFilterTest {

    @Test
    public void testInitAndDestroy() throws IOException, ServletException {
        CorsFilter filter = new CorsFilter();

        FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.destroy();
    }

    @Test
    public void testFilter() throws IOException, ServletException {
        CorsFilter filter = new CorsFilter();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        filter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
        Mockito.verify(response, Mockito.times(4)).setHeader(Mockito.any(String.class), Mockito.any(String.class));
    }
}
