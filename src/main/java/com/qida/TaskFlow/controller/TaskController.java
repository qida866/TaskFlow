package com.qida.TaskFlow.controller;

import com.qida.TaskFlow.model.Step;
import com.qida.TaskFlow.model.Task;
import com.qida.TaskFlow.planner.TaskPlanner;
import com.qida.TaskFlow.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static class CreateTaskRequest {
        public String title;
    }

    private final TaskService taskService;
    private final TaskPlanner planner = new TaskPlanner();

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // POST /tasks?autoPlan=true
    @PostMapping
    public Task create(
            @RequestBody CreateTaskRequest req,
            @RequestParam(name = "autoPlan", defaultValue = "false") boolean autoPlan
    ) {
        Task task = taskService.create(req.title);
        if (autoPlan) {
            task.setSteps(planner.decompose(task.getTitle()));
        }
        return task;
    }

    @GetMapping
    public List<Task> list() {
        return taskService.list();
    }

    // 保持旧接口：仍然返回字符串列表（不返回 done）
    @PostMapping("/decompose")
    public List<String> decompose(@RequestBody DecomposeRequest req) {
        List<Step> steps = planner.decompose(req.getTitle());
        List<String> out = new ArrayList<>();
        for (Step s : steps) out.add(s.getText());
        return out;
    }

    // ✅ 新接口：标记某个 step 完成/未完成
    // PATCH /tasks/{id}/steps/{index}?done=true
    @PatchMapping("/{id}/steps/{index}")
    public ResponseEntity<Task> setStepDone(
            @PathVariable long id,
            @PathVariable int index,
            @RequestParam(name = "done") boolean done
    ) {
        Task updated = taskService.setStepDone(id, index, done);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // ✅ 新接口：查看进度
    // GET /tasks/{id}/progress
    @GetMapping("/{id}/progress")
    public ResponseEntity<Map<String, Object>> progress(@PathVariable long id) {
        Task t = taskService.findById(id);
        if (t == null) return ResponseEntity.notFound().build();

        int total = t.getTotalCount();
        int done = t.getDoneCount();
        int percent = (total == 0) ? 0 : (done * 100 / total);

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("taskId", t.getId());
        res.put("title", t.getTitle());
        res.put("done", done);
        res.put("total", total);
        res.put("percent", percent);
        return ResponseEntity.ok(res);
    }
    // ✅ 新接口 1：一键切换 done（true <-> false）
// PATCH /tasks/{id}/steps/{index}/toggle
    @PatchMapping("/{id}/steps/{index}/toggle")
    public ResponseEntity<Task> toggleStep(@PathVariable long id, @PathVariable int index) {
        Task t = taskService.findById(id);
        if (t == null) return ResponseEntity.notFound().build();

        if (t.getSteps() == null || index < 0 || index >= t.getSteps().size()) {
            return ResponseEntity.badRequest().build();
        }

        boolean current = t.getSteps().get(index).isDone();
        Task updated = taskService.setStepDone(id, index, !current);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // ✅ 新接口 2：重置任务进度（全部 steps.done = false）
// POST /tasks/{id}/reset
    @PostMapping("/{id}/reset")
    public ResponseEntity<Task> reset(@PathVariable long id) {
        Task t = taskService.findById(id);
        if (t == null) return ResponseEntity.notFound().build();

        if (t.getSteps() != null) {
            for (int i = 0; i < t.getSteps().size(); i++) {
                taskService.setStepDone(id, i, false);
            }
        }
        taskService.refreshStatus(t);
        Task latest = taskService.findById(id);
        return ResponseEntity.ok(latest);

    }
    // ✅ 一键全完成
// POST /tasks/{id}/completeAll
    @PostMapping("/{id}/completeAll")
    public ResponseEntity<Task> completeAll(@PathVariable long id) {
        Task t = taskService.findById(id);
        if (t == null) return ResponseEntity.notFound().build();

        if (t.getSteps() != null) {
            for (int i = 0; i < t.getSteps().size(); i++) {
                taskService.setStepDone(id, i, true);
            }
        }
        taskService.refreshStatus(t);
        Task latest = taskService.findById(id);
        return ResponseEntity.ok(latest);
    }

    // ✅ 一键全取消（全部设回未完成）
// POST /tasks/{id}/uncompleteAll
    @PostMapping("/{id}/uncompleteAll")
    public ResponseEntity<Task> uncompleteAll(@PathVariable long id) {
        Task t = taskService.findById(id);
        if (t == null) return ResponseEntity.notFound().build();

        if (t.getSteps() != null) {
            for (int i = 0; i < t.getSteps().size(); i++) {
                taskService.setStepDone(id, i, false);
            }
        }
        taskService.refreshStatus(t);
        Task latest = taskService.findById(id);
        return ResponseEntity.ok(latest);
    }
    // ✅ 删除单个任务
// DELETE /tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable long id) {
        Task t = taskService.findById(id);
        if (t == null) return ResponseEntity.notFound().build();

        taskService.list().remove(t);
        return ResponseEntity.noContent().build(); // 204
    }

    // ✅ 清空所有任务
// DELETE /tasks
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        taskService.list().clear();
        return ResponseEntity.noContent().build(); // 204
    }




}