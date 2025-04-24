package com.tasktracker;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskCRUD {
    private JSONArray tasks;
    private int id = 1;

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
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet");
            return;
        }
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            System.out.println(task.toString());
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
            if (props.get("state") == state) {
                System.out.println(task.toString());
            }
        }
    }

    public void saveTasks() {
        try {
            FileWriter fw = new FileWriter("tasks.json");
            fw.write(tasks.toString());
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

    public JSONArray getTasks() {
        return tasks;
    }

    public void setTasks(JSONArray tasks) {
        this.tasks = tasks;
    }

}
