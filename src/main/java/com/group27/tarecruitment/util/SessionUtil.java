package com.group27.tarecruitment.util;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil {
    public static final String CURRENT_USER = "currentUser";
    public static final String FLASH_MESSAGE = "flashMessage";
    public static final String FLASH_ERROR = "flashError";

    private SessionUtil() {
    }

    public static void storeUser(HttpServletRequest request, UserAccount userAccount) {
        HttpSession session = request.getSession(true);
        session.setAttribute(CURRENT_USER, userAccount);
    }

    public static UserAccount getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(CURRENT_USER);
        return value instanceof UserAccount ? (UserAccount) value : null;
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }

    public static boolean hasRole(HttpServletRequest request, UserRole role) {
        UserAccount currentUser = getCurrentUser(request);
        return currentUser != null && currentUser.getRole() == role;
    }

    public static void storeFlashMessage(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(FLASH_MESSAGE, message);
    }

    public static void storeFlashError(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(FLASH_ERROR, message);
    }

    public static String consumeFlashMessage(HttpServletRequest request) {
        return consume(request, FLASH_MESSAGE);
    }

    public static String consumeFlashError(HttpServletRequest request) {
        return consume(request, FLASH_ERROR);
    }

    public static void clear(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private static String consume(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(key);
        if (value == null) {
            return null;
        }
        session.removeAttribute(key);
        return value.toString();
    }
}
