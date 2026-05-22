package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.util.JsonFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * AiImportTaskRepository class type.
 *
 * <p>Repository type that encapsulates persistence and query behavior.</p>
 * <p>Package: {@code com.group27.tarecruitment.repository}</p>
 */
public class AiImportTaskRepository {
    private static final String TASKS_RESOURCE = "data/ai_import_tasks.json";

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @return a collection containing the computed result elements.
     */
    public List<AiImportTask> findAll() {
        return JsonFileUtil.readList(TASKS_RESOURCE, AiImportTask.class);
    }

    /**
     * Retrieves data using the provided criteria and current business rules.
     * @param taskId input parameter of type {@code String}.
     * @return an optional result that is present when data is available.
     */
    public Optional<AiImportTask> findById(String taskId) {
        return findAll().stream()
                .filter(task -> task.getTaskId() != null && task.getTaskId().equals(taskId))
                .findFirst();
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param task input parameter of type {@code AiImportTask}.
     */
    public void save(AiImportTask task) {
        List<AiImportTask> all = new ArrayList<>(findAll());
        all.removeIf(item -> task.getTaskId().equals(item.getTaskId()));
        all.add(task);
        JsonFileUtil.writeList(TASKS_RESOURCE, all);
    }

    /**
     * Executes business behavior as part of the class contract.
     * @param tasks input parameter of type {@code List<AiImportTask>}.
     */
    public void saveAll(List<AiImportTask> tasks) {
        JsonFileUtil.writeList(TASKS_RESOURCE, tasks);
    }
}
