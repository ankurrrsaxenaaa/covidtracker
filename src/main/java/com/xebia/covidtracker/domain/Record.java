package com.xebia.covidtracker.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "records")
public class Record {

    @Id
    @Column(unique=true)
    private String location;
    private long tested;
    private long confirmed;
    private long active;
    private long recovered;
    private long dead;
    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Record() {
    }

    private Record(Builder builder) {
        location = builder.location;
        tested = builder.tested;
        confirmed = builder.confirmed;
        active = builder.active;
        recovered = builder.recovered;
        dead = builder.dead;
        updatedAt = builder.updatedAt;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Record update(Record copyFrom) {
        this.tested += copyFrom.getTested();
        this.confirmed += copyFrom.getConfirmed();
        this.active += copyFrom.getActive();
        this.recovered += copyFrom.getRecovered();
        this.dead += copyFrom.getDead();
        this.updatedAt = new Date();
        return this;
    }

    public static final class Builder {
        private String location;
        private long tested;
        private long confirmed;
        private long active;
        private long recovered;
        private long dead;
        private Date updatedAt;

        public Builder() {
        }


        public Builder withLocation(String val) {
            location = val;
            return this;
        }

        public Builder withTested(long val) {
            tested = val;
            return this;
        }

        public Builder withConfirmed(long val) {
            confirmed = val;
            return this;
        }

        public Builder withActive(long val) {
            active = val;
            return this;
        }

        public Builder withRecovered(long val) {
            recovered = val;
            return this;
        }

        public Builder withDead(long val) {
            dead = val;
            return this;
        }

        public Builder withUpdatedAt(Date val) {
            updatedAt = val;
            return this;
        }

        public Record build() {
            return new Record(this);
        }
    }

    @Override
    public String toString() {
        return "Record{" +
                ", location='" + location + '\'' +
                ", tested=" + tested +
                ", confirmed=" + confirmed +
                ", active=" + active +
                ", recovered=" + recovered +
                ", dead=" + dead +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
