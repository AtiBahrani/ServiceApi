package com.example.Sep4_Data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorWithSDate {
    private String sensorName;
    private String unitType;
    private double value;
    private String timestamp;

    @JsonCreator
    public SensorWithSDate(@JsonProperty("sensorName") String sensorName, @JsonProperty("unitType") String unitType, @JsonProperty("value") double value, @JsonProperty("timestamp") String timestamp) {
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorName='" + sensorName + '\'' +
                ", unitType='" + unitType + '\'' +
                ", value= " + value +
                ", timestamp= " + timestamp +
                '}';
    }
}