package com.group27.tarecruitment.service;

import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.UserRole;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.VacancyRepository;
import com.group27.tarecruitment.util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * VacancyService class type.
 *
 * <p>Service type that centralizes business rules and multi-step domain workflows.</p>
 * <p>Package: {@code com.group27.tarecruitment.service}</p>
 */
public class VacancyService {
    private static final String STATUS_OPEN = "OPEN";
    private static final String STATUS_ARCHIVED = "ARCHIVED";
    private static final String CAMPUS_XTC = "Xitucheng Campus";
    private static final String CAMPUS_SH = "Shahe Campus";
    private static final Set<String> SUPPORTED_CAMPUSES = Set.of(
            CAMPUS_XTC.toLowerCase(),
            CAMPUS_SH.toLowerCase()
    );

    private final VacancyRepository vacancyRepository = new VacancyRepository();

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<Vacancy> getOpenVacancies() {
        return vacancyRepository.findAll().stream()
                .filter(vacancy -> STATUS_OPEN.equalsIgnoreCase(vacancy.getStatus()))
                .toList();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param vacancyId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<Vacancy> getVacancy(String vacancyId) {
        return vacancyRepository.findById(vacancyId);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @param moduleCode input parameter of type {@code String}.
     * @param moduleName input parameter of type {@code String}.
     * @param campus input parameter of type {@code String}.
     * @param description input parameter of type {@code String}.
     * @param requiredSkills input parameter of type {@code String}.
     * @param preferredBackground input parameter of type {@code String}.
     * @param workloadValue input parameter of type {@code String}.
     * @param positionCount input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    public String validateNewVacancy(UserAccount currentUser,
                                     String moduleCode,
                                     String moduleName,
                                     String campus,
                                     String description,
                                     String requiredSkills,
                                     String preferredBackground,
                                     String workloadValue,
                                     String positionCount) {
        if (currentUser == null || currentUser.getRole() != UserRole.MO) {
            return "Only organiser accounts can publish course jobs.";
        }
        if (ValidationUtil.isBlank(moduleCode)) {
            return "Module code is required.";
        }
        if (ValidationUtil.isBlank(moduleName)) {
            return "Module name is required.";
        }
        if (ValidationUtil.isBlank(campus)) {
            return "Campus is required.";
        }
        String normalizedCampus = normalizeCampus(campus);
        if (normalizedCampus == null) {
            return "Campus must be Xitucheng Campus or Shahe Campus.";
        }
        if (ValidationUtil.isBlank(description)) {
            return "Course job description is required.";
        }
        if (ValidationUtil.splitCsv(requiredSkills).isEmpty()) {
            return "At least one required skill must be provided.";
        }
        if (ValidationUtil.parsePositiveInt(workloadValue) == null) {
            return "Workload value must be a positive integer.";
        }
        if (ValidationUtil.parsePositiveInt(positionCount) == null) {
            return "TA places must be a positive integer.";
        }
        if (hasDuplicateVacancy(moduleCode, moduleName, normalizedCampus)) {
            return "A course job with the same module code, module name, and campus already exists.";
        }
        return null;
    }

    /**
     * Creates and initializes new business data for downstream use.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @param moduleCode input parameter of type {@code String}.
     * @param moduleName input parameter of type {@code String}.
     * @param campus input parameter of type {@code String}.
     * @param description input parameter of type {@code String}.
     * @param requiredSkills input parameter of type {@code String}.
     * @param preferredBackground input parameter of type {@code String}.
     * @param workloadValue input parameter of type {@code String}.
     * @param positionCount input parameter of type {@code String}.
     * @param leaderRoleAvailable input parameter of type {@code boolean}.
     * @return the computed `Vacancy` value for this operation.
     */
    public Vacancy createVacancy(UserAccount currentUser,
                                 String moduleCode,
                                 String moduleName,
                                 String campus,
                                 String description,
                                 String requiredSkills,
                                 String preferredBackground,
                                 String workloadValue,
                                 String positionCount,
                                 boolean leaderRoleAvailable) {
        List<Vacancy> vacancies = new ArrayList<>(vacancyRepository.findAll());

        Vacancy vacancy = new Vacancy();
        vacancy.setVacancyId("vac-" + UUID.randomUUID().toString().substring(0, 8));
        vacancy.setModuleCode(ValidationUtil.trimToEmpty(moduleCode));
        vacancy.setModuleName(ValidationUtil.trimToEmpty(moduleName));
        vacancy.setCampus(normalizeCampus(campus));
        vacancy.setTitle("Teaching Assistant Team");
        vacancy.setDescription(ValidationUtil.trimToEmpty(description));
        vacancy.setRequiredSkills(ValidationUtil.splitCsv(requiredSkills));
        vacancy.setPreferredBackground(ValidationUtil.trimToEmpty(preferredBackground));
        vacancy.setWorkloadValue(ValidationUtil.parsePositiveInt(workloadValue));
        vacancy.setPositionCount(ValidationUtil.parsePositiveInt(positionCount));
        vacancy.setLeaderRoleAvailable(leaderRoleAvailable);
        vacancy.setDeadline("");
        vacancy.setStatus(STATUS_OPEN);
        vacancy.setCreatedBy(currentUser.getUsername());
        vacancy.setApplicantCount(0);

        vacancies.add(vacancy);
        vacancyRepository.saveAll(vacancies);
        return vacancy;
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param campus input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String normalizeCampus(String campus) {
        String value = ValidationUtil.trimToEmpty(campus);
        if (value.isEmpty()) {
            return null;
        }
        String lowered = value.toLowerCase();
        if (!SUPPORTED_CAMPUSES.contains(lowered)) {
            return null;
        }
        return CAMPUS_XTC.toLowerCase().equals(lowered) ? CAMPUS_XTC : CAMPUS_SH;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param moduleCode input parameter of type {@code String}.
     * @param moduleName input parameter of type {@code String}.
     * @param campus input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean hasDuplicateVacancy(String moduleCode, String moduleName, String campus) {
        String normalizedModuleCode = normalizeText(moduleCode);
        String normalizedModuleName = normalizeText(moduleName);
        return vacancyRepository.findAll().stream()
                .anyMatch(existing ->
                        !isArchived(existing.getStatus())
                                && normalizedModuleCode.equals(normalizeText(existing.getModuleCode()))
                                && normalizedModuleName.equals(normalizeText(existing.getModuleName()))
                                && ValidationUtil.trimToEmpty(campus)
                                .equalsIgnoreCase(ValidationUtil.trimToEmpty(existing.getCampus())));
    }

    /**
     * Removes, archives, or cancels previously created business state.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @param vacancyId input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    public String archiveVacancy(UserAccount currentUser, String vacancyId) {
        if (currentUser == null || currentUser.getRole() != UserRole.MO) {
            return "Only organiser accounts can archive course jobs.";
        }
        String normalizedVacancyId = ValidationUtil.trimToEmpty(vacancyId);
        if (normalizedVacancyId.isEmpty()) {
            return "Vacancy ID is required.";
        }

        List<Vacancy> vacancies = new ArrayList<>(vacancyRepository.findAll());
        Vacancy target = null;
        for (Vacancy vacancy : vacancies) {
            if (normalizedVacancyId.equals(vacancy.getVacancyId())) {
                target = vacancy;
                break;
            }
        }
        if (target == null) {
            return "The selected course job could not be found.";
        }
        if (!isOwnedBy(currentUser, target)) {
            return "You can only archive course jobs published by your organiser account.";
        }
        if (isArchived(target.getStatus())) {
            return "This course job is already archived.";
        }

        target.setStatus(STATUS_ARCHIVED);
        vacancyRepository.saveAll(vacancies);
        return null;
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param currentUser input parameter of type {@code UserAccount}.
     * @param vacancy input parameter of type {@code Vacancy}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isOwnedBy(UserAccount currentUser, Vacancy vacancy) {
        String createdBy = ValidationUtil.trimToEmpty(vacancy.getCreatedBy());
        return !createdBy.isEmpty()
                && (createdBy.equalsIgnoreCase(ValidationUtil.trimToEmpty(currentUser.getUsername()))
                || createdBy.equalsIgnoreCase(ValidationUtil.trimToEmpty(currentUser.getUserId())));
    }

    /**
     * Evaluates and returns a boolean condition for caller logic.
     * @param status input parameter of type {@code String}.
     * @return true when the condition is met; otherwise false.
     */
    private boolean isArchived(String status) {
        return STATUS_ARCHIVED.equalsIgnoreCase(ValidationUtil.trimToEmpty(status));
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param value input parameter of type {@code String}.
     * @return the computed `String` value for this operation.
     */
    private String normalizeText(String value) {
        return ValidationUtil.trimToEmpty(value).toLowerCase();
    }
}
