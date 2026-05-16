package com.group27.tarecruitment.service;

import com.group27.tarecruitment.util.ValidationUtil;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QuickLoginRequestService {
    private static final long REQUEST_TTL_MILLIS = 3 * 60 * 1000;
    private static final Map<String, QuickLoginRequest> REQUESTS = new ConcurrentHashMap<>();

    public QuickLoginRequest createRequest() {
        cleanup();
        String requestId = UUID.randomUUID().toString().replace("-", "");
        long now = Instant.now().toEpochMilli();
        QuickLoginRequest request = new QuickLoginRequest(requestId, Status.PENDING, now, now + REQUEST_TTL_MILLIS);
        REQUESTS.put(requestId, request);
        return request;
    }

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

    private void cleanup() {
        long now = Instant.now().toEpochMilli();
        REQUESTS.entrySet().removeIf(entry -> entry.getValue().getExpiresAtEpochMillis() + 60_000 < now);
    }

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

        public String getRequestId() {
            return requestId;
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

        public String getConfirmedUserId() {
            return confirmedUserId;
        }

        public void setConfirmedUserId(String confirmedUserId) {
            this.confirmedUserId = confirmedUserId;
        }

        public Long getConfirmedAtEpochMillis() {
            return confirmedAtEpochMillis;
        }

        public void setConfirmedAtEpochMillis(Long confirmedAtEpochMillis) {
            this.confirmedAtEpochMillis = confirmedAtEpochMillis;
        }
    }
}
