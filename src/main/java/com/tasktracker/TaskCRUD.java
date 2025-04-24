package com.tasktracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskCRUD {
    private JSONArray tasks;
    private Map<String, Status> statuses;

    public TaskCRUD() {
        statuses = new HashMap<String, Status>();
        statuses.put("todo", Status.todo);
        statuses.put("inProgress", Status.inProgress);
        statuses.put("done", Status.done);

        tasks = new JSONArray();
        try {
            File tasksFile = new File("tasks.json");
            if (tasksFile.createNewFile()) {
                FileWriter fw = new FileWriter("tasks.json");
                fw.write(tasks.toString());
                fw.close();
            }
            String content = new String(Files.readAllBytes(Paths.get("tasks.json")));
            tasks = new JSONArray(content);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int addTask(String description) throws JSONException {
        Task task = new Task();
        int id = getNextTaskId();
        task.setId(id);
        task.setDescription(description);
        task.setStatus(Status.todo);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        tasks.put(task.toJSON());
        saveTasks();
        return id;
    }

    public boolean updateTask(int taskId, String update) throws JSONException {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex == null) {
            return false;
        }
        JSONObject task = (JSONObject) tasks.get(taskIndex);
        task.put("description", update);
        task.put("updatedAt", Instant.now());
        saveTasks();
        return true;
    }

    public boolean deleteTask(int taskId) {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex == null) {
            return false;
        }
        tasks.remove(taskIndex);
        saveTasks();
        return true;
    }

    public boolean markInProgress(int taskId) {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex == null) {
            return false;
        }
        JSONObject task = (JSONObject) tasks.get(taskIndex);
        task.put("status", Status.inProgress);
        task.put("updatedAt", Instant.now());
        saveTasks();
        return true;
    }

    public boolean markDone(int taskId) {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex == null) {
            return false;
        }
        JSONObject task = (JSONObject) tasks.get(taskIndex);
        task.put("status", Status.done);
        task.put("updatedAt", Instant.now());
        saveTasks();
        return true;
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet");
            return;
        }
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            printTask(task);
        }
    }

    public boolean listTasks(String stateInput) {
        if (!statuses.containsKey(stateInput)) {
            return false;
        }

        Status status = statuses.get(stateInput);

        if (tasks.isEmpty()) {
            System.out.println("No tasks with status " + status.name() + " yet");
            return true;
        }

        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            if (task.get("status").equals(stateInput.toString())) {
                printTask(task);
            }
        }
        return true;
    }

    private void saveTasks() {
        try {
            FileWriter fw = new FileWriter("tasks.json");
            fw.write(tasks.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNextTaskId() {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }
        try {
            JSONObject lastTask = (JSONObject) tasks.get(tasks.length() - 1);
            int id = Integer.parseInt(lastTask.get("id").toString()) + 1;
            return id;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Integer findTaskIndex(int id) {
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            if (task.get("id").toString().equals(String.valueOf(id))) {
                return i;
            }
        }
        return null;
    }

    private void printTask(JSONObject task) {
        StringBuilder sb = new StringBuilder();
        sb.append("Task (ID ").append(task.get("id")).append(")\n");
        sb.append("  - Description: ").append(task.get("description")).append("\n");
        sb.append("  - State: ").append(task.get("status").toString()).append("\n");
        System.out.println(sb.toString());
    }

    public JSONArray getTasks() {
        return tasks;
    }

    public void setTasks(JSONArray tasks) {
        this.tasks = tasks;
    }

}
