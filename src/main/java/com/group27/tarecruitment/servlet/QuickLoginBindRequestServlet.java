package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.QuickLoginBindRequestService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/applicant/quick-login-binding/request")
public class QuickLoginBindRequestServlet extends HttpServlet {
    private final QuickLoginBindRequestService quickLoginBindRequestService = new QuickLoginBindRequestService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null || currentUser.getRole() != UserRole.APPLICANT) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN, "{\"status\":\"FORBIDDEN\"}");
            return;
        }

        QuickLoginBindRequestService.BindRequest bindRequest = quickLoginBindRequestService.createRequest(currentUser.getUserId());
        String bindPath = request.getContextPath()
                + "/quick-login/bind/mobile?request="
                + URLEncoder.encode(bindRequest.getRequestId(), StandardCharsets.UTF_8);
        String bindUrl = buildAbsoluteUrl(request, bindPath);
        String body = "{"
                + "\"status\":\"PENDING\","
                + "\"requestId\":\"" + escapeJson(bindRequest.getRequestId()) + "\","
                + "\"bindUrl\":\"" + escapeJson(bindUrl) + "\","
                + "\"expiresAt\":" + bindRequest.getExpiresAtEpochMillis()
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
