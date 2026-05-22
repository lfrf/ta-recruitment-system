package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.AiProfileImportService;
import com.group27.tarecruitment.util.SessionUtil;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * ApplicantAiTaskApplyServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/applicant/ai/tasks/apply")
public class ApplicantAiTaskApplyServlet extends HttpServlet {
    private final AiProfileImportService aiProfileImportService = new AiProfileImportService();

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

        String taskId = ValidationUtil.trimToEmpty(request.getParameter("taskId"));
        AiProfileImportService.ApplyResult result =
                aiProfileImportService.applySuggestionToProfile(currentUser.getUserId(), taskId);
        if (result.isOk()) {
            String body = "{"
                    + "\"status\":\"OK\","
                    + "\"taskStatus\":\"APPLIED\","
                    + "\"profile\":" + toProfileJson(result.getProfile())
                    + "}";
            writeJson(response, HttpServletResponse.SC_OK, body);
            return;
        }

        int statusCode = HttpServletResponse.SC_BAD_REQUEST;
        if ("TASK_NOT_FOUND".equals(result.getCode())) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
        } else if ("TASK_EXPIRED".equals(result.getCode())
                || "TASK_LOCKED".equals(result.getCode())
                || "TASK_NOT_READY".equals(result.getCode())) {
            statusCode = HttpServletResponse.SC_CONFLICT;
        }
        String body = "{"
                + "\"status\":\"ERROR\","
                + "\"code\":\"" + escapeJson(result.getCode()) + "\","
                + "\"message\":\"" + escapeJson(result.getMessage()) + "\""
                + "}";
        writeJson(response, statusCode, body);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param profile input parameter of type {@code ApplicantProfile}.
     * @return the computed `String` value for this operation.
     */
    private String toProfileJson(ApplicantProfile profile) {
        if (profile == null) {
            return "null";
        }
        return "{"
                + "\"fullName\":\"" + escapeJson(profile.getFullName()) + "\","
                + "\"studentId\":\"" + escapeJson(profile.getStudentId()) + "\","
                + "\"email\":\"" + escapeJson(profile.getEmail()) + "\","
                + "\"phone\":\"" + escapeJson(profile.getPhone()) + "\","
                + "\"degreeProgramme\":\"" + escapeJson(profile.getDegreeProgramme()) + "\","
                + "\"yearOfStudy\":\"" + escapeJson(profile.getYearOfStudy()) + "\","
                + "\"taExperience\":\"" + escapeJson(profile.getTaExperience()) + "\","
                + "\"projectOrLeadershipExperience\":\"" + escapeJson(profile.getProjectOrLeadershipExperience()) + "\","
                + "\"availability\":\"" + escapeJson(profile.getAvailability()) + "\","
                + "\"relevantCourses\":" + toJsonArray(profile.getRelevantCourses()) + ","
                + "\"skills\":" + toJsonArray(profile.getSkills())
                + "}";
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param values input parameter of type {@code Iterable<String>}.
     * @return the computed `String` value for this operation.
     */
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
