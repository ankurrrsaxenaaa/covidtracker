package com.xebia.covidtracker.api.representations;

import com.xebia.covidtracker.domain.Record;

import java.util.Date;

public class RecordRequest {

    private long id;
    private String location;
    private long tested;
    private long confirmed;
    private long active;
    private long recovered;
    private long dead;

    public Record toRecord() {
        return new Record.Builder()
                .withLocation(this.location!=null?this.location.toLowerCase():null)
                .withTested(this.tested)
                .withConfirmed(this.confirmed)
                .withActive(this.active)
                .withRecovered(this.recovered)
                .withDead(this.dead)
                .withUpdatedAt(new Date())
                .build();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTested(long tested) {
        this.tested = tested;
    }

    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }

    public void setActive(long active) {
        this.active = active;
    }

    public void setRecovered(long recovered) {
        this.recovered = recovered;
    }

    public void setDead(long dead) {
        this.dead = dead;
    }
}
