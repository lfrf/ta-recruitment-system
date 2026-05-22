package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.QuickLoginBinding;
import com.group27.tarecruitment.service.QuickLoginBindRequestService;
import com.group27.tarecruitment.service.QuickLoginBindingService;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * QuickLoginBindMobileServlet class type.
 *
 * <p>Servlet/controller type that handles HTTP input, output, and endpoint orchestration.</p>
 * <p>Package: {@code com.group27.tarecruitment.servlet}</p>
 */
@WebServlet("/quick-login/bind/mobile")
public class QuickLoginBindMobileServlet extends HttpServlet {
    private static final String VIEW_PATH = "/WEB-INF/views/common/quick-login-bind-mobile.jsp";

    private final QuickLoginBindRequestService quickLoginBindRequestService = new QuickLoginBindRequestService();
    private final QuickLoginBindingService quickLoginBindingService = new QuickLoginBindingService();

    /**
     * Handles the primary HTTP/filter entrypoint workflow for this operation.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @throws ServletException if this operation cannot complete successfully.
     * @throws IOException if this operation cannot complete successfully.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = ValidationUtil.trimToEmpty(request.getParameter("request"));
        if (ValidationUtil.isBlank(requestId)) {
            attachState(request, "Invalid bind link.", "Please return to your computer profile page and generate a new bind QR code.");
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        Optional<QuickLoginBindRequestService.BindRequest> bindRequest = quickLoginBindRequestService.findRequest(requestId);
        if (bindRequest.isEmpty()) {
            attachState(request, "This bind request is no longer available.", "Generate a fresh bind QR code from My Profile.");
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        QuickLoginBindRequestService.BindRequest value = bindRequest.get();
        if (value.getStatus() == QuickLoginBindRequestService.Status.EXPIRED) {
            attachState(request, "This bind request has expired.", "Generate a fresh bind QR code from My Profile.");
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        if (value.getStatus() != QuickLoginBindRequestService.Status.BOUND) {
            QuickLoginBinding binding = quickLoginBindingService.bindDevice(value.getUserId(), request.getHeader("User-Agent"));
            quickLoginBindRequestService.markBound(requestId, binding.getDeviceName());
            attachBindingCookie(request, response, binding.getBindToken());
            attachState(request, "Phone bound successfully", "This phone can now confirm quick login requests for your applicant account.");
            request.setAttribute("boundDeviceName", binding.getDeviceName());
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        quickLoginBindingService.getActiveBinding(value.getUserId())
                .ifPresent(binding -> attachBindingCookie(request, response, binding.getBindToken()));
        String deviceName = ValidationUtil.trimToEmpty(value.getBoundDeviceName());
        attachState(request, "This bind request is already completed.", "You can return to the computer and continue.");
        if (!ValidationUtil.isBlank(deviceName)) {
            request.setAttribute("boundDeviceName", deviceName);
        }
        request.getRequestDispatcher(VIEW_PATH).forward(request, response);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param response input parameter of type {@code HttpServletResponse}.
     * @param bindToken input parameter of type {@code String}.
     */
    private void attachBindingCookie(HttpServletRequest request, HttpServletResponse response, String bindToken) {
        Cookie cookie = new Cookie(QuickLoginBindingService.QUICK_LOGIN_DEVICE_COOKIE, bindToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(180 * 24 * 60 * 60);
        String path = request.getContextPath();
        cookie.setPath((path == null || path.isBlank()) ? "/" : path);
        response.addCookie(cookie);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param title input parameter of type {@code String}.
     * @param message input parameter of type {@code String}.
     */
    private void attachState(HttpServletRequest request, String title, String message) {
        request.setAttribute("stateTitle", title);
        request.setAttribute("stateMessage", message);
    }
}
