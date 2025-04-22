package com.tasktracker;

import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskCRUD {
    private JSONArray tasks;
    private int id = 1;

    public TaskCRUD() {
        tasks = new JSONArray();
        for (; id < 5; id++) {
            JSONObject taskInfo = new JSONObject();
            taskInfo.put("description", "TEST " + id);
            taskInfo.put("state", State.inProgress);
            taskInfo.put("createdAt", Instant.now());
            taskInfo.put("updatedAt", Instant.now());

            JSONObject taskId = new JSONObject();
            taskId.put(String.valueOf(id++), taskInfo);

            tasks.put(taskId);
        }
    }

    public int addTask(String description) throws JSONException {
        Task task = new Task();
        task.setId(id);
        task.setDescription(description);
        task.setState(State.todo);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        tasks.put(task.toJSON());
        listTasks();
        return id++;
    }

    public void updateTask(int taskId, String update) throws JSONException {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex != null) {
            JSONObject task = (JSONObject) tasks.get(taskIndex);
            JSONObject props = (JSONObject) task.get(String.valueOf(taskId));
            props.put("description", update);
            props.put("updatedAt", Instant.now());
            listTasks();
        } else {
            System.err.println("Task (ID " + taskId + ") not found");
        }
    }

    public void deleteTask(int taskId) {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex != null) {
            tasks.remove(taskIndex);
        } else {
            System.err.println("Task (ID " + taskId + ") not found");
        }
        listTasks();
    }

    public void markInProgress(int taskId) {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex != null) {
            JSONObject task = (JSONObject) tasks.get(taskIndex);
            JSONObject props = (JSONObject) task.get(String.valueOf(taskId));
            props.put("state", State.inProgress);
            props.put("updatedAt", Instant.now());
        } else {
            System.err.println("Task (ID " + taskId + ") not found");
        }
        listTasks();
    }

    public void markDone(int taskId) {
        Integer taskIndex = findTaskIndex(taskId);
        if (taskIndex != null) {
            JSONObject task = (JSONObject) tasks.get(taskIndex);
            JSONObject props = (JSONObject) task.get(String.valueOf(taskId));
            props.put("state", State.done);
            props.put("updatedAt", Instant.now());
        } else {
            System.err.println("Task (ID " + taskId + ") not found");
        }
        listTasks();
    }

    public void listTasks() {
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            System.out.println(task.toString());
        }
    }

    public void listTasks(State state) {
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            JSONObject props = (JSONObject) task.get(task.keys().next()); // one single key
            if (props.get("state") == state) {
                System.out.println(task.toString());
            }
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

}
