package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplicantProfileService {
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();

    public ApplicantProfile getOrCreateProfile(UserAccount currentUser) {
        return applicantProfileRepository.findByApplicantId(currentUser.getUserId())
                .orElseGet(() -> buildDefaultProfile(currentUser));
    }

    public Optional<ApplicantProfile> findByApplicantId(String applicantId) {
        return applicantProfileRepository.findByApplicantId(applicantId);
    }

    public boolean hasProfile(String applicantId) {
        return applicantProfileRepository.findByApplicantId(applicantId).isPresent();
    }

    public void saveProfile(ApplicantProfile profile) {
        List<ApplicantProfile> profiles = new ArrayList<>(applicantProfileRepository.findAll());
        profiles.removeIf(existing -> existing.getApplicantId() != null
                && existing.getApplicantId().equals(profile.getApplicantId()));
        profiles.add(profile);
        applicantProfileRepository.saveAll(profiles);
    }

    private ApplicantProfile buildDefaultProfile(UserAccount currentUser) {
        ApplicantProfile profile = new ApplicantProfile();
        profile.setApplicantId(currentUser.getUserId());
        profile.setFullName(currentUser.getDisplayName());
        profile.setEmail(currentUser.getEmail());
        profile.setBlacklisted(false);
        return profile;
    }
}
