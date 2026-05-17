package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.AiVacancyRecommendTask;
import com.group27.tarecruitment.model.AiVacancyRecommendation;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AiVacancyRecommendService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/applicant/ai/recommend/tasks/status")
public class ApplicantAiRecommendTaskStatusServlet extends HttpServlet {
    private final AiVacancyRecommendService recommendService = new AiVacancyRecommendService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null || currentUser.getRole() != UserRole.APPLICANT) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN,
                    "{\"status\":\"FORBIDDEN\",\"message\":\"Applicant login is required.\"}");
            return;
        }

        String taskId = ValidationUtil.trimToEmpty(request.getParameter("taskId"));
        if (ValidationUtil.isBlank(taskId)) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    "{\"status\":\"INVALID_REQUEST\",\"message\":\"taskId is required.\"}");
            return;
        }

        Optional<AiVacancyRecommendTask> optionalTask =
                recommendService.findTaskForUser(currentUser.getUserId(), taskId);
        if (optionalTask.isEmpty()) {
            writeJson(response, HttpServletResponse.SC_NOT_FOUND,
                    "{\"status\":\"NOT_FOUND\",\"message\":\"Task was not found.\"}");
            return;
        }

        AiVacancyRecommendTask task = optionalTask.get();
        String body = "{"
                + "\"status\":\"OK\","
                + "\"taskId\":\"" + escapeJson(task.getTaskId()) + "\","
                + "\"taskStatus\":\"" + escapeJson(task.getStatus()) + "\","
                + "\"expiresAt\":" + task.getExpiresAtEpochMillis() + ","
                + "\"validationErrors\":" + toJsonArray(task.getValidationErrors()) + ","
                + "\"rankings\":" + toRankingJson(task.getRecommendations())
                + "}";
        writeJson(response, HttpServletResponse.SC_OK, body);
    }

    private String toRankingJson(Iterable<AiVacancyRecommendation> rankings) {
        if (rankings == null) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for (AiVacancyRecommendation recommendation : rankings) {
            if (!first) {
                builder.append(",");
            }
            builder.append("{")
                    .append("\"vacancyId\":\"").append(escapeJson(recommendation.getVacancyId())).append("\",")
                    .append("\"score\":").append(recommendation.getScore() == null ? 0 : recommendation.getScore()).append(",")
                    .append("\"reasons\":").append(toJsonArray(recommendation.getReasons()))
                    .append("}");
            first = false;
        }
        builder.append("]");
        return builder.toString();
    }

    private String toJsonArray(Iterable<String> values) {
        if (values == null) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for (String value : values) {
            if (!first) {
                builder.append(",");
            }
            builder.append("\"").append(escapeJson(value)).append("\"");
            first = false;
        }
        builder.append("]");
        return builder.toString();
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

