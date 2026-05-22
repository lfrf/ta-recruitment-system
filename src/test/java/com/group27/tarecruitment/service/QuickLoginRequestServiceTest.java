package com.group27.tarecruitment.service;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * QuickLoginRequestServiceTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class QuickLoginRequestServiceTest {

    private final QuickLoginRequestService quickLoginRequestService = new QuickLoginRequestService();

    /**
     * Creates and initializes new business data for downstream use.
     */
    @Test
    void createConfirmAndConsumeShouldFollowExpectedStateTransitions() {
        QuickLoginRequestService.QuickLoginRequest request = quickLoginRequestService.createRequest();
        String requestId = request.getRequestId();

        Optional<QuickLoginRequestService.QuickLoginRequest> pending = quickLoginRequestService.findRequest(requestId);
        assertTrue(pending.isPresent());
        assertEquals(QuickLoginRequestService.Status.PENDING, pending.get().getStatus());

        boolean confirmed = quickLoginRequestService.confirmRequest(requestId, "u-app-1");
        assertTrue(confirmed);

        Optional<String> consumedUserId = quickLoginRequestService.consumeConfirmedRequest(requestId);
        assertTrue(consumedUserId.isPresent());
        assertEquals("u-app-1", consumedUserId.get());

        Optional<String> consumedAgain = quickLoginRequestService.consumeConfirmedRequest(requestId);
        assertTrue(consumedAgain.isEmpty());
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void confirmShouldRejectDifferentUserAfterAlreadyConfirmed() {
        QuickLoginRequestService.QuickLoginRequest request = quickLoginRequestService.createRequest();
        String requestId = request.getRequestId();

        boolean first = quickLoginRequestService.confirmRequest(requestId, "u-app-1");
        assertTrue(first);

        boolean secondWithDifferentUser = quickLoginRequestService.confirmRequest(requestId, "u-app-2");
        assertFalse(secondWithDifferentUser);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     */
    @Test
    void findRequestShouldReturnEmptyForBlankInput() {
        assertTrue(quickLoginRequestService.findRequest("").isEmpty());
        assertTrue(quickLoginRequestService.findRequest(" ").isEmpty());
    }
}
