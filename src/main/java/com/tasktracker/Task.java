package com.tasktracker;

import java.time.Instant;

import org.json.JSONObject;

public class Task {
    private int id;
    private String description;
    private State state;
    private Instant createdAt;
    private Instant updatedAt;

    public Task() {
    }

    public Task(int id, String description, State state, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.description = description;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public JSONObject toJSON(){
        JSONObject task = new JSONObject();
        task.put("id", id);
        task.put("description", description);
        task.put("state", State.todo);
        task.put("createdAt", Instant.now());
        task.put("updatedAt", Instant.now());
        return task;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", description=" + description + ", state=" + state + ", createdAt=" + createdAt
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
