package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ApplicantProfileService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class ApplicantProfileService {
    private final ApplicantProfileRepository applicantProfileRepository = new ApplicantProfileRepository();

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @return the computed `ApplicantProfile` value for this operation.
     */
    public ApplicantProfile getOrCreateProfile(UserAccount currentUser) {
        return applicantProfileRepository.findByApplicantId(currentUser.getUserId())
                .orElseGet(() -> buildDefaultProfile(currentUser));
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param applicantId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<ApplicantProfile> findByApplicantId(String applicantId) {
        return applicantProfileRepository.findByApplicantId(applicantId);
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param applicantId input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean hasProfile(String applicantId) {
        return applicantProfileRepository.findByApplicantId(applicantId).isPresent();
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param applicantId input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isProfileReady(String applicantId) {
        return applicantProfileRepository.findByApplicantId(applicantId)
                .map(this::isProfileReady)
                .orElse(false);
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param profile input parameter of type {@code ApplicantProfile}.
     * @return true when the condition is met; otherwise false.
     */
    public boolean isProfileReady(ApplicantProfile profile) {
        return profile != null
                && !ValidationUtil.isBlank(profile.getFullName())
                && !ValidationUtil.isBlank(profile.getStudentId())
                && !ValidationUtil.isBlank(profile.getEmail())
                && ValidationUtil.isValidEmail(profile.getEmail());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param profile input parameter of type {@code ApplicantProfile}.
     */
    public void saveProfile(ApplicantProfile profile) {
        List<ApplicantProfile> profiles = new ArrayList<>(applicantProfileRepository.findAll());
        profiles.removeIf(existing -> existing.getApplicantId() != null
                && existing.getApplicantId().equals(profile.getApplicantId()));
        profiles.add(profile);
        applicantProfileRepository.saveAll(profiles);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @return the computed `ApplicantProfile` value for this operation.
     */
    private ApplicantProfile buildDefaultProfile(UserAccount currentUser) {
        ApplicantProfile profile = new ApplicantProfile();
        profile.setApplicantId(currentUser.getUserId());
        profile.setFullName(currentUser.getDisplayName());
        profile.setEmail(currentUser.getEmail());
        profile.setBlacklisted(false);
        return profile;
    }
}
