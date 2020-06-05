package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface DatabaseAdaptor {

    void addSensorData(Sensor data) throws SQLException;

    void addReport(Report report) throws SQLException;

    List<SensorWithSDate> getData() throws SQLException;

    List<SensorWithSDate> getDataFromTo(String from, String to) throws SQLException, ParseException;

    List<Report> getReport(String timestamp) throws SQLException;

    List<EmDefaultValue> getDefaultValueEm() throws SQLException; //replace the name and type of the method with the code you have


}