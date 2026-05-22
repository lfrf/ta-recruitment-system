package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LogoutServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     *
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SessionUtil.clear(request);
        response.sendRedirect(request.getContextPath() + "/vacancies");
    }
}
