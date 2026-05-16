package com.group27.tarecruitment.servlet;

import com.group27.tarecruitment.model.QuickLoginBinding;
import com.group27.tarecruitment.service.QuickLoginBindingService;
import com.group27.tarecruitment.service.QuickLoginRequestService;
import com.group27.tarecruitment.util.ValidationUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/quick-login/mobile")
public class QuickLoginMobileServlet extends HttpServlet {
    private static final String VIEW_PATH = "/WEB-INF/views/common/quick-login-mobile.jsp";

    private final QuickLoginRequestService quickLoginRequestService = new QuickLoginRequestService();
    private final QuickLoginBindingService quickLoginBindingService = new QuickLoginBindingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = ValidationUtil.trimToEmpty(request.getParameter("request"));
        if (ValidationUtil.isBlank(requestId)) {
            attachState(request, "Invalid quick login link.", "Please return to the applicant login page and generate a new QR code.", false);
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        Optional<QuickLoginRequestService.QuickLoginRequest> quickLoginRequest = quickLoginRequestService.findRequest(requestId);
        if (quickLoginRequest.isEmpty()) {
            attachState(request, "This login request is no longer available.", "Generate a fresh QR code on the applicant login page.", false);
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        QuickLoginRequestService.QuickLoginRequest value = quickLoginRequest.get();
        if (value.getStatus() == QuickLoginRequestService.Status.EXPIRED || value.getStatus() == QuickLoginRequestService.Status.USED) {
            attachState(request, "This login request has expired.", "Generate a fresh QR code on the applicant login page.", false);
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        Optional<QuickLoginBinding> quickLoginBinding = findBindingFromCookie(request);
        if (quickLoginBinding.isEmpty()) {
            attachState(request, "This phone is not bound yet.", "Bind this phone from My Profile first, then scan the login QR again.", false);
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        QuickLoginBinding binding = quickLoginBinding.get();
        request.setAttribute("boundDeviceName", binding.getDeviceName());

        if (value.getStatus() == QuickLoginRequestService.Status.CONFIRMED) {
            if (binding.getUserId().equals(value.getConfirmedUserId())) {
                attachState(request, "This request is already confirmed.", "Switch back to the computer. It should finish login shortly.", false);
            } else {
                attachState(request, "This request is already confirmed by another account.", "Generate a new QR code if needed.", false);
            }
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        request.setAttribute("requestId", requestId);
        attachState(request, "Confirm quick login", "If this is your own login request, tap Confirm to sign in on the computer.", true);
        request.getRequestDispatcher(VIEW_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = ValidationUtil.trimToEmpty(request.getParameter("request"));
        if (ValidationUtil.isBlank(requestId)) {
            attachState(request, "Invalid login request.", "Please scan a fresh login QR code.", false);
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        Optional<QuickLoginBinding> quickLoginBinding = findBindingFromCookie(request);
        if (quickLoginBinding.isEmpty()) {
            attachState(request, "This phone is not bound yet.", "Bind this phone from My Profile first, then scan again.", false);
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        QuickLoginBinding binding = quickLoginBinding.get();
        boolean confirmed = quickLoginRequestService.confirmRequest(requestId, binding.getUserId());
        if (!confirmed) {
            attachState(request, "Unable to confirm this request.", "It may already be used or expired. Please generate a new QR code.", false);
            request.setAttribute("boundDeviceName", binding.getDeviceName());
            request.getRequestDispatcher(VIEW_PATH).forward(request, response);
            return;
        }

        request.setAttribute("boundDeviceName", binding.getDeviceName());
        attachState(request, "Confirmed", "You can now return to the computer. Login will complete automatically.", false);
        request.getRequestDispatcher(VIEW_PATH).forward(request, response);
    }

    private Optional<QuickLoginBinding> findBindingFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (QuickLoginBindingService.QUICK_LOGIN_DEVICE_COOKIE.equals(cookie.getName())) {
                String token = ValidationUtil.trimToEmpty(cookie.getValue());
                return quickLoginBindingService.getActiveBindingByToken(token);
            }
        }
        return Optional.empty();
    }

    private void attachState(HttpServletRequest request, String title, String message, boolean canConfirm) {
        request.setAttribute("stateTitle", title);
        request.setAttribute("stateMessage", message);
        request.setAttribute("canConfirm", canConfirm);
    }
}
