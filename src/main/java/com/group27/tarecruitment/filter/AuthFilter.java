package com.group27.tarecruitment.filter;

import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/applicant/*", "/mo/*", "/admin/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request,
                         jakarta.servlet.ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String servletPath = httpRequest.getServletPath();
        if ("/admin/login".equals(servletPath) || "/staff/login".equals(servletPath)) {
            chain.doFilter(request, response);
            return;
        }

        boolean adminPath = servletPath.startsWith("/admin/");
        boolean moPath = servletPath.startsWith("/mo/");
        boolean staffPath = adminPath || moPath;
        if (!SessionUtil.isLoggedIn(httpRequest)) {
            SessionUtil.storeFlashError(httpRequest, staffPath
                    ? "Please use the staff login page before accessing staff features."
                    : "Please log in before accessing protected features.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + (staffPath ? "/staff/login" : "/login"));
            return;
        }

        chain.doFilter(request, response);
    }
}
