package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Comparable<Task> {

    private int id;
    private String caption;
    private String description;
    private int priority;
    private Date deadline;
    private String status;
    private Date complete;

    public Task(int id, String caption, String description, int priority, Date deadline, String status, Date complete) {
        this.id = id;
        this.caption = caption;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
        this.complete = complete;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getComplete() {
        return complete;
    }

    public void setComplete(Date complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String completeReturn = "";
        if (complete == null) {
            completeReturn = "Не выполнено";
        } else {
            completeReturn = formatter.format(complete);
        }
        return "Задача{" +
                "id=" + id +
                ", заголовок='" + caption + '\'' +
                ", описание='" + description + '\'' +
                ", приоритет=" + priority +
                ", дата срока завершения=" + formatter.format(deadline) +
                ", статус='" + status + '\'' +
                ", дата выполнения=" + completeReturn +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        return (this.id - o.id);
    }
}
