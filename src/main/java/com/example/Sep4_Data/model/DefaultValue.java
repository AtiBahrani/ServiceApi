package com.example.Sep4_Data.model;

import java.sql.Timestamp;

public class DefaultValue {
    private Timestamp timestamp;
    private String state;
   // private boolean log;
    private String parameterType;
    private double co2;
    private double humidity;
    private double temperature;

    public DefaultValue(Timestamp timestamp, String state,  String parameterType, double co2,double humidity, double temperature) {
        this.timestamp = timestamp;
        this.state = state;

        this.parameterType = parameterType;
        this.co2 = co2;
        this.humidity=humidity;
        this.temperature=temperature;
    }

    public DefaultValue() {
    }

    public DefaultValue(String state, String parameterType, double co2,double humidity, double temperature) {
       timestamp= new Timestamp(System.currentTimeMillis());
        this.state = state;
        this.parameterType = parameterType;
        this.co2 = co2;
        this.humidity=humidity;
        this.temperature=temperature;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }




    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }


    public double getCo2() {
        return co2;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "DefaultValue{" +
                "timestamp=" + timestamp +
                ", state='" + state + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", co2=" + co2 +
                ", humidity=" + humidity +
                ", temperature=" + temperature +
                '}';
    }
}
