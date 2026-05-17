package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.service.AiVacancyRecommendService;
import com.group27.tarecruitment.service.ApplicantProfileService;
import com.group27.tarecruitment.service.VacancyService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/applicant/ai/recommend/tasks")
public class ApplicantAiRecommendTaskCreateServlet extends HttpServlet {
    private final AiVacancyRecommendService recommendService = new AiVacancyRecommendService();
    private final VacancyService vacancyService = new VacancyService();
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null || currentUser.getRole() != UserRole.APPLICANT) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN,
                    "{\"status\":\"FORBIDDEN\",\"message\":\"Applicant login is required.\"}");
            return;
        }

        List<Vacancy> candidateVacancies = vacancyService.getAllVacancies().stream()
                .filter(vacancy -> isBrowsableStatus(vacancy.getStatus()))
                .toList();
        if (candidateVacancies.isEmpty()) {
            writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
                    "{\"status\":\"INVALID_REQUEST\",\"message\":\"No vacancies are available for recommendation.\"}");
            return;
        }

        ApplicantProfile profile = applicantProfileService.findByApplicantId(currentUser.getUserId()).orElse(null);
        AiVacancyRecommendService.TaskCreationResult result =
                recommendService.createTask(currentUser.getUserId(), request, profile, candidateVacancies);

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

    private boolean isBrowsableStatus(String status) {
        String normalized = ValidationUtil.trimToEmpty(status);
        return "OPEN".equalsIgnoreCase(normalized) || "CLOSED".equalsIgnoreCase(normalized);
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

