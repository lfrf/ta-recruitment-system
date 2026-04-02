package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.ApplicantWorkloadSummary;
import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.repository.ApplicationRepository;
import com.group27.tarecruitment.repository.BlacklistRepository;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorkloadService {
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();
    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    private final BlacklistRepository blacklistRepository = new BlacklistRepository();
    private final UserRepository userRepository = new UserRepository();
    private final VacancyRepository vacancyRepository = new VacancyRepository();

    public List<ApplicantWorkloadSummary> getApplicantSummaries(int maxWorkload) {
        return filterSummaries(maxWorkload, "", "", false);
    }

    public List<ApplicantWorkloadSummary> filterSummaries(int maxWorkload,
                                                          String applicantKeyword,
                                                          String moduleKeyword,
                                                          boolean flaggedOnly) {
        Map<String, ApplicantProfile> profileByApplicantId = new LinkedHashMap<>();
        for (ApplicantProfile profile : applicantProfileRepository.findAll()) {
            profileByApplicantId.put(profile.getApplicantId(), profile);
        }

        Map<String, UserAccount> userById = new LinkedHashMap<>();
        for (UserAccount user : userRepository.findAll()) {
            userById.put(user.getUserId(), user);
        }

        Map<String, Vacancy> vacancyById = new LinkedHashMap<>();
        for (Vacancy vacancy : vacancyRepository.findAll()) {
            vacancyById.put(vacancy.getVacancyId(), vacancy);
        }

        Set<String> blacklistedIds = new LinkedHashSet<>();
        for (BlacklistEntry entry : blacklistRepository.findActiveEntries()) {
            blacklistedIds.add(entry.getApplicantId());
        }

        Set<String> applicantIds = new LinkedHashSet<>();
        applicantIds.addAll(profileByApplicantId.keySet());
        applicantIds.addAll(blacklistedIds);
        for (ApplicationRecord application : applicationRepository.findAll()) {
            applicantIds.add(application.getApplicantId());
        }

        String normalizedApplicantKeyword = ValidationUtil.trimToEmpty(applicantKeyword).toLowerCase();
        String normalizedModuleKeyword = ValidationUtil.trimToEmpty(moduleKeyword).toLowerCase();

        List<ApplicantWorkloadSummary> summaries = new ArrayList<>();
        for (String applicantId : applicantIds) {
            ApplicantProfile profile = profileByApplicantId.get(applicantId);
            UserAccount user = userById.get(applicantId);
            int totalApplicationsCount = 0;
            int submittedCount = 0;
            int unsuccessfulCount = 0;
            int offeredCount = 0;
            int activeCount = 0;
            Set<String> activeModules = new LinkedHashSet<>();

            for (ApplicationRecord application : applicationRepository.findByApplicantId(applicantId)) {
                totalApplicationsCount++;
                if ("Submitted".equalsIgnoreCase(application.getStatus())) {
                    submittedCount++;
                    activeCount++;
                }
                if ("Offered".equalsIgnoreCase(application.getStatus())) {
                    offeredCount++;
                    activeCount++;
                }
                if ("Unsuccessful".equalsIgnoreCase(application.getStatus())) {
                    unsuccessfulCount++;
                }
                if (!"Unsuccessful".equalsIgnoreCase(application.getStatus())) {
                    Vacancy vacancy = vacancyById.get(application.getVacancyId());
                    if (vacancy != null) {
                        activeModules.add(vacancy.getModuleCode() + " - " + vacancy.getModuleName());
                    }
                }
            }

            ApplicantWorkloadSummary summary = new ApplicantWorkloadSummary();
            summary.setApplicantId(applicantId);
            summary.setDisplayName(profile != null && profile.getFullName() != null && !profile.getFullName().isBlank()
                    ? profile.getFullName()
                    : user != null ? user.getDisplayName() : applicantId);
            summary.setStudentId(profile != null ? profile.getStudentId() : "");
            summary.setEmail(profile != null && profile.getEmail() != null && !profile.getEmail().isBlank()
                    ? profile.getEmail()
                    : user != null ? user.getEmail() : "");
            summary.setTotalApplicationsCount(totalApplicationsCount);
            summary.setSubmittedCount(submittedCount);
            summary.setUnsuccessfulCount(unsuccessfulCount);
            summary.setOfferedCount(offeredCount);
            summary.setActiveCount(activeCount);
            summary.setMaxWorkload(maxWorkload);
            summary.setBlacklisted(blacklistedIds.contains(applicantId) || (profile != null && profile.isBlacklisted()));
            summary.setOverloaded(activeCount > maxWorkload);
            summary.setActiveModules(new ArrayList<>(activeModules));

            if (!matchesApplicant(summary, normalizedApplicantKeyword)) {
                continue;
            }
            if (!matchesModule(summary, normalizedModuleKeyword)) {
                continue;
            }
            if (flaggedOnly && !summary.isBlacklisted() && !summary.isOverloaded()) {
                continue;
            }

            summaries.add(summary);
        }

        summaries.sort(Comparator.comparing(ApplicantWorkloadSummary::getDisplayName, String.CASE_INSENSITIVE_ORDER));
        return summaries;
    }

    public Map<String, Integer> getActiveCountByApplicantId() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (ApplicationRecord application : applicationRepository.findAll()) {
            if (!"Unsuccessful".equalsIgnoreCase(application.getStatus())) {
                counts.merge(application.getApplicantId(), 1, Integer::sum);
            }
        }
        return counts;
    }

    private boolean matchesApplicant(ApplicantWorkloadSummary summary, String keyword) {
        if (keyword.isBlank()) {
            return true;
        }
        return contains(summary.getDisplayName(), keyword)
                || contains(summary.getStudentId(), keyword)
                || contains(summary.getEmail(), keyword);
    }

    private boolean matchesModule(ApplicantWorkloadSummary summary, String keyword) {
        if (keyword.isBlank()) {
            return true;
        }
        for (String module : summary.getActiveModules()) {
            if (contains(module, keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}
