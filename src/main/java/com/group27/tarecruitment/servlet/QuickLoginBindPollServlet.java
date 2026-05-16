package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.QuickLoginBindRequestService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/applicant/quick-login-binding/poll")
public class QuickLoginBindPollServlet extends HttpServlet {
    private final QuickLoginBindRequestService quickLoginBindRequestService = new QuickLoginBindRequestService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null || currentUser.getRole() != UserRole.APPLICANT) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN, "{\"status\":\"FORBIDDEN\"}");
            return;
        }

        String requestId = ValidationUtil.trimToEmpty(request.getParameter("request"));
        if (ValidationUtil.isBlank(requestId)) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"status\":\"INVALID_REQUEST\"}");
            return;
        }

        Optional<QuickLoginBindRequestService.BindRequest> bindRequest = quickLoginBindRequestService.findRequest(requestId);
        if (bindRequest.isEmpty()) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND, "{\"status\":\"NOT_FOUND\"}");
            return;
        }

        QuickLoginBindRequestService.BindRequest value = bindRequest.get();
        if (!currentUser.getUserId().equals(value.getUserId())) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN, "{\"status\":\"FORBIDDEN\"}");
            return;
        }

        String body = "{"
                + "\"status\":\"" + value.getStatus().name() + "\","
                + "\"expiresAt\":" + value.getExpiresAtEpochMillis() + ","
                + "\"boundDeviceName\":\"" + escapeJson(value.getBoundDeviceName()) + "\""
                + "}";
        writeJson(response, HttpServletResponse.SC_OK, body);
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
