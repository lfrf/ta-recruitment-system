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

/**
 * ApplicantAiRecommendTaskCreateServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/applicant/ai/recommend/tasks")
public class ApplicantAiRecommendTaskCreateServlet extends HttpServlet {
    private final AiVacancyRecommendService recommendService = new AiVacancyRecommendService();
    private final VacancyService vacancyService = new VacancyService();
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
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

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param status input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isBrowsableStatus(String status) {
        String normalized = ValidationUtil.trimToEmpty(status);
        return "OPEN".equalsIgnoreCase(normalized) || "CLOSED".equalsIgnoreCase(normalized);
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
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}

