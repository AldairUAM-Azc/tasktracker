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
        JSONObject props = (JSONObject) task.get(String.valueOf(taskId));
        props.put("description", update);
        props.put("updatedAt", Instant.now());
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
        JSONObject props = (JSONObject) task.get(String.valueOf(taskId));
        props.put("state", State.inProgress);
        props.put("updatedAt", Instant.now());
        saveTasks();
        return true;
    }

    public boolean markDone(int taskId) {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex == null) {
            return false;
        }

        JSONObject task = (JSONObject) tasks.get(taskIndex);
        JSONObject props = (JSONObject) task.get(String.valueOf(taskId));
        props.put("state", State.done);
        props.put("updatedAt", Instant.now());
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
            JSONObject props = (JSONObject) task.get(task.keys().next()); // one single key
            if (props.get("state").equals(state.toString())) {
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
            if (task.has(String.valueOf(id))) {
                return i;
            }
        }
        return null;
    }

    private void printTask(JSONObject task) {
        StringBuilder sb = new StringBuilder();
        String id = task.keys().next();
        JSONObject props = (JSONObject) task.get(task.keys().next()); // one single key
        sb.append("Task (ID ").append(id).append(")\n");
        sb.append("  - Description: ").append(props.get("description")).append("\n");
        sb.append("  - State: ").append(props.get("state").toString()).append("\n");
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
