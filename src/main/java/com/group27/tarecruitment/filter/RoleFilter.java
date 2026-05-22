package com.group27.tarecruitment.filter;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * RoleFilter class type.
 *
 * <p>Filter type that applies request pre-checks and access control gating.</p>
 * <p>Package: {@code com.group27.tarecruitment.filter}</p>
 */
@WebFilter(urlPatterns = {"/applicant/*", "/mo/*", "/admin/*"})
public class RoleFilter implements Filter {
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
        UserAccount currentUser = SessionUtil.getCurrentUser(httpRequest);

        if (currentUser == null) {
            chain.doFilter(request, response);
            return;
        }

        String servletPath = httpRequest.getServletPath();
        if (servletPath.startsWith("/applicant/") && currentUser.getRole() != UserRole.APPLICANT) {
            SessionUtil.storeFlashError(httpRequest, "Only applicant accounts can access applicant pages.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/vacancies");
            return;
        }
        if (servletPath.startsWith("/mo/") && currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(httpRequest, "Only organiser accounts can access organiser pages.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/vacancies");
            return;
        }
        if (servletPath.startsWith("/admin/") && !"/admin/login".equals(servletPath) && currentUser.getRole() != UserRole.ADMIN) {
            SessionUtil.storeFlashError(httpRequest, "Only admin accounts can access admin pages. Please use the staff login page.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/staff/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
