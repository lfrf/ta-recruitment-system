package com.group27.tarecruitment.service;

import com.group27.tarecruitment.util.ValidationUtil;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * QuickLoginRequestService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class QuickLoginRequestService {
    private static final long REQUEST_TTL_MILLIS = 3 * 60 * 1000;
    private static final Map<String, QuickLoginRequest> REQUESTS = new ConcurrentHashMap<>();

    /**
     * Creates and initializes new business data for downstream use.
     * @return the computed `QuickLoginRequest` value for this operation.
     */
    public QuickLoginRequest createRequest() {
        cleanup();
        String requestId = UUID.randomUUID().toString().replace("-", "");
        long now = Instant.now().toEpochMilli();
        QuickLoginRequest request = new QuickLoginRequest(requestId, Status.PENDING, now, now + REQUEST_TTL_MILLIS);
        REQUESTS.put(requestId, request);
        return request;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param requestId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<QuickLoginRequest> findRequest(String requestId) {
        cleanup();
        if (ValidationUtil.isBlank(requestId)) {
            return Optional.empty();
        }
        QuickLoginRequest request = REQUESTS.get(requestId);
        if (request == null) {
            return Optional.empty();
        }
        if (request.getExpiresAtEpochMillis() <= Instant.now().toEpochMilli() && request.getStatus() != Status.USED) {
            request.setStatus(Status.EXPIRED);
        }
        return Optional.of(request);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param requestId input parameter of type {@code String}.
     * @param userId input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean confirmRequest(String requestId, String userId) {
        Optional<QuickLoginRequest> optional = findRequest(requestId);
        if (optional.isEmpty()) {
            return false;
        }
        QuickLoginRequest request = optional.get();
        if (request.getStatus() == Status.EXPIRED || request.getStatus() == Status.USED) {
            return false;
        }
        if (request.getStatus() == Status.CONFIRMED) {
            return userId.equals(request.getConfirmedUserId());
        }
        request.setStatus(Status.CONFIRMED);
        request.setConfirmedUserId(userId);
        request.setConfirmedAtEpochMillis(Instant.now().toEpochMilli());
        return true;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param requestId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<String> consumeConfirmedRequest(String requestId) {
        Optional<QuickLoginRequest> optional = findRequest(requestId);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        QuickLoginRequest request = optional.get();
        if (request.getStatus() != Status.CONFIRMED || ValidationUtil.isBlank(request.getConfirmedUserId())) {
            return Optional.empty();
        }
        request.setStatus(Status.USED);
        return Optional.of(request.getConfirmedUserId());
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    private void cleanup() {
        long now = Instant.now().toEpochMilli();
        REQUESTS.entrySet().removeIf(entry -> entry.getValue().getExpiresAtEpochMillis() + 60_000 < now);
    }

    /**
     * Status enum type.
     *
     * <p>Core module type in the TA recruitment system domain.</p>
     * <p>Package: {@code com.group27.tarecruitment.service}</p>
     */
    public enum Status {
        PENDING,
        CONFIRMED,
        USED,
        EXPIRED
    }

    public static class QuickLoginRequest {
        private final String requestId;
        private Status status;
        private final long createdAtEpochMillis;
        private final long expiresAtEpochMillis;
        private String confirmedUserId;
        private Long confirmedAtEpochMillis;

        public QuickLoginRequest(String requestId, Status status, long createdAtEpochMillis, long expiresAtEpochMillis) {
            this.requestId = requestId;
            this.status = status;
            this.createdAtEpochMillis = createdAtEpochMillis;
            this.expiresAtEpochMillis = expiresAtEpochMillis;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
        public String getRequestId() {
            return requestId;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `Status` value for this operation.
         */
        public Status getStatus() {
            return status;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param status input parameter of type {@code Status}.
         */
        public void setStatus(Status status) {
            this.status = status;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `long` value for this operation.
         */
        public long getCreatedAtEpochMillis() {
            return createdAtEpochMillis;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `long` value for this operation.
         */
        public long getExpiresAtEpochMillis() {
            return expiresAtEpochMillis;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
        public String getConfirmedUserId() {
            return confirmedUserId;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param confirmedUserId input parameter of type {@code String}.
         */
        public void setConfirmedUserId(String confirmedUserId) {
            this.confirmedUserId = confirmedUserId;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `Long` value for this operation.
         */
        public Long getConfirmedAtEpochMillis() {
            return confirmedAtEpochMillis;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param confirmedAtEpochMillis input parameter of type {@code Long}.
         */
        public void setConfirmedAtEpochMillis(Long confirmedAtEpochMillis) {
            this.confirmedAtEpochMillis = confirmedAtEpochMillis;
        }
    }
}
