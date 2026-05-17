package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.AiImportTask;
import com.group27.tarecruitment.util.JsonFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AiImportTaskRepository {
    private static final String TASKS_RESOURCE = "data/ai_import_tasks.json";

    public List<AiImportTask> findAll() {
        return JsonFileUtil.readList(TASKS_RESOURCE, AiImportTask.class);
    }

    public Optional<AiImportTask> findById(String taskId) {
        return findAll().stream()
                .filter(task -> task.getTaskId() != null && task.getTaskId().equals(taskId))
                .findFirst();
    }

    public void save(AiImportTask task) {
        List<AiImportTask> all = new ArrayList<>(findAll());
        all.removeIf(item -> task.getTaskId().equals(item.getTaskId()));
        all.add(task);
        JsonFileUtil.writeList(TASKS_RESOURCE, all);
    }

    public void saveAll(List<AiImportTask> tasks) {
        JsonFileUtil.writeList(TASKS_RESOURCE, tasks);
    }
}
