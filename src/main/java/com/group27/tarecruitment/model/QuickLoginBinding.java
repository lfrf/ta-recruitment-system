package com.group27.tarecruitment.model;

/**
 * QuickLoginBinding class type.
 *
 * <p>Model type that represents structured domain data and state fields.</p>
 * <p>Package: {@code com.group27.tarecruitment.model}</p>
 */
public class QuickLoginBinding {
    private String userId;
    private String bindToken;
    private String deviceName;
    private String boundAt;
    private boolean active;

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param userId input parameter of type {@code String}.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getBindToken() {
        return bindToken;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param bindToken input parameter of type {@code String}.
     */
    public void setBindToken(String bindToken) {
        this.bindToken = bindToken;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param deviceName input parameter of type {@code String}.
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return the computed `String` value for this operation.
     */
    public String getBoundAt() {
        return boundAt;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param boundAt input parameter of type {@code String}.
     */
    public void setBoundAt(String boundAt) {
        this.boundAt = boundAt;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Updates existing state while preserving consistency constraints.
     * @param active input parameter of type {@code boolean}.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
