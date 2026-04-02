package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;
import java.util.Optional;

public class ApplicantProfileRepository {
    private static final String PROFILES_RESOURCE = "data/applicant_profiles.json";

    public List<ApplicantProfile> findAll() {
        return JsonFileUtil.readList(PROFILES_RESOURCE, ApplicantProfile.class);
    }

    public Optional<ApplicantProfile> findByApplicantId(String applicantId) {
        return findAll().stream()
                .filter(profile -> profile.getApplicantId() != null && profile.getApplicantId().equals(applicantId))
                .findFirst();
    }

    public void saveAll(List<ApplicantProfile> profiles) {
        JsonFileUtil.writeList(PROFILES_RESOURCE, profiles);
    }
}
