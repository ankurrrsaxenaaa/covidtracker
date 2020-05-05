package com.xebia.covidtracker.api.representations;

import com.xebia.covidtracker.domain.Record;

public class RecordResponse {
    private String location;
    private long tested;
    private long confirmed;
    private long active;
    private long recovered;
    private long dead;

    public RecordResponse(String location, long tested, long confirmed, long active, long recovered, long dead) {
        this.location = location;
        this.tested = tested;
        this.confirmed = confirmed;
        this.active = active;
        this.recovered = recovered;
        this.dead = dead;
    }

    public static RecordResponse from(Record record){
        return new RecordResponse(record.getLocation(),record.getTested(),record.getConfirmed(),record.getActive(),
                record.getRecovered(),record.getDead());
    }

    public String getLocation() {
        return location;
    }

    public long getTested() {
        return tested;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public long getActive() {
        return active;
    }

    public long getRecovered() {
        return recovered;
    }

    public long getDead() {
        return dead;
    }
}
