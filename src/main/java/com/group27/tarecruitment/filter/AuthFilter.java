package com.group27.tarecruitment.filter;

import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * AuthFilter class type.
 *
 * <p>Filter type that applies request pre-checks and access control gating.</p>
 * <p>Package: {@code com.group27.tarecruitment.filter}</p>
 */
@WebFilter(urlPatterns = {"/applicant/*", "/mo/*", "/admin/*"})
public class AuthFilter implements Filter {
    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code jakarta.servlet.ServletRequest}.
     * @param response input parameter of type {@code jakarta.servlet.ServletResponse}.
     * @param chain input parameter of type {@code FilterChain}.
     * @throws IOException if this operation cannot complete successfully.
     * @throws ServletException if this operation cannot complete successfully.
     */
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
