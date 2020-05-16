package com.example.Sep4_Data.model;

import java.sql.Timestamp;

public class Sensor {
    private String sensorName;
    private String unitType;
    private double value;
    private Timestamp timestamp;

    public Sensor() {
    }

    public Sensor(String sensorName, String unitType, double value) {
        this.sensorName = sensorName;
        this.unitType = unitType;
        this.value = value;
        timestamp= new Timestamp(System.currentTimeMillis());
    }

    public Sensor(String sensorName, String unitType, double value, Timestamp timestamp) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
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
