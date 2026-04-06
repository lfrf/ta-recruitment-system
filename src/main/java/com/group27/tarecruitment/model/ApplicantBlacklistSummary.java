package com.group27.tarecruitment.model;

public class ApplicantBlacklistSummary {
    private String applicantId;
    private int listedTimes;
    private boolean active;
    private String activeEntryId;
    private String latestReason;
    private String latestCreatedAt;
    private String latestCreatedBy;

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public int getListedTimes() {
        return listedTimes;
    }

    public void setListedTimes(int listedTimes) {
        this.listedTimes = listedTimes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActiveEntryId() {
        return activeEntryId;
    }

    public void setActiveEntryId(String activeEntryId) {
        this.activeEntryId = activeEntryId;
    }

    public String getLatestReason() {
        return latestReason;
    }

    public void setLatestReason(String latestReason) {
        this.latestReason = latestReason;
    }

    public String getLatestCreatedAt() {
        return latestCreatedAt;
    }

    public void setLatestCreatedAt(String latestCreatedAt) {
        this.latestCreatedAt = latestCreatedAt;
    }

    public String getLatestCreatedBy() {
        return latestCreatedBy;
    }

    public void setLatestCreatedBy(String latestCreatedBy) {
        this.latestCreatedBy = latestCreatedBy;
    }
}