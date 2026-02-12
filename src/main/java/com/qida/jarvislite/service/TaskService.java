package com.qida.jarvislite.service;

import com.qida.jarvislite.model.Step;
import com.qida.jarvislite.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public Task create(String title) {
        Task t = new Task(idGen.getAndIncrement(), title, "TODO");
        tasks.add(t);
        return t;
    }

    public List<Task> list() {
        return tasks;
    }

    public Task findById(long id) {
        for (Task t : tasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    public Task setStepDone(long taskId, int stepIndex, boolean done) {
        Task t = findById(taskId);
        if (t == null) return null;

        List<Step> steps = t.getSteps();
        if (steps == null || stepIndex < 0 || stepIndex >= steps.size()) return null;

        steps.get(stepIndex).setDone(done);

        // ✅ 每次 step 变化后，自动刷新 status
        refreshStatus(t);

        return t;
    }

    // ✅ 根据 steps 自动更新 status
    public void refreshStatus(Task t) {
        if (t == null) return;

        int total = t.getTotalCount();
        int done = t.getDoneCount();

        if (total > 0 && done == total) {
            t.setStatus("DONE");
        } else {
            t.setStatus("TODO");
        }
    }
}
