package com.qida.jarvislite.model;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private long id;
    private String title;
    private String status;
    private List<Step> steps = new ArrayList<>();

    public Task() {}

    public Task(long id, String title, String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) {
        this.steps = (steps == null) ? new ArrayList<>() : steps;
    }

    // 进度：已完成/总数（百分比在 controller 里算）
    public int getDoneCount() {
        int done = 0;
        for (Step s : steps) if (s != null && s.isDone()) done++;
        return done;
    }

    public int getTotalCount() {
        return steps == null ? 0 : steps.size();
    }
}
