package com.tasktracker;

import java.time.Instant;

import org.json.JSONObject;

public class Task {
    private int id;
    private String description;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;

    public Task() {
    }

    public Task(int id, String description, Status status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public JSONObject toJSON(){
        JSONObject task = new JSONObject();
        task.put("id", id);
        task.put("description", description);
        task.put("status", Status.todo);
        task.put("createdAt", Instant.now());
        task.put("updatedAt", Instant.now());
        return task;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", description=" + description + ", status=" + status + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
