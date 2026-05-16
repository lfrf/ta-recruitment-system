package com.group27.tarecruitment.model;

public class QuickLoginBinding {
    private String userId;
    private String bindToken;
    private String deviceName;
    private String boundAt;
    private boolean active;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBindToken() {
        return bindToken;
    }

    public void setBindToken(String bindToken) {
        this.bindToken = bindToken;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBoundAt() {
        return boundAt;
    }

    public void setBoundAt(String boundAt) {
        this.boundAt = boundAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
