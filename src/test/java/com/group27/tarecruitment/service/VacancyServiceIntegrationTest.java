package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.support.TestDataSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * VacancyServiceIntegrationTest class type.
 *
 * <p>Test type used to verify behavior, edge cases, and regression safety.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
class VacancyServiceIntegrationTest {

    private final VacancyService vacancyService = new VacancyService();
    private final VacancyRepository vacancyRepository = new VacancyRepository();

    /**
     * Updates existing state while preserving consistency constraints.
     */
    @BeforeEach
    void setUp() {
        TestDataSupport.resetRuntimeDataDir();
        TestDataSupport.seedVacancies(List.of(existingVacancy("vac-existing", "EBU6304", "Software Engineering",
                "Shahe Campus", "mo01", "OPEN")));
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void validateNewVacancyShouldRejectDuplicateModuleInSameCampus() {
        String error = vacancyService.validateNewVacancy(
                moUser("mo01"),
                "EBU6304",
                "Software Engineering",
                "Shahe Campus",
                "Support labs.",
                "Java",
                "",
                "8",
                "2");
        assertEquals("A course job with the same module code, module name, and campus already exists.", error);
    }

    /**
     * Applies review or decision outcomes and related status changes.
     */
    @Test
    void validateNewVacancyShouldRejectInvalidCampusAndSkillInput() {
        String campusError = vacancyService.validateNewVacancy(
                moUser("mo01"),
                "EBU7000",
                "Distributed Systems",
                "Unknown Campus",
                "Support coursework.",
                "Java",
                "",
                "8",
                "2");
        assertEquals("Campus must be Xitucheng Campus or Shahe Campus.", campusError);

        String skillError = vacancyService.validateNewVacancy(
                moUser("mo01"),
                "EBU7001",
                "Networks",
                "Shahe Campus",
                "Support coursework.",
                "   ",
                "",
                "8",
                "2");
        assertEquals("At least one required skill must be provided.", skillError);
    }

    /**
     * Creates and initializes new business data for downstream use.
     */
    @Test
    void createAndArchiveVacancyShouldPersistAndUpdateStatus() {
        String validation = vacancyService.validateNewVacancy(
                moUser("mo01"),
                "EBU7002",
                "Cloud Computing",
                "Xitucheng Campus",
                "Support practical sessions.",
                "Java, Docker",
                "Teaching support",
                "10",
                "3");
        assertNull(validation);

        Vacancy created = vacancyService.createVacancy(
                moUser("mo01"),
                "EBU7002",
                "Cloud Computing",
                "Xitucheng Campus",
                "Support practical sessions.",
                "Java, Docker",
                "Teaching support",
                "10",
                "3",
                true);

        assertNotNull(created);
        assertEquals("OPEN", created.getStatus());
        assertEquals("Xitucheng Campus", created.getCampus());
        assertEquals(2, vacancyRepository.findAll().size());

        String archiveError = vacancyService.archiveVacancy(moUser("mo01"), created.getVacancyId());
        assertNull(archiveError);

        Vacancy archived = vacancyRepository.findById(created.getVacancyId()).orElse(null);
        assertNotNull(archived);
        assertEquals("ARCHIVED", archived.getStatus());
    }

    /**
     * Removes, archives, or cancels previously created business state.
     */
    @Test
    void archiveVacancyShouldRejectWrongOwner() {
        String error = vacancyService.archiveVacancy(moUser("mo02"), "vac-existing");
        assertEquals("You can only archive course jobs published by your organiser account.", error);
        assertTrue(vacancyRepository.findById("vac-existing").isPresent());
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param username input parameter of type {@code String}.
     * @return the computed `UserAccount` value for this operation.
     */
    private UserAccount moUser(String username) {
        UserAccount user = new UserAccount();
        user.setUserId(username);
        user.setUsername(username);
        user.setRole(UserRole.MO);
        user.setDisplayName(username);
        user.setActive(true);
        return user;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancyId input parameter of type {@code String}.
     * @param moduleCode input parameter of type {@code String}.
     * @param moduleName input parameter of type {@code String}.
     * @param campus input parameter of type {@code String}.
     * @param createdBy input parameter of type {@code String}.
     * @param status input parameter of type {@code String}.
     * @return the computed `Vacancy` value for this operation.
     */
    private Vacancy existingVacancy(String vacancyId,
                                    String moduleCode,
                                    String moduleName,
                                    String campus,
                                    String createdBy,
                                    String status) {
        Vacancy vacancy = new Vacancy();
        vacancy.setVacancyId(vacancyId);
        vacancy.setModuleCode(moduleCode);
        vacancy.setModuleName(moduleName);
        vacancy.setCampus(campus);
        vacancy.setStatus(status);
        vacancy.setCreatedBy(createdBy);
        vacancy.setDescription("Existing course job");
        vacancy.setRequiredSkills(List.of("Java"));
        vacancy.setWorkloadValue(8);
        vacancy.setPositionCount(2);
        vacancy.setLeaderRoleAvailable(true);
        return vacancy;
    }
}
