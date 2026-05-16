package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AiProfileImportService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/applicant/ai/tasks")
public class ApplicantAiTaskCreateServlet extends HttpServlet {
    private final AiProfileImportService aiProfileImportService = new AiProfileImportService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null || currentUser.getRole() != UserRole.APPLICANT) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN,
                    "{\"status\":\"FORBIDDEN\",\"message\":\"Applicant login is required.\"}");
            return;
        }

        AiProfileImportService.TaskCreationResult result =
                aiProfileImportService.createTask(currentUser.getUserId(), request);
        String body = "{"
                + "\"status\":\"OK\","
                + "\"taskId\":\"" + escapeJson(result.getTask().getTaskId()) + "\","
                + "\"schemaVersion\":\"" + escapeJson(result.getTask().getSchemaVersion()) + "\","
                + "\"expiresAt\":" + result.getTask().getExpiresAtEpochMillis() + ","
                + "\"callbackUrl\":\"" + escapeJson(result.getCallbackUrl()) + "\","
                + "\"promptTemplate\":\"" + escapeJson(result.getPromptTemplate()) + "\""
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
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}
