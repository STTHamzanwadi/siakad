package org.stth.siak.entity;

public class DashboardNotification {
    private long id;
    private String content;
    private boolean read;
    private String firstName;
    private String lastName;
    private String prettyTime;
    private String action;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrettyTime() {
        return prettyTime;
    }

    public void setPrettyTime(String prettyTime) {
        this.prettyTime = prettyTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
