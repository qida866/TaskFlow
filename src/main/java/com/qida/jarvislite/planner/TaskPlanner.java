package com.qida.jarvislite.planner;

import com.qida.jarvislite.model.Step;

import java.util.ArrayList;
import java.util.List;

public class TaskPlanner {

    public List<Step> decompose(String title) {
        List<Step> steps = new ArrayList<>();
        if (title == null || title.isBlank()) return steps;

        String t = title.toLowerCase();

        if (containsAny(t, "homework", "assignment", "cs", "math", "study", "quiz", "exam", "midterm", "final")) {
            add(steps, "Read the requirements / rubric");
            add(steps, "Break down problems into smaller parts");
            add(steps, "Work on the hardest part first");
            add(steps, "Finish remaining parts and write up");
            add(steps, "Review, polish, and submit");
            return steps;
        }

        if (containsAny(t, "project", "build", "develop", "api", "backend", "frontend", "java", "spring", "bug", "debug")) {
            add(steps, "Clarify scope and success criteria");
            add(steps, "Design data model and API endpoints");
            add(steps, "Implement core functionality");
            add(steps, "Add error handling and basic tests");
            add(steps, "Refactor, document, and demo");
            return steps;
        }

        if (containsAny(t, "intern", "internship", "resume", "cv", "linkedin", "apply", "job")) {
            add(steps, "Identify the target role and requirements");
            add(steps, "Map your experiences to required skills");
            add(steps, "Draft / update resume bullets");
            add(steps, "Tailor for the job and submit application");
            add(steps, "Prepare interview questions and practice");
            return steps;
        }

        if (containsAny(t, "presentation", "slides", "pitch", "demo", "report")) {
            add(steps, "Outline the key message and structure");
            add(steps, "Collect supporting content and visuals");
            add(steps, "Create slides / write the report draft");
            add(steps, "Practice and refine based on feedback");
            add(steps, "Finalize and deliver");
            return steps;
        }

        add(steps, "Understand requirements");
        add(steps, "Design solution");
        add(steps, "Implement work");
        add(steps, "Review and finalize");
        return steps;
    }

    private void add(List<Step> steps, String text) {
        steps.add(new Step(text, false));
    }

    private boolean containsAny(String text, String... keywords) {
        for (String k : keywords) if (text.contains(k)) return true;
        return false;
    }
}
