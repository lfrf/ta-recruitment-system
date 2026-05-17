package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.service.AiProfileImportService;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/ai/callback")
public class AiTaskCallbackServlet extends HttpServlet {
    private static final String CALLBACK_TOKEN_HEADER = "X-Callback-Token";

    private final AiProfileImportService aiProfileImportService = new AiProfileImportService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId = ValidationUtil.trimToEmpty(request.getParameter("taskId"));
        String callbackToken = ValidationUtil.trimToEmpty(request.getHeader(CALLBACK_TOKEN_HEADER));
        String payloadJson = request.getReader().lines().collect(Collectors.joining("\n"));

        AiProfileImportService.CallbackResult result = aiProfileImportService.acceptCallback(taskId, callbackToken, payloadJson);
        if (result.isOk()) {
            writeJson(response, HttpServletResponse.SC_OK, "{\"status\":\"OK\"}");
            return;
        }

        int statusCode = HttpServletResponse.SC_BAD_REQUEST;
        if ("TASK_NOT_FOUND".equals(result.getCode())) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
        } else if ("INVALID_TOKEN".equals(result.getCode())) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
        } else if ("TASK_EXPIRED".equals(result.getCode()) || "TASK_LOCKED".equals(result.getCode())) {
            statusCode = HttpServletResponse.SC_CONFLICT;
        }
        String body = "{"
                + "\"status\":\"ERROR\","
                + "\"code\":\"" + escapeJson(result.getCode()) + "\","
                + "\"message\":\"" + escapeJson(result.getMessage()) + "\""
                + "}";
        writeJson(response, statusCode, body);
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
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}
