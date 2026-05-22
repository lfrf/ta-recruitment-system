package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.ApplicantProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ApplicantProfileServiceTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class ApplicantProfileServiceTest {

    private final ApplicantProfileService service = new ApplicantProfileService();

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void profileReadyShouldBeTrueWhenRequiredFieldsAreValid() {
        ApplicantProfile profile = new ApplicantProfile();
        profile.setFullName("Alice Zhang");
        profile.setStudentId("S1234567");
        profile.setEmail("alice.zhang@example.com");

        assertTrue(service.isProfileReady(profile));
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    @Test
    void profileReadyShouldBeFalseWhenRequiredFieldsAreMissingOrInvalid() {
        ApplicantProfile missingName = new ApplicantProfile();
        missingName.setStudentId("S1234567");
        missingName.setEmail("alice.zhang@example.com");
        assertFalse(service.isProfileReady(missingName));

        ApplicantProfile invalidEmail = new ApplicantProfile();
        invalidEmail.setFullName("Alice Zhang");
        invalidEmail.setStudentId("S1234567");
        invalidEmail.setEmail("alice-at-example.com");
        assertFalse(service.isProfileReady(invalidEmail));
    }
}

