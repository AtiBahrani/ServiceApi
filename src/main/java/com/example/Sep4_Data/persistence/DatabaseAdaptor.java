package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.EmDefaultValue;
import com.example.Sep4_Data.model.Sensor;
import com.example.Sep4_Data.model.SensorWithSDate;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface DatabaseAdaptor {

    void addSensorData(Sensor data) throws SQLException;



    List<SensorWithSDate> getData() throws SQLException;
    public List<SensorWithSDate> getDataFromTo(String from, String to) throws SQLException, ParseException;

    List<EmDefaultValue> getDefaultValueEm()throws SQLException; //replace the name and type of the method with the code you have
    /*void addRoom(Room room);
    void addProfile(Profile profile);*/
}