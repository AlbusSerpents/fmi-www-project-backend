package com.serpents.ipv6dns.spring.networking;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.*;

public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig arg0) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpResponse.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, PUT, DELETE, OPTIONS");
        httpResponse.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, X-Auth-Token, Access-Control-Allow-Headers");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}