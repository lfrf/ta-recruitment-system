package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.util.JsonFileUtil;
import java.util.List;
import java.util.Optional;

/**
 * ApplicantProfileRepository class type.
 *
 * <p>Repository type that encapsulates persistence and query behavior.</p>
 * <p>Package: {@code com.group27.tarecruitment.repository}</p>
 */
public class ApplicantProfileRepository {
    private static final String PROFILES_RESOURCE = "data/applicant_profiles.json";

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<ApplicantProfile> findAll() {
        return JsonFileUtil.readList(PROFILES_RESOURCE, ApplicantProfile.class);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param applicantId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<ApplicantProfile> findByApplicantId(String applicantId) {
        return findAll().stream()
                .filter(profile -> profile.getApplicantId() != null && profile.getApplicantId().equals(applicantId))
                .findFirst();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param profiles input parameter of type {@code List<ApplicantProfile>}.
     */
    public void saveAll(List<ApplicantProfile> profiles) {
        JsonFileUtil.writeList(PROFILES_RESOURCE, profiles);
    }
}
