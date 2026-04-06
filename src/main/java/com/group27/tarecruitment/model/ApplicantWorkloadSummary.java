package com.group27.tarecruitment.model;

import java.util.List;

public class ApplicantWorkloadSummary {
    private String applicantId;
    private String displayName;
    private String studentId;
    private String email;
    private int totalApplicationsCount;
    private int submittedCount;
    private int unsuccessfulCount;
    private int offeredCount;
    private int activeCount;
    private int maxWorkload;
    private boolean blacklisted;
    private boolean overloaded;
    private List<String> activeModules;

    public String getApplicantId() { return applicantId; }
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getTotalApplicationsCount() { return totalApplicationsCount; }
    public void setTotalApplicationsCount(int totalApplicationsCount) { this.totalApplicationsCount = totalApplicationsCount; }
    public int getSubmittedCount() { return submittedCount; }
    public void setSubmittedCount(int submittedCount) { this.submittedCount = submittedCount; }
    public int getUnsuccessfulCount() { return unsuccessfulCount; }
    public void setUnsuccessfulCount(int unsuccessfulCount) { this.unsuccessfulCount = unsuccessfulCount; }
    public int getOfferedCount() { return offeredCount; }
    public void setOfferedCount(int offeredCount) { this.offeredCount = offeredCount; }
    public int getActiveCount() { return activeCount; }
    public void setActiveCount(int activeCount) { this.activeCount = activeCount; }
    public int getMaxWorkload() { return maxWorkload; }
    public void setMaxWorkload(int maxWorkload) { this.maxWorkload = maxWorkload; }
    public boolean isBlacklisted() { return blacklisted; }
    public void setBlacklisted(boolean blacklisted) { this.blacklisted = blacklisted; }
    public boolean isOverloaded() { return overloaded; }
    public void setOverloaded(boolean overloaded) { this.overloaded = overloaded; }
    public List<String> getActiveModules() { return activeModules; }
    public void setActiveModules(List<String> activeModules) { this.activeModules = activeModules; }
}
