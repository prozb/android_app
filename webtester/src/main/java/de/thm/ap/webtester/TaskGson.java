package de.thm.ap.webtester;

public class TaskGson {
    private final long id;
    private String summary;
    private String description;
    private Status status;
    private int priority;

    @Override
    public String toString() {
        return summary + " " + id + " " + priority;
    }

    public TaskGson(long id, String summary, String description, Status status, int priority){
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public String getSummary() {
        return summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public enum Status{
        CREATED, ASSIGNED, CANCELED, COMPLETED
    }

    public long getId(){
        return id;
    }


}
