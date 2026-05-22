package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.service.ApplicantProfileService;
import com.group27.tarecruitment.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * DownloadApplicantCvServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/mo/cv")
public class DownloadApplicantCvServlet extends HttpServlet {
    private final ApplicantProfileService applicantProfileService = new ApplicantProfileService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccount currentUser = SessionUtil.getCurrentUser(request);
        if (currentUser == null || currentUser.getRole() != UserRole.MO) {
            SessionUtil.storeFlashError(request, "Only organiser accounts can download applicant CV files.");
            response.sendRedirect(request.getContextPath() + "/staff/login");
            return;
        }

        String applicantId = request.getParameter("applicantId");
        Optional<ApplicantProfile> optionalProfile = applicantProfileService.findByApplicantId(applicantId);
        if (optionalProfile.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Applicant profile not found.");
            return;
        }

        ApplicantProfile profile = optionalProfile.get();
        if (profile.getCvFilePath() == null || profile.getCvFilePath().isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No CV has been uploaded for this applicant.");
            return;
        }

        Path cvPath = Path.of(profile.getCvFilePath());
        if (Files.notExists(cvPath) || !Files.isReadable(cvPath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "The stored CV file could not be found.");
            return;
        }

        String fileName = profile.getCvFileName() == null || profile.getCvFileName().isBlank()
                ? cvPath.getFileName().toString()
                : profile.getCvFileName();
        String contentType = Files.probeContentType(cvPath);
        response.setContentType(contentType != null ? contentType : "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName.replace("\"", "") + "\"");
        response.setContentLengthLong(Files.size(cvPath));
        Files.copy(cvPath, response.getOutputStream());
    }
}
