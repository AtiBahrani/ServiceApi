package com.example.Sep4_Data.model;

public class EmDefaultValue {
    private String sensorName;
    private double value;

    public EmDefaultValue(String sensorName, double value) {
        this.sensorName = sensorName;
        this.value = value;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EmDefaultValue{" +
                "sensorName='" + sensorName + '\'' +
                ", value=" + value +
                '}';
    }
}
