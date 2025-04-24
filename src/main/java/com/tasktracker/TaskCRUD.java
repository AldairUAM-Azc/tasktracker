package com.tasktracker;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskCRUD {
    private JSONArray tasks;
    private int id;

    public TaskCRUD() {
    }

    public int addTask(String description) throws JSONException {
        Task task = new Task();
        task.setId(id);
        task.setDescription(description);
        task.setState(State.todo);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        tasks.put(task.toJSON());
        saveTasks();
        int currentId = id;
        id += 1;
        saveNextId();
        return currentId;
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
        task.put("state", State.inProgress);
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
        task.put("state", State.done);
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

    public void listTasks(State state) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks with state " + state.toString() + " yet");
            return;
        }
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            if (task.get("state").equals(state.toString())) {
                printTask(task);
            }
        }
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

    private void saveNextId() {
        try {
            FileWriter fw = new FileWriter("trackId.txt");
            fw.write(String.valueOf(id));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        sb.append("  - State: ").append(task.get("state").toString()).append("\n");
        System.out.println(sb.toString());
    }

    public JSONArray getTasks() {
        return tasks;
    }

    public void setTasks(JSONArray tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
