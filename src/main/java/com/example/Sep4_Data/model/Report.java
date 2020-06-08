package com.example.Sep4_Data.model;

public class Report {
    private double co2_value;
    private double humidity_value;
    private double temperature_value;
    private String timestamp;

    public Report(double co2_value, double humidity_value, double temperature_value, String timestamp){
        this.co2_value=co2_value;
        this.humidity_value=humidity_value;
        this.temperature_value=temperature_value;
        this.timestamp=timestamp;
    }

    public double getCo2_value() {
        return co2_value;
    }

    public void setCo2_value(double co2_value) {
        this.co2_value = co2_value;
    }

    public double getHumidity_value() {
        return humidity_value;
    }

    public void setHumidity_value(double humidity_value) {
        this.humidity_value = humidity_value;
    }

    public double getTemperature_value() {
        return temperature_value;
    }

    public void setTemperature_value(double temperature_value) {
        this.temperature_value = temperature_value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Report{" +
                "co2_value=" + co2_value +
                ", humidity_value=" + humidity_value +
                ", temperature_value=" + temperature_value +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
