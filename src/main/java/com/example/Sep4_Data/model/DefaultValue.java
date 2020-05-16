package com.example.Sep4_Data.model;

import java.sql.Timestamp;

public class DefaultValue {
    private Timestamp timestamp;
    private String state;
    private boolean log;
    private String parameterType;
    private double parameterValue;

    public DefaultValue(Timestamp timestamp, String state, boolean log, String parameterType, double parameterValue) {
        this.timestamp = timestamp;
        this.state = state;
        this.log = log;
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }

    public DefaultValue() {
    }

    public DefaultValue(String state, boolean log, String parameterType, double parameterValue) {
       timestamp= new Timestamp(System.currentTimeMillis());
        this.state = state;
        this.log = log;
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
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

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public double getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(double parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public String toString() {
        return "DefaultValue{" +
                "timestamp=" + timestamp +
                ", state='" + state + '\'' +
                ", log=" + log +
                ", parameterType='" + parameterType + '\'' +
                ", parameterValue=" + parameterValue +
                '}';
    }
}
