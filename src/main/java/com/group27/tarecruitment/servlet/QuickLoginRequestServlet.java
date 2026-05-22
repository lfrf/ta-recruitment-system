package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.service.QuickLoginRequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * QuickLoginRequestServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/quick-login/request")
public class QuickLoginRequestServlet extends HttpServlet {
    private final QuickLoginRequestService quickLoginRequestService = new QuickLoginRequestService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuickLoginRequestService.QuickLoginRequest quickLoginRequest = quickLoginRequestService.createRequest();
        String confirmPath = request.getContextPath()
                + "/quick-login/mobile?request="
                + URLEncoder.encode(quickLoginRequest.getRequestId(), StandardCharsets.UTF_8);
        String confirmUrl = buildAbsoluteUrl(request, confirmPath);
        String body = "{"
                + "\"status\":\"PENDING\","
                + "\"requestId\":\"" + escapeJson(quickLoginRequest.getRequestId()) + "\","
                + "\"confirmUrl\":\"" + escapeJson(confirmUrl) + "\","
                + "\"expiresAt\":" + quickLoginRequest.getExpiresAtEpochMillis()
                + "}";
        writeJson(response, HttpServletResponse.SC_OK, body);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param path input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String buildAbsoluteUrl(HttpServletRequest request, String path) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        return scheme + "://" + serverName + (defaultPort ? "" : ":" + port) + path;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @param statusCode input parameter of type {@code int}.
     * @param body input parameter of type {@code String}.
     * @throws IOException if this operation cannot complete successfully.
     */
    private void writeJson(HttpServletResponse response, int statusCode, String body) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param value input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
