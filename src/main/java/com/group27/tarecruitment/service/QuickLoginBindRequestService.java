package com.group27.tarecruitment.service;

import com.group27.tarecruitment.util.ValidationUtil;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QuickLoginBindRequestService {
    private static final long REQUEST_TTL_MILLIS = 3 * 60 * 1000;
    private static final Map<String, BindRequest> REQUESTS = new ConcurrentHashMap<>();

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

    private void cleanup() {
        long now = Instant.now().toEpochMilli();
        REQUESTS.entrySet().removeIf(entry -> entry.getValue().getExpiresAtEpochMillis() + 60_000 < now);
    }

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

        public String getRequestId() {
            return requestId;
        }

        public String getUserId() {
            return userId;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public long getCreatedAtEpochMillis() {
            return createdAtEpochMillis;
        }

        public long getExpiresAtEpochMillis() {
            return expiresAtEpochMillis;
        }

        public Long getBoundAtEpochMillis() {
            return boundAtEpochMillis;
        }

        public void setBoundAtEpochMillis(Long boundAtEpochMillis) {
            this.boundAtEpochMillis = boundAtEpochMillis;
        }

        public String getBoundDeviceName() {
            return boundDeviceName;
        }

        public void setBoundDeviceName(String boundDeviceName) {
            this.boundDeviceName = boundDeviceName;
        }
    }
}
