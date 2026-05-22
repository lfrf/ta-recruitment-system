package com.group27.tarecruitment.service;

import com.group27.tarecruitment.util.ValidationUtil;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * QuickLoginBindRequestService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class QuickLoginBindRequestService {
    private static final long REQUEST_TTL_MILLIS = 3 * 60 * 1000;
    private static final Map<String, BindRequest> REQUESTS = new ConcurrentHashMap<>();

    /**
     * Creates and initializes new business data for downstream use.
     * @param userId input parameter of type {@code String}.
     * @return the computed `BindRequest` value for this operation.
     */
    public BindRequest createRequest(String userId) {
        cleanup();
        String requestId = UUID.randomUUID().toString().replace("-", "");
        long now = Instant.now().toEpochMilli();
        BindRequest bindRequest = new BindRequest(
                requestId,
                userId,
                Status.PENDING,
                now,
                now + REQUEST_TTL_MILLIS
        );
        REQUESTS.put(requestId, bindRequest);
        return bindRequest;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param requestId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<BindRequest> findRequest(String requestId) {
        cleanup();
        if (ValidationUtil.isBlank(requestId)) {
            return Optional.empty();
        }
        BindRequest bindRequest = REQUESTS.get(requestId);
        if (bindRequest == null) {
            return Optional.empty();
        }
        if (bindRequest.getExpiresAtEpochMillis() <= Instant.now().toEpochMilli()
                && bindRequest.getStatus() == Status.PENDING) {
            bindRequest.setStatus(Status.EXPIRED);
        }
        return Optional.of(bindRequest);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param requestId input parameter of type {@code String}.
     * @param deviceName input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean markBound(String requestId, String deviceName) {
        Optional<BindRequest> optional = findRequest(requestId);
        if (optional.isEmpty()) {
            return false;
        }
        BindRequest bindRequest = optional.get();
        if (bindRequest.getStatus() == Status.EXPIRED) {
            return false;
        }
        bindRequest.setStatus(Status.BOUND);
        bindRequest.setBoundAtEpochMillis(Instant.now().toEpochMilli());
        bindRequest.setBoundDeviceName(ValidationUtil.trimToEmpty(deviceName));
        return true;
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
        BOUND,
        EXPIRED
    }

    public static class BindRequest {
        private final String requestId;
        private final String userId;
        private Status status;
        private final long createdAtEpochMillis;
        private final long expiresAtEpochMillis;
        private Long boundAtEpochMillis;
        private String boundDeviceName;

        public BindRequest(String requestId,
                           String userId,
                           Status status,
                           long createdAtEpochMillis,
                           long expiresAtEpochMillis) {
            this.requestId = requestId;
            this.userId = userId;
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
         * @return the computed `String` value for this operation.
         */
        public String getUserId() {
            return userId;
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
         * @return the computed `Long` value for this operation.
         */
        public Long getBoundAtEpochMillis() {
            return boundAtEpochMillis;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param boundAtEpochMillis input parameter of type {@code Long}.
         */
        public void setBoundAtEpochMillis(Long boundAtEpochMillis) {
            this.boundAtEpochMillis = boundAtEpochMillis;
        }

        /**
         * Retrieves data using the provided criteria and current business rules.
         * @return the computed `String` value for this operation.
         */
        public String getBoundDeviceName() {
            return boundDeviceName;
        }

        /**
         * Updates existing state while preserving consistency constraints.
         * @param boundDeviceName input parameter of type {@code String}.
         */
        public void setBoundDeviceName(String boundDeviceName) {
            this.boundDeviceName = boundDeviceName;
        }
    }
}
