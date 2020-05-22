package com.example.Sep4_Data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sensor {
    private String sensorName;
    private String unitType;
    private double value;
    private long timestamp;
    @JsonCreator
    public Sensor(@JsonProperty("sensorName") String sensorName, @JsonProperty("unitType") String unitType, @JsonProperty("value") double value, @JsonProperty("timestamp") long timestamp) {
        this.sensorName = sensorName;
        this.unitType = unitType;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorName='" + sensorName + '\'' +
                ", unitType='" + unitType + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}