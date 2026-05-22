package com.group27.tarecruitment.util;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil {
    public static final String CURRENT_USER = "currentUser";
    public static final String FLASH_MESSAGE = "flashMessage";
    public static final String FLASH_ERROR = "flashError";

    /**
     * Executes business behavior as part of the class contract.
     */
    private SessionUtil() {
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param userAccount input parameter of type {@code UserAccount}.
     */
    public static void storeUser(HttpServletRequest request, UserAccount userAccount) {
        HttpSession session = request.getSession(true);
        session.setAttribute(CURRENT_USER, userAccount);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @return the computed `UserAccount` value for this operation.
     */
    public static UserAccount getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(CURRENT_USER);
        return value instanceof UserAccount ? (UserAccount) value : null;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @return true when the condition is met; otherwise false.
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param role input parameter of type {@code UserRole}.
     * @return true when the condition is met; otherwise false.
     */
    public static boolean hasRole(HttpServletRequest request, UserRole role) {
        UserAccount currentUser = getCurrentUser(request);
        return currentUser != null && currentUser.getRole() == role;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param message input parameter of type {@code String}.
     */
    public static void storeFlashMessage(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(FLASH_MESSAGE, message);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param message input parameter of type {@code String}.
     */
    public static void storeFlashError(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute(FLASH_ERROR, message);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @return the computed `String` value for this operation.
     */
    public static String consumeFlashMessage(HttpServletRequest request) {
        return consume(request, FLASH_MESSAGE);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @return the computed `String` value for this operation.
     */
    public static String consumeFlashError(HttpServletRequest request) {
        return consume(request, FLASH_ERROR);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     */
    public static void clear(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param request input parameter of type {@code HttpServletRequest}.
     * @param key input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
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
