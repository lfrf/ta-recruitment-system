package com.group27.tarecruitment.support;

import com.group27.tarecruitment.model.AdminConfig;
import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.model.ApplicantProfile;
import com.group27.tarecruitment.model.ApplicationRecord;
import com.group27.tarecruitment.model.BlacklistEntry;
import com.group27.tarecruitment.model.UserAccount;
import com.group27.tarecruitment.model.Vacancy;
import com.group27.tarecruitment.repository.AdminConfigRepository;
import com.group27.tarecruitment.repository.AiImportTaskRepository;
import com.group27.tarecruitment.repository.ApplicantProfileRepository;
import com.group27.tarecruitment.repository.ApplicationRepository;
import com.group27.tarecruitment.repository.BlacklistRepository;
import com.group27.tarecruitment.repository.UserRepository;
import com.group27.tarecruitment.repository.VacancyRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TestDataSupport {
    private static final Path TEST_RUNTIME_DATA_DIR = Path.of("target", "test-runtime-data").toAbsolutePath();

    static {
        System.setProperty("ta.recruitment.dataDir", TEST_RUNTIME_DATA_DIR.toString());
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    private TestDataSupport() {
    }

    /**
     * Executes business behavior as part of the class contract.
     */
    public static void resetRuntimeDataDir() {
        try {
            if (Files.exists(TEST_RUNTIME_DATA_DIR)) {
                List<Path> allPaths = new ArrayList<>();
                try (var stream = Files.walk(TEST_RUNTIME_DATA_DIR)) {
                    stream.sorted(Comparator.reverseOrder()).forEach(allPaths::add);
                }
                for (Path path : allPaths) {
                    Files.deleteIfExists(path);
                }
            }
            Files.createDirectories(TEST_RUNTIME_DATA_DIR);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to reset test runtime data directory.", exception);
        }
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param maxWorkload input parameter of type {@code int}.
     * @param allowVisitorBrowsing input parameter of type {@code boolean}.
     */
    public static void seedAdminConfig(int maxWorkload, boolean allowVisitorBrowsing) {
        AdminConfig config = new AdminConfig();
        config.setMaxWorkload(maxWorkload);
        config.setAllowVisitorBrowsing(allowVisitorBrowsing);
        new AdminConfigRepository().save(config);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param users input parameter of type {@code List<UserAccount>}.
     */
    public static void seedUsers(List<UserAccount> users) {
        new UserRepository().saveAll(users);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param vacancies input parameter of type {@code List<Vacancy>}.
     */
    public static void seedVacancies(List<Vacancy> vacancies) {
        new VacancyRepository().saveAll(vacancies);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param applications input parameter of type {@code List<ApplicationRecord>}.
     */
    public static void seedApplications(List<ApplicationRecord> applications) {
        new ApplicationRepository().saveAll(applications);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param profiles input parameter of type {@code List<ApplicantProfile>}.
     */
    public static void seedProfiles(List<ApplicantProfile> profiles) {
        new ApplicantProfileRepository().saveAll(profiles);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param entries input parameter of type {@code List<BlacklistEntry>}.
     */
    public static void seedBlacklist(List<BlacklistEntry> entries) {
        new BlacklistRepository().saveAll(entries);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param tasks input parameter of type {@code List<AiImportTask>}.
     */
    public static void seedAiImportTasks(List<AiImportTask> tasks) {
        new AiImportTaskRepository().saveAll(tasks);
    }
}
