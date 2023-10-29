package com.example.taskmanager.util;

import java.util.Date;

public class Task {
    private String name;
    private String description;
    private Date end_at;

    public Task() {
    }

    public Task(String name, String description, Date end_at) {
        this.name = name;
        this.description = description;
        this.end_at = end_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnd_at() {
        return end_at;
    }

    public void setEnd_at(Date end_at) {
        this.end_at = end_at;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", end_at=" + end_at +
                '}';
    }
}
