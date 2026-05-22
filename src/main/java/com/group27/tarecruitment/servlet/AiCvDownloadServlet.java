package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.service.AiProfileImportService;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * AiCvDownloadServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/ai/cv-download")
public class AiCvDownloadServlet extends HttpServlet {
    private final AiProfileImportService aiProfileImportService = new AiProfileImportService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId = ValidationUtil.trimToEmpty(request.getParameter("taskId"));
        String token = ValidationUtil.trimToEmpty(request.getParameter("token"));

        AiProfileImportService.CvDownloadResult result = aiProfileImportService.consumeCvDownload(taskId, token);
        if (!result.isOk()) {
            int statusCode = HttpServletResponse.SC_BAD_REQUEST;
            if ("TASK_NOT_FOUND".equals(result.getCode()) || "CV_NOT_FOUND".equals(result.getCode())) {
                statusCode = HttpServletResponse.SC_NOT_FOUND;
            } else if ("TOKEN_EXPIRED".equals(result.getCode()) || "TOKEN_EXHAUSTED".equals(result.getCode())) {
                statusCode = HttpServletResponse.SC_CONFLICT;
            } else if ("INVALID_TOKEN".equals(result.getCode())) {
                statusCode = HttpServletResponse.SC_FORBIDDEN;
            }
            response.setStatus(statusCode);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write(result.getCode() + ": " + result.getMessage());
            return;
        }

        Path filePath = result.getFilePath();
        String fileName = resolveFileName(result.getFileName(), filePath);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(resolveContentType(fileName));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Cache-Control", "no-store");
        response.setContentLengthLong(Files.size(filePath));
        Files.copy(filePath, response.getOutputStream());
        aiProfileImportService.markCvDownloadConsumed(taskId);
    }

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId = ValidationUtil.trimToEmpty(request.getParameter("taskId"));
        String token = ValidationUtil.trimToEmpty(request.getParameter("token"));

        AiProfileImportService.CvDownloadResult result = aiProfileImportService.consumeCvDownload(taskId, token);
        if (!result.isOk()) {
            int statusCode = HttpServletResponse.SC_BAD_REQUEST;
            if ("TASK_NOT_FOUND".equals(result.getCode()) || "CV_NOT_FOUND".equals(result.getCode())) {
                statusCode = HttpServletResponse.SC_NOT_FOUND;
            } else if ("TOKEN_EXPIRED".equals(result.getCode()) || "TOKEN_EXHAUSTED".equals(result.getCode())) {
                statusCode = HttpServletResponse.SC_CONFLICT;
            } else if ("INVALID_TOKEN".equals(result.getCode())) {
                statusCode = HttpServletResponse.SC_FORBIDDEN;
            }
            response.setStatus(statusCode);
            return;
        }

        Path filePath = result.getFilePath();
        String fileName = resolveFileName(result.getFileName(), filePath);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(resolveContentType(fileName));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Cache-Control", "no-store");
        response.setContentLengthLong(Files.size(filePath));
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param originalFileName input parameter of type {@code String}.
     * @param filePath input parameter of type {@code Path}.
     * @return the computed `String` value for this operation.
     */
    private String resolveFileName(String originalFileName, Path filePath) {
        if (!ValidationUtil.isBlank(originalFileName)) {
            return Path.of(originalFileName).getFileName().toString();
        }
        return filePath.getFileName().toString();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param fileName input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String resolveContentType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf")) {
            return "application/pdf";
        }
        if (lower.endsWith(".doc")) {
            return "application/msword";
        }
        if (lower.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        return "application/octet-stream";
    }
}
