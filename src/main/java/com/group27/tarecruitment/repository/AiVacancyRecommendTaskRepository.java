package com.group27.tarecruitment.repository;

import com.group27.tarecruitment.model.AiVacancyRecommendTask;
import com.group27.tarecruitment.util.JsonFileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AiVacancyRecommendTaskRepository {
    private static final String TASKS_RESOURCE = "data/ai_vacancy_recommend_tasks.json";

    public List<AiVacancyRecommendTask> findAll() {
        return JsonFileUtil.readList(TASKS_RESOURCE, AiVacancyRecommendTask.class);
    }

    public Optional<AiVacancyRecommendTask> findById(String taskId) {
        return findAll().stream()
                .filter(task -> task.getTaskId() != null && task.getTaskId().equals(taskId))
                .findFirst();
    }

    public void save(AiVacancyRecommendTask task) {
        List<AiVacancyRecommendTask> all = new ArrayList<>(findAll());
        all.removeIf(item -> task.getTaskId().equals(item.getTaskId()));
        all.add(task);
        JsonFileUtil.writeList(TASKS_RESOURCE, all);
    }

    public void saveAll(List<AiVacancyRecommendTask> tasks) {
        JsonFileUtil.writeList(TASKS_RESOURCE, tasks);
    }
}

