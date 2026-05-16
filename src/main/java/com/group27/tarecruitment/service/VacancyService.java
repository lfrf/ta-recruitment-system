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

    public List<Vacancy> getOpenVacancies() {
        return vacancyRepository.findAll().stream()
                .filter(vacancy -> STATUS_OPEN.equalsIgnoreCase(vacancy.getStatus()))
                .toList();
    }

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    public Optional<Vacancy> getVacancy(String vacancyId) {
        return vacancyRepository.findById(vacancyId);
    }

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

    private boolean isOwnedBy(UserAccount currentUser, Vacancy vacancy) {
        String createdBy = ValidationUtil.trimToEmpty(vacancy.getCreatedBy());
        return !createdBy.isEmpty()
                && (createdBy.equalsIgnoreCase(ValidationUtil.trimToEmpty(currentUser.getUsername()))
                || createdBy.equalsIgnoreCase(ValidationUtil.trimToEmpty(currentUser.getUserId())));
    }

    private boolean isArchived(String status) {
        return STATUS_ARCHIVED.equalsIgnoreCase(ValidationUtil.trimToEmpty(status));
    }

    private String normalizeText(String value) {
        return ValidationUtil.trimToEmpty(value).toLowerCase();
    }
}
