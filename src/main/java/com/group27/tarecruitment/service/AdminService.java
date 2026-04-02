package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.model.ApplicantBlacklistSummary;
import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.repository.AdminConfigRepository;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.repository.BlacklistRepository;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdminService {
    private final AdminConfigRepository adminConfigRepository = new AdminConfigRepository();
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();
    private final BlacklistRepository blacklistRepository = new BlacklistRepository();
    private final UserRepository userRepository = new UserRepository();

    public AdminConfig getConfig() {
        return adminConfigRepository.load();
    }

    public String updateConfig(String maxWorkloadValue, boolean allowVisitorBrowsing) {
        Integer maxWorkload = ValidationUtil.parsePositiveInt(maxWorkloadValue);
        if (maxWorkload == null) {
            return "Max workload must be a valid positive integer.";
        }
        if (maxWorkload < 1 || maxWorkload > 10) {
            return "Max workload should be between 1 and 10 roles.";
        }

        AdminConfig config = adminConfigRepository.load();
        config.setMaxWorkload(maxWorkload);
        config.setAllowVisitorBrowsing(allowVisitorBrowsing);
        adminConfigRepository.save(config);
        return null;
    }

    public List<ApplicantBlacklistSummary> getBlacklistSummaries() {
        List<BlacklistEntry> entries = blacklistRepository.findAll();
        Map<String, ApplicantBlacklistSummary> summaryByApplicantId = new LinkedHashMap<>();

        for (BlacklistEntry entry : entries) {
            ApplicantBlacklistSummary summary = summaryByApplicantId.computeIfAbsent(entry.getApplicantId(), applicantId -> {
                ApplicantBlacklistSummary created = new ApplicantBlacklistSummary();
                created.setApplicantId(applicantId);
                created.setListedTimes(0);
                return created;
            });

            summary.setListedTimes(summary.getListedTimes() + 1);

            if (entry.isActive()) {
                summary.setActive(true);
                summary.setActiveEntryId(entry.getEntryId());
            }

            if (summary.getLatestCreatedAt() == null
                    || (entry.getCreatedAt() != null && entry.getCreatedAt().compareTo(summary.getLatestCreatedAt()) > 0)) {
                summary.setLatestCreatedAt(entry.getCreatedAt());
                summary.setLatestCreatedBy(entry.getCreatedBy());
                summary.setLatestReason(entry.getReason());
            }
        }

        return summaryByApplicantId.values().stream()
                .sorted(Comparator.comparing(ApplicantBlacklistSummary::getLatestCreatedAt, Comparator.nullsLast(String::compareTo)).reversed())
                .toList();
    }

    public List<UserAccount> findApplicantAccounts() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.APPLICANT)
                .filter(UserAccount::isActive)
                .sorted(Comparator.comparing(UserAccount::getDisplayName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .toList();
    }

    public String addBlacklistEntry(UserAccount adminUser, String applicantId, String reason, boolean confirmed) {
        if (adminUser == null || adminUser.getRole() != UserRole.ADMIN) {
            return "Only admin accounts can update the blacklist.";
        }
        if (ValidationUtil.isBlank(applicantId)) {
            return "Please select an applicant to add to the blacklist.";
        }
        if (!confirmed) {
            return "Please confirm the selected applicant before adding a blacklist entry.";
        }
        if (ValidationUtil.isBlank(reason)) {
            return "Please provide a blacklist reason.";
        }

        List<BlacklistEntry> entries = new ArrayList<>(blacklistRepository.findAll());
        boolean alreadyActive = entries.stream()
                .anyMatch(entry -> entry.isActive() && applicantId.equals(entry.getApplicantId()));
        if (alreadyActive) {
            return "The selected applicant already has an active blacklist entry.";
        }

        BlacklistEntry entry = new BlacklistEntry();
        entry.setEntryId("bl-" + UUID.randomUUID().toString().substring(0, 8));
        entry.setApplicantId(applicantId);
        entry.setReason(ValidationUtil.trimToEmpty(reason));
        entry.setCreatedAt(LocalDateTime.now().toString());
        entry.setCreatedBy(adminUser.getUsername());
        entry.setActive(true);
        entries.add(entry);
        blacklistRepository.saveAll(entries);

        updateApplicantBlacklistFlag(applicantId, true);
        return null;
    }

    public String deactivateBlacklistEntry(String entryId) {
        if (ValidationUtil.isBlank(entryId)) {
            return "A blacklist entry ID is required.";
        }

        List<BlacklistEntry> entries = new ArrayList<>(blacklistRepository.findAll());
        BlacklistEntry target = null;
        for (BlacklistEntry entry : entries) {
            if (entryId.equals(entry.getEntryId())) {
                target = entry;
                break;
            }
        }

        if (target == null) {
            return "The selected blacklist entry could not be found.";
        }

        target.setActive(false);
        blacklistRepository.saveAll(entries);

        String applicantId = target.getApplicantId();
        boolean stillBlacklisted = entries.stream()
                .anyMatch(entry -> entry.isActive() && applicantId.equals(entry.getApplicantId()));
        updateApplicantBlacklistFlag(applicantId, stillBlacklisted);
        return null;
    }

    private void updateApplicantBlacklistFlag(String applicantId, boolean blacklisted) {
        List<ApplicantProfile> profiles = new ArrayList<>(applicantProfileRepository.findAll());
        boolean updated = false;
        for (ApplicantProfile profile : profiles) {
            if (applicantId.equals(profile.getApplicantId())) {
                profile.setBlacklisted(blacklisted);
                updated = true;
            }
        }
        if (updated) {
            applicantProfileRepository.saveAll(profiles);
        }
    }
}
