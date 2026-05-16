package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.service.QuickLoginRequestService;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/quick-login/poll")
public class QuickLoginPollServlet extends HttpServlet {
    private final QuickLoginRequestService quickLoginRequestService = new QuickLoginRequestService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = ValidationUtil.trimToEmpty(request.getParameter("request"));
        if (ValidationUtil.isBlank(requestId)) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"status\":\"INVALID_REQUEST\"}");
            return;
        }

        Optional<QuickLoginRequestService.QuickLoginRequest> quickLoginRequest = quickLoginRequestService.findRequest(requestId);
        if (quickLoginRequest.isEmpty()) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND, "{\"status\":\"NOT_FOUND\"}");
            return;
        }

        QuickLoginRequestService.QuickLoginRequest value = quickLoginRequest.get();
        String body = "{"
                + "\"status\":\"" + value.getStatus().name() + "\","
                + "\"expiresAt\":" + value.getExpiresAtEpochMillis()
                + "}";
        writeJson(response, HttpServletResponse.SC_OK, body);
    }

    private void writeJson(HttpServletResponse response, int statusCode, String body) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
    }
}
