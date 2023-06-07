package com.bank.mtransfer.models.payload;

public class StatisticUser {
    private String description;
    private long percentage;

    public StatisticUser(String description, long percentage) {
        this.description = description;
        this.percentage = percentage;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public long getPercentage() { return percentage; }

    public void setPercentage(long percentage) { this.percentage = percentage; }
}
