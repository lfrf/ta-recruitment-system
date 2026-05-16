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

@WebServlet("/quick-login/request")
public class QuickLoginRequestServlet extends HttpServlet {
    private final QuickLoginRequestService quickLoginRequestService = new QuickLoginRequestService();

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

    private String buildAbsoluteUrl(HttpServletRequest request, String path) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        return scheme + "://" + serverName + (defaultPort ? "" : ":" + port) + path;
    }

    private void writeJson(HttpServletResponse response, int statusCode, String body) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
